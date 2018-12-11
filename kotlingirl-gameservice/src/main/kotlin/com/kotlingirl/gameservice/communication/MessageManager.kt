package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.Mechanics
import com.kotlingirl.gameservice.game.Tile
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import java.rmi.activation.UnknownObjectException
import java.util.concurrent.ConcurrentLinkedQueue

class MessageManager(private val mechanics: Mechanics, private val broker: Broker) {

    var inputQueue = ConcurrentLinkedQueue<Pair<WebSocketSession, Message> >()
    val objects = mutableListOf<Any>()

    fun makeReplica(elapsed: Long): Replica? {
        objects.clear()
        if (inputQueue.isNotEmpty()) {
            val batchSize = inputQueue.size
            log.info("Size of copied queue size = ${batchSize}")
            for (i in 0 until batchSize) {
                val pair = inputQueue.poll()
                when (pair.second.topic) {
                    Topic.MOVE -> {
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

                    Topic.PLANT_BOMB, Topic.JUMP -> {
                        val bomb = mechanics.plantBomb(pair.first)
                        if (bomb != null) {
                            objects.add(0, bomb.dto)
                        }
                    }

                    else -> throw UnknownObjectException("Not such Topic type")
                }
            }
        }
        consumeObjects(elapsed)
        return if (objects.isNotEmpty()) {
//            log.info("last state of pawn ${objects}")
            return Replica(Topic.REPLICA, Data(objects, false))
        } else null
    }

    private fun consumeObjects(elapsed: Long) {
        consumeGameOver(elapsed)
        consumeBombs(elapsed)
        consumePawns()
        consumeFires(elapsed)
    }

    private fun consumeGameOver(elapsed: Long) {
        val closableSessions = mutableListOf<WebSocketSession>()
        if (mechanics.pawns.size == 1) {
            mechanics.pawns.forEach { session, _ ->
                broker.send(session, Topic.GAME_OVER, "You Win!!!")
                closableSessions.add(session)
            }
        }

        mechanics.pawns.forEach { session, pawn ->
            if (!pawn.alive) {
                if(pawn.deadTime <= 0) {
                    broker.send(session, Topic.GAME_OVER, "Game Over")
                    closableSessions.add(session)
                } else {
                    pawn.tick(elapsed)
                }
            }
        }
        closableSessions.forEach { mechanics.pawns.remove(it) }
        closableSessions.forEach { it.close() }
    }

    private fun consumeBombs(elapsed: Long) {
        mechanics.bombs.forEach {
            if (it.timeLeft == 0) objects.addAll(mechanics.explose(it))
            else {
                objects.add(it.dto)
                it.tick(elapsed)
            }
        }
        mechanics.bombs.removeIf { it.explosed }
    }

    private fun consumePawns() {
        val objPawns = objects.filter { it is PawnDto }.map { it as PawnDto }
        mechanics.pawns.values.forEach {
            if (!it.alive) {
                if (objPawns.contains(it.dto)) {
                    objects.remove(it.dto); objects.add(it.dto)
                }
            }
        }
        mechanics.pawns.values.forEach {
            if (!objPawns.contains(it.dto)) {
                it.direction = ""; objects.add(it.dto)
            }
        }
    }

    private fun consumeFires(elapsed: Long) {
        mechanics.fires.removeIf { it.leftTime == 0 }
        mechanics.fires.forEach { objects.add(it.dto); it.tick(elapsed) }
    }

    fun addMessage(session: WebSocketSession, msg: Message) {
        inputQueue.add(Pair(session, msg))
    }

    fun broadcastReplica(replica: Replica?) {
        if (replica != null) {
            broker.broadcast(replica.topic, replica.data)
        }
    }

    fun broadcastInitState() {
        mechanics.initPawns()
        val replicas = mutableListOf<Any>()
        mechanics.field.forEach { line ->
            line.forEach { if (it.isNotEmpty() && it.last() is Tile)
                    replicas.add(it.last() as Tile)
            }
        }
        mechanics.pawns.values.forEach { replicas.add(it.dto) }
        broker.broadcast(Topic.REPLICA, Data(replicas, false))
    }


    companion object {
        private val log = logger()
    }
}