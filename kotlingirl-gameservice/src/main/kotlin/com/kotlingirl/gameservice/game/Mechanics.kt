package com.kotlingirl.gameservice.game

import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.gameservice.game.entities.Bomb
import com.kotlingirl.gameservice.game.entities.Fire
import com.kotlingirl.gameservice.game.entities.Pawn
import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession

typealias Line = Array < MutableList <Any> >
typealias Matrix = ArrayList <Line>
typealias Coord = Point

operator fun Matrix.get(coord: Coord) = this[coord.x][coord.y]
operator fun Matrix.set(coord: Coord, obj: Any) { this[coord.x][coord.y].add(obj) }
class Mechanics {

    var isWarm = true
    val h = 17
    val w = 27
    val tileSize = 32
    val field = Matrix()
    var pawns = HashMap<WebSocketSession, Pawn>()
    var bombs = mutableListOf<Bomb>()
    var bomb2player = mutableMapOf<Bomb, Pawn>()
    var fires = mutableListOf<Fire>()
    var curCoord = Coord(1, h - 2)

    fun createPawn(session: WebSocketSession, user: User) {
        val count = pawns.size
        pawns[session] = Pawn(count).also { it.id = idGen.getId(); it.changePosition(coordToPoint(curCoord)) }
        with(curCoord) {
            when {
                x == 1 -> x = w - 2
                y == h - 2 -> y = 1
                x == w - 2 -> x = 1
                y == 1 -> y = h - 2
            }
        }
    }

    fun initPawns() {
        pawns.values.forEach {
            with(curCoord) {
                when (it.count) {
                    0 -> { x = 1; y = h - 2 }
                    1 -> { x = w - 2; y = h - 2 }
                    2 -> { x = w - 2; y = 1}
                    3 -> { x = 1; y = 1}
                }
            }
            it.changePosition(coordToPoint(curCoord))
            it.id = idGen.getId()
        }
    }

    fun checkColliding(pawn: Pawn): Boolean {
        val neighbours = pawn.coords.map { findNeighbour(it, pawn.direction) }
        val bar = Bar(Point(pawn.bar.leftBottomCorner.x - pawn.velocity, pawn.bar.leftBottomCorner.y - pawn.velocity),
                Point(pawn.bar.rightTopCorner.x + pawn.velocity, pawn.bar.rightTopCorner.y + pawn.velocity))
        return neighbours.map {
                    val cell = field[it.x][it.y]
                    if (cell.isNotEmpty()) {
                        val element = cell.last()
                        when(element) {
                             is Tile -> bar.isColliding(Bar(coordToPoint(it), Point(coordToPoint(it).x + 31, coordToPoint(it).y + 31)))
                             is Bomb -> bar.isColliding(element.bar)
                            else -> false
                        }
                    }
                    else false }
                .reduce{first, second -> first || second}
    }

    private fun findNeighbour(coord: Coord, direction: String): Coord {
        when(direction) {
            "UP" -> {
                if (coord.y + 1 <= h - 1)
                    return Point(coord.x, coord.y + 1)
            }
            "DOWN" -> {
                if (coord.y - 1 >= 0)
                    return Point(coord.x, coord.y - 1)
            }
            "LEFT" -> {
                if (coord.x - 1 >= 0)
                    return Point(coord.x - 1, coord.y)
            }
            "RIGHT" -> {
                if (coord.x + 1 <= w - 1)
                    return Point(coord.x + 1, coord.y)
            }
/*            "UP-RIGHT" -> {
                if (coord.x + 1 <= w - 1 && coord.y + 1 <= h - 1)
                    return Point(coord.x + 1, coord.y + 1)
            }
            "UP-LEFT" -> {
                if (coord.x - 1 >= 0 && coord.y + 1 <= h - 1)
                    return Point(coord.x - 1, coord.y + 1)
            }
            "DOWN-RIGHT" -> {
                if (coord.x + 1 <= w - 1 && coord.y - 1 >= 0)
                    return Point(coord.x + 1, coord.y - 1)
            }
            "DOWN-LEFT" -> {
                if (coord.x - 1 >= 0 && coord.y - 1 >= 0)
                    return Point(coord.x - 1, coord.y - 1)
            }*/
            else -> return coord
        }
        return coord
    }

