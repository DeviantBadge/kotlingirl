package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession

typealias Line = Array < Array <Any> >
typealias Matrix = ArrayList <Line>

class Mechanics {

    val h = 17
    val w = 27
    val tileSize = 32
    val field = Matrix()
    var pawns = HashMap<WebSocketSession, Pawn>()

    fun createPawn(session: WebSocketSession, user: User) {
        pawns[session] = Pawn(user.id)
    }

    private fun initMatrix() {
        for(i in 0..(w - 1)) {
            val line: Line = Array(h) { emptyArray<Any>() }
            field.add(line)
        }
    }

    private fun makeLabyrinth() {
        for (i in 0 until field.size)
            for (j in 0 until field[i].size) {
                if (i == 0 || j == 0 || i == field.size - 1 || j == field[i].size - 1)
                    field[i][j] = arrayOf(Wall(idGen.getId(), Point(i * tileSize, j * tileSize)))
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