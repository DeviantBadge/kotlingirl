package com.kotlingirl.gameservice.game

import com.kotlingirl.gameservice.communication.User
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

    fun createPawn(session: WebSocketSession, user: User) {
        pawns[session] = Pawn(idGen.getId())
    }

    fun initPawns() {
        val curCoord = Point(1, h - 2)
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
            else -> return point
        }
        return point
    }

    fun plantBomb(session: WebSocketSession): Bomb {
        var bomb = Bomb(0, Point(0, 0))
        val pawn = pawns[session]
        if (pawn != null) {
            with(pawn) {
                bomb = Bomb(idGen.getId(), Point(position.x, position.y))
            }
        }
        bombs.add(bomb)
        return bomb
    }

    private fun coordToPoint(coord: Point): Point  = Point(coord.x * tileSize, coord.y * tileSize)

    private fun initMatrix() {
        for(i in 0..(w - 1)) {
            val line: Line = Array(h) { mutableListOf<Any>() }
            field.add(line)
        }
    }

    private fun makeLabyrinth() {
        for (i in 0 until field.size)
            for (j in 0 until field[i].size) {
                if (i == 0 || j == 0 || i == field.size - 1 || j == field[i].size - 1)
                    field[i][j] = mutableListOf(Wall(idGen.getId(), Point(i * tileSize, j * tileSize)))
            }
//        field[2][10].add(Wall(idGen.getId(), Point(2 * tileSize, 10 * tileSize)))
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