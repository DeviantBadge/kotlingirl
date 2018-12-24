package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.Mechanics
import org.springframework.web.socket.WebSocketSession
import java.rmi.activation.UnknownObjectException
import java.util.concurrent.ConcurrentLinkedQueue

class QueueReader(private val mechanics: Mechanics) {
    val objects = mutableListOf<Any>()

    fun read(inputQueue: ConcurrentLinkedQueue<Pair<WebSocketSession, Message>>, elapsed: Long): MutableList<Any> {
        objects.clear()
        if (inputQueue.isNotEmpty()) {
            val snapShot = inputQueue.toList()
            inputQueue.removeAll(snapShot)
            snapShot.forEach {
                when (it.second.topic) {
                    Topic.MOVE -> movePawn(it, elapsed)
                    Topic.PLANT_BOMB, Topic.JUMP -> plantBomb(it)
                    else -> throw UnknownObjectException("Not such Topic type")
                }
            }
        }
        return objects
    }

    private fun plantBomb(pair: Pair<WebSocketSession, Message>) {
        val bomb = mechanics.plantBomb(pair.first)
        if (bomb != null) {
            objects.add(0, bomb.dto)
        }
    }

    private fun movePawn(pair: Pair<WebSocketSession, Message>, elapsed: Long) {
        val moveData: MoveData = pair.second.data as MoveData
        val pawn = mechanics.pawns[pair.first]
        if (pawn != null && !(objects.contains(pawn.dto))) {
            pawn.direction = moveData.direction
            if (!mechanics.checkColliding(pawn)) {
                pawn.tick(elapsed)
            }
            objects.add(pawn.dto)
        }
    }
}