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

class Mechanics {

    val h = 17
    val w = 27
    val tileSize = 32
    val field = Matrix()
    var pawns = HashMap<WebSocketSession, Pawn>()
    var bombs = mutableListOf<Bomb>()
    var fires = mutableListOf<Fire>()

    fun createPawn(session: WebSocketSession, user: User) {
        val count = pawns.size
        pawns[session] = Pawn(idGen.getId(), count)
    }

    fun initPawns() {
        val curCoord = Point(1, h - 2)
        var count = 0
        for (entry in pawns) {
            entry.value.changePosition(coordToPoint(curCoord)).also { log.info("Begin state: ${entry.value.dto}") }
            with(curCoord) {
                field[x][y].add(entry.value)
                when {
                    x == 1 -> x = w - 2
                    y == h - 2 -> y = 1
                    x == w - 2 -> x = 1
                    y == 1 -> y = h - 2
                }
            }
        }
    }

    fun checkColliding(pawn: Pawn): Boolean {
        val neighbours = pawn.coords.map { findNeighbour(it, pawn.direction) }
        return neighbours.map {
            val cell = field[it.x][it.y]
            if (cell.isNotEmpty() && cell.last() is Tile) {
                val bar = Bar(Point(pawn.bar.leftBottomCorner.x - pawn.velocity, pawn.bar.leftBottomCorner.y - pawn.velocity),
                                Point(pawn.bar.rightTopCorner.x + pawn.velocity, pawn.bar.rightTopCorner.y + pawn.velocity))
                bar.isColliding(Bar(coordToPoint(it), Point(coordToPoint(it).x + 31, coordToPoint(it).y + 31)))
            }
            else
                false }
                .reduce{first, second -> first || second}
    }

    private fun findNeighbour(point: Point, direction: String): Point {
        when(direction) {
            "UP" -> {
                if (point.y + 1 <= h - 1)
                    return Point(point.x, point.y + 1)
            }
            "DOWN" -> {
                if (point.y - 1 >= 0)
                    return Point(point.x, point.y - 1)
            }
            "LEFT" -> {
                if (point.x - 1 >= 0)
                    return Point(point.x - 1, point.y)
            }
            "RIGHT" -> {
                if (point.x + 1 <= w - 1)
                    return Point(point.x + 1, point.y)
            }
/*            "UP-RIGHT" -> {
                if (point.x + 1 <= w - 1 && point.y + 1 <= h - 1)
                    return Point(point.x + 1, point.y + 1)
            }
            "UP-LEFT" -> {
                if (point.x - 1 >= 0 && point.y + 1 <= h - 1)
                    return Point(point.x - 1, point.y + 1)
            }
            "DOWN-RIGHT" -> {
                if (point.x + 1 <= w - 1 && point.y - 1 >= 0)
                    return Point(point.x + 1, point.y - 1)
            }
            "DOWN-LEFT" -> {
                if (point.x - 1 >= 0 && point.y - 1 >= 0)
                    return Point(point.x - 1, point.y - 1)
            }*/
            else -> return point
        }
        return point
    }

    fun plantBomb(session: WebSocketSession): Bomb {
        var bomb = Bomb(0, Point(0, 0))
        val pawn = pawns[session]
        if (pawn != null) {
            with(pawn) {
                bomb = Bomb(idGen.getId(), Point(bar.leftBottomCorner.x, bar.leftBottomCorner.y))
            }
        }
        bombs.add(bomb)
        return bomb
    }

    fun explose(bomb: Bomb): List<Any> {
        bomb.explosed = true
        val coord = pointToCoord(bomb.position)
        val directions = listOf("UP", "DOWN", "LEFT", "RIGHT", "UP-RIGHT", "UP-LEFT", "DOWN-RIGHT", "DOWN-LEFT")
        val neighbours = mutableListOf(coord)
        pawns.values.forEach { if (it.coords.contains(coord)) it.alive = false }
        directions.forEach { dir ->
            val neighbour = findNeighbour(coord, dir)
            pawns.values.forEach { if (it.coords.contains(neighbour)) it.alive = false }
                neighbours.add(neighbour) }
        // ищем все ящики вокруг бомбы, но пока не удаляем их из матрицы
        val woods = neighbours.filter { field[it.x][it.y].isNotEmpty() }
                .filter { field[it.x][it.y].last() is Wood }
        val returnWoods = woods.map{ field[it.x][it.y].last() as Wood }
        woods.forEach { fires.add(Fire(idGen.getId(), coordToPoint(Point(it.x, it.y)))) }
        // после того, как мы сгенерировали выходной массив и огонь, можем удалить ящики из матрицы
        woods.forEach { field[it.x][it.y].clear() }
        return returnWoods
    }

    private fun coordToPoint(coord: Point): Point  = Point(coord.x * tileSize, coord.y * tileSize)
    private fun pointToCoord(point: Point): Point  = Point(point.x / tileSize, point.y / tileSize)

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
                    field[i][j].add(Wall(idGen.getId(), Point(i * tileSize, j * tileSize)))
                else if ( !(i == 1 && j == 1 || i == 2 && j == 1 || i == 1 && j == 2 ||
                                i == 1 && j == h - 2 || i == 2 && j == h - 2 || i == 1 && j == h - 3 ||
                                i == w - 2 && j == h - 2 || i == w - 2 && j == h - 3 || i == w - 3 && j == h - 2 ||
                                i == w - 2 && j == 1 || i == w - 2 && j == 2 || i == w - 3 && j == 1))
                    field[i][j].add(Wood(idGen.getId(), Point(i * tileSize, j * tileSize)))
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
}