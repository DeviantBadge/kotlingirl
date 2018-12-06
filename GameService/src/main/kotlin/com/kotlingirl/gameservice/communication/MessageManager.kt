package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.Mechanics
import com.kotlingirl.gameservice.game.Pawn
import com.kotlingirl.gameservice.game.Tile
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import java.rmi.activation.UnknownObjectException
import java.util.concurrent.ConcurrentLinkedQueue

class MessageManager(val mechanics: Mechanics, val broker: Broker) {

    var inputQueue = ConcurrentLinkedQueue<Pair<WebSocketSession, Message> >()
    var replica : Replica? = Replica(Topic.REPLICA, Data(emptyList(), false))

    fun makeReplica(elapsed: Long): Replica? {
        val objects = mutableListOf<Any>()
        val stopPawns = mutableSetOf<Pawn>()
        stopPawns.addAll(mechanics.pawns.values)
        if (inputQueue.isNotEmpty()) {
            val batchSize = inputQueue.size
            log.info("Size of copied queue size = ${batchSize}")
            for (i in 0 until batchSize) {
                val pair = inputQueue.poll()
                when (pair.second.topic) {
                    Topic.MOVE -> {
                        val moveData: MoveData = pair.second.data
                        val pawn = mechanics.pawns[pair.first]
                        stopPawns.remove(pawn)
                        if (pawn != null && !(objects.contains(pawn.dto))) {
                            pawn.direction = moveData.direction
                            if (!mechanics.checkColliding(pawn)) {
                                pawn.tick(elapsed)
                            }
                            objects.add(pawn.dto)
                        }
                    }

                    Topic.PLANT_BOMB -> {

                    }

                    Topic.JUMP -> {

                    }

                    else -> throw UnknownObjectException("Not such Topic type")
                }
            }
        }
        stopPawns.forEach { it.direction = "" ; objects.add(it.dto) }
        return if (objects.isNotEmpty()) {
            log.info("last state of pawn ${objects}")
            Replica(Topic.REPLICA, Data(objects, false))
        } else null
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
            line.forEach { if (it.isNotEmpty())
                when (it.last()) {
                    is Pawn -> replicas.add((it.last() as Pawn).dto)
                    is Tile -> replicas.add(it.last())
                }
            } }
        broker.broadcast(Topic.REPLICA, Data(replicas, false))
    }


    companion object {
        private val log = logger()
    }
}