package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.ObjectsConsumer
import com.kotlingirl.gameservice.game.Mechanics
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedQueue


/** Receives messages and makes replica to send it */
class MessageManager(private val mechanics: Mechanics, private val broker: Broker) {

    private val inputQueue = ConcurrentLinkedQueue<Pair<WebSocketSession, Message> >()
    private val objectsConsumer = ObjectsConsumer(mechanics)
    private val queueReader = QueueReader(mechanics)

    fun makeReplica(elapsed: Long): Replica? {
        val objects = queueReader.read(inputQueue, elapsed)
        checkGameOver(elapsed)
        objectsConsumer.consume(objects, elapsed)
        return if (objects.isNotEmpty())
            Replica(Topic.REPLICA, Data(objects, false))
        else null
    }

    fun addMessage(session: WebSocketSession, msg: Message) {
        inputQueue.add(Pair(session, msg))
    }

    fun broadcastReplica(replica: Replica?) {
        if (replica != null) {
            broker.broadcast(replica.topic, replica.data)
        }
    }

    /** when first player connected */
    fun broadcastCurrentState() {
        broker.broadcast(Topic.REPLICA, Data(constructLabyrinthReplica(), false))
    }

    /** send current warm up labyrinth */
    fun sendCurrentState(session: WebSocketSession) {
        broker.send(session, Topic.REPLICA, Data(constructLabyrinthReplica(), false))
    }

    /** all this performs when ticker.needReInit is set true */
    fun initMainGame() {
        mechanics.isWarm = false
        sendCurrentStateToAllWarmUpPlayers()
        mechanics.init()
        /** after deleting last state and initing everything,
         *  we need to broadcast init state one else time */
        broadcastCurrentState()
    }

    private fun checkGameOver(elapsed: Long) {
        val closableSessions = mutableListOf<WebSocketSession>()
        //todo comment for warmUp
        checkWon(closableSessions)
        checkLost(closableSessions, elapsed)
        closableSessions.forEach { mechanics.pawns.remove(it); it.close()}
    }

    private fun checkWon(closableSessions: MutableList<WebSocketSession>) {
        if (!mechanics.isWarm && mechanics.pawns.size == 1) {
            mechanics.pawns.forEach { session, _ ->
                broker.send(session, Topic.GAME_OVER, "You Win!!!")
                closableSessions.add(session)
            }
        }
    }

    private fun checkLost(closableSessions: MutableList<WebSocketSession>, elapsed: Long) {
        mechanics.pawns.forEach { session, pawn ->
            if (!pawn.alive) {
                if (pawn.deadTime <= 0) {
                    broker.send(session, Topic.GAME_OVER, "Game Over")
                    closableSessions.add(session)
                } else {
                    pawn.tick(elapsed)
                }
            }
        }
    }

    private fun sendCurrentStateToAllWarmUpPlayers() {
        val count = mechanics.pawns.size - 1
        /** extract all players except of last added */
        val sessions = mechanics.pawns.filter { e -> e.value.count != count }.keys
        sessions.forEach { sendCurrentState(it) }
    }

    private fun constructLabyrinthReplica(): List<Any> {
        val replicas = mutableListOf<Any>()
        replicas.addAll(mechanics.tiles)
        replicas.addAll(mechanics.bonuses)
        return replicas.toList()
    }

    companion object {
        private val log = logger()
    }
}