    fun plantBomb(session: WebSocketSession): Bomb? {
        var bomb: Bomb? = null
        val pawn = pawns[session]
        if (pawn != null && pawn.bombsCount > 0) {
            with(pawn) {
                bomb = Bomb(idGen.getId(), coordToPoint(pointToCoord(Point(bar.leftBottomCorner.x, bar.leftBottomCorner.y))))
                bombsCount--
                bomb2player[bomb!!] = this
                bombs.add(bomb!!)
                field[pointToCoord(bomb!!.position)] = bomb!!
            }
        }
        return bomb
    }

    fun explose(bomb: Bomb): List<Any> {
        bomb.explosed = true
        val coord = pointToCoord(bomb.position)
        val directions = listOf("UP", "DOWN", "LEFT", "RIGHT", "")
        val neighbours = mutableListOf<Point>()
        //pawns.values.forEach { if (it.coords.contains(coord)) it.alive = false }
        directions.forEach { dir ->
            val neighbour = findNeighbour(coord, dir)
            if (!isWarm) {
                pawns.values.forEach { if (it.coords.contains(neighbour)) it.alive = false }
            }
                neighbours.add(neighbour) }
        // ищем все ящики вокруг бомбы, но пока не удаляем их из матрицы
        val woods = neighbours.filter { field[it.x][it.y].isNotEmpty() }
                .filter { field[it.x][it.y].last() is Wood }
        val returnWoods = woods.map{ field[it.x][it.y].last() as Wood }
        neighbours.forEach { fires.add(Fire(idGen.getId(), coordToPoint(it))) }
        // после того, как мы сгенерировали выходной массив и огонь, можем удалить ящики из матрицы
        woods.forEach { field[it.x][it.y].clear() }
        bomb2player[bomb]!!.bombsCount++
        bomb2player.remove(bomb)
        field[coord].clear()
        return returnWoods
    }

    private fun coordToPoint(coord: Coord): Point  = Point(coord.x * tileSize, coord.y * tileSize)
    private fun pointToCoord(point: Point): Coord  = Coord(point.x / tileSize, point.y / tileSize)

    private fun initMatrix() {
        for(i in 0..(w - 1)) {
            val line: Line = Array(h) { mutableListOf<Any>() }
            field.add(line)
        }
    }

    private fun makeLabyrinth() {
        for (i in 0 until field.size)
            for (j in 0 until field[i].size) {
                if (i == 0 || j == 0 || i == field.size - 1 || j == field[i].size - 1 || ( i % 2 == 0 && j % 2 == 0))
                    field[i][j] = mutableListOf(Wall(idGen.getId(), Point(i * tileSize, j * tileSize)))
                else if ( !(i == 1 && j == 1 || i == 2 && j == 1 || i == 1 && j == 2 ||
                                i == 1 && j == h - 2 || i == 2 && j == h - 2 || i == 1 && j == h - 3 ||
                                i == w - 2 && j == h - 2 || i == w - 2 && j == h - 3 || i == w - 3 && j == h - 2 ||
                                i == w - 2 && j == 1 || i == w - 2 && j == 2 || i == w - 3 && j == 1))
                    field[i][j] = mutableListOf(Wood(idGen.getId(), Point(i * tileSize, j * tileSize)))
            }
    }

    companion object {
        private val log = logger()
        private val idGen = IntIdGen()
    }

    init {
        initMatrix()
        makeLabyrinth()
    }

    fun init() {
        makeLabyrinth()
        bombs.clear()
        bomb2player.clear()
        fires.clear()
    }
}