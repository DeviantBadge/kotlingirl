package com.kotlingirl.gameservice.game

import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.gameservice.game.entities.Bomb
import com.kotlingirl.gameservice.game.entities.Bonus
import com.kotlingirl.gameservice.game.entities.BonusBomb
import com.kotlingirl.gameservice.game.entities.BonusExplosion
import com.kotlingirl.gameservice.game.entities.BonusSpeed
import com.kotlingirl.gameservice.game.entities.Fire
import com.kotlingirl.gameservice.game.entities.Pawn
import com.kotlingirl.gameservice.game.entities.Tile
import com.kotlingirl.gameservice.game.entities.Wall
import com.kotlingirl.gameservice.game.entities.Wood
import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import kotlin.random.Random

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
    var bonuses = mutableListOf<Bonus>()
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
            it.reset()
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
                            is Tile -> {
                                bar.isColliding(Bar(coordToPoint(it), Point(coordToPoint(it).x + 31, coordToPoint(it).y + 31)))
/*                                if(bar.isColliding(element)) {
                                    pawn.steps = stepCounter(pawn, element)
                                } else {
                                    pawn.steps = pawn.velocity
                                }
                                false*/
                            }
                            is Bomb -> bar.isColliding(element.bar)
                            is Bonus -> {
                                if(bar.isColliding(element)) {
                                    cell.clear()
                                    pawn.applyBonus(element)
                                    element.taken = true
                                }
                                false
                            }
                            else -> false
                        }
                    }
                    else false }
                .reduce{first, second -> first || second}
    }

    private fun stepCounter(pawn: Pawn, tile: Tile): Int {
        val bar = pawn.bar
        val dir = pawn.direction
        return when (dir) {
            "UP" -> {tile.leftBottomCorner.y - bar.rightTopCorner.y - 1}
            "DOWN" -> {bar.leftBottomCorner.y - tile.rightTopCorner.y - 1}
            "LEFT" -> {bar.leftBottomCorner.x - tile.rightTopCorner.x - 1}
            "RIGHT" -> {tile.leftBottomCorner.x - tile.rightTopCorner.x - 1}
            else -> throw NotImplementedError()
        }
    }

    private fun findNeighbour(coord: Coord, direction: String, strength: Int = 1): Coord {
        when(direction) {
            "UP" -> {
                if (coord.y + strength <= h - 1)
                    return Point(coord.x, coord.y + strength)
            }
            "DOWN" -> {
                if (coord.y - strength >= 0)
                    return Point(coord.x, coord.y - strength)
            }
            "LEFT" -> {
                if (coord.x - strength >= 0)
                    return Point(coord.x - strength, coord.y)
            }
            "RIGHT" -> {
                if (coord.x + strength <= w - 1)
                    return Point(coord.x + strength, coord.y)
            }
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
        val coordOfBomb = pointToCoord(bomb.position)
        var directions = mutableSetOf("UP", "DOWN", "LEFT", "RIGHT", "")
        val neighbours = mutableListOf<Point>()

        val excludedDirections = mutableSetOf<String>()
        for (strength in 1..bomb2player[bomb]!!.bombStrength) {
            directions = directions.subtract(excludedDirections).toMutableSet()
            for (dir in directions) {
                val neighbour = findNeighbour(coordOfBomb, dir, strength)
                if (field[neighbour].isNotEmpty() && field[neighbour].last() is Wall) {
                    excludedDirections.add(dir)
                } else {
                    if (!isWarm) {
                        pawns.values.forEach { if (it.coords.contains(neighbour)) it.alive = false }
                    }
                    neighbours.add(neighbour)
                    if (field[neighbour].isNotEmpty() && field[neighbour].last() is Wood) {
                        excludedDirections.add(dir)
                    }
                }
            }
        }

        // ищем все ящики вокруг бомбы, но пока не удаляем их из матрицы
        val woods = neighbours.filter { field[it].isNotEmpty() }
                .filter { field[it].last() is Wood }
        val returnWoods = woods.map{ field[it].last() as Wood }.toMutableList<Any>()
        neighbours.forEach { if(field[it].isEmpty() || field[it].last() !is Wall) fires.add(Fire(idGen.getId(), coordToPoint(it))) }
        // после того, как мы сгенерировали выходной массив и огонь, можем удалить ящики из матрицы
        woods.forEach {
            val bonus = isBonus(coordToPoint(it))
            if (bonus != null) {
                field[it].add(bonus); returnWoods.add(bonus); bonuses.add(bonus)
            } else {
                field[it].clear()
            }
        }
        bomb2player[bomb]!!.bombsCount++
        bomb2player.remove(bomb)
        field[coordOfBomb].clear()
        return returnWoods
    }

    private fun isBonus(position: Point): Bonus? {
        val rand = Random.nextInt(3)
        return when(rand) {
            0 -> makeBonus(position)
            else -> null
        }
    }

    private fun makeBonus(position: Point): Bonus {
        val rand = Random.nextInt(3)
        return when (rand) {
            0 -> BonusExplosion(idGen.getId(), position)
            1 -> BonusSpeed(idGen.getId(), position)
            2 -> BonusBomb(idGen.getId(), position)
            else -> throw NotImplementedError()
        }
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
        bonuses.clear()
    }
}