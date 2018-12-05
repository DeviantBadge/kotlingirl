package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.Mechanics
import com.kotlingirl.gameservice.game.Tile
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedQueue

class MessageManager(val mechanics: Mechanics, val broker: Broker) {

    var inputQueue = ConcurrentLinkedQueue<Pair<WebSocketSession, Message> >()

    fun consume(): Message? {
        if (inputQueue.isNotEmpty()) {
/*            val size = inputQueue.size
            log.info("Internal of copied queue: size = ${size}")
            val lst = mutableListOf<Any>()
            for (i in 0..(size - 1))
            {
                val pair = inputQueue.poll()
                if (pair.second.topic == Topic.MOVE) {
                    val moveData: MoveData = pair.second.data.toJsonString().fromJsonString()
                    val pawn = pawns[pair.first]
                    if(pawn != null) {
                        if (pawn.direction != moveData.direction) {
                            pawn.direction = moveData.direction
                            pawn.steps = 2
                            setNearestTile(pawn)
                        }
                        pawn.tick(Ticker.FRAME_TIME)
                        lst.add(pawn)
                    }
                }
            }
            if (lst.isNotEmpty()) {
                log.info("last state of pawn ${lst.last()}")
                val replica = Data(lst, false)
                log.info(replica.toString())
                return Pair(Topic.REPLICA, replica)
            } else {
                return null
            }*/
        }
        return null
    }

    fun addMessage(session: WebSocketSession, msg: Message) {
        inputQueue.add(Pair(session, msg))
    }

    fun broadcastMessage(message: Message?) {
        if (message != null) {
            broker.broadcast(message.topic, message.data)
        }
    }

    fun broadcastInitState() {
        val replicas = mutableListOf<Tile>()
        mechanics.field.forEach { line ->
            line.forEach { if (it.isNotEmpty()) replicas.add(it.last() as Tile) } }
        broker.broadcast(Topic.REPLICA, Data(replicas, false))
    }


    companion object {
        private val log = logger()
    }
}