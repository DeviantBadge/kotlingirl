package io.rybalkinsd.kotlinbootcamp.game

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.rybalkinsd.kotlinbootcamp.communication.Broker
import io.rybalkinsd.kotlinbootcamp.communication.ConnectionPool
import io.rybalkinsd.kotlinbootcamp.communication.Data
import io.rybalkinsd.kotlinbootcamp.communication.Message
import io.rybalkinsd.kotlinbootcamp.communication.Replica
import io.rybalkinsd.kotlinbootcamp.communication.Topic
import io.rybalkinsd.kotlinbootcamp.util.IntIdGen
import io.rybalkinsd.kotlinbootcamp.util.JsonHelper
import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicInteger

@Component
@Scope("prototype")
class Game(val count: Int) {
    companion object {
        private val log = logger()
        private val idGen = IntIdGen()
    }
    val id = 0//idGen.getId()
    val ticker = Ticker()
    var connectionPool = ConnectionPool()
    lateinit var broker: Broker
    val connections = ConcurrentLinkedDeque<WebSocketSession>()


    fun start() {
        log.info("HAHA, game number $id started, congratulations!")
        Thread {
            broker = Broker(connectionPool)
            ticker.broker = broker
            val user = connectionPool.getPlayer(connections.first())
            val pawn = Pawn(user!!.id, Position(20, 10), true, "")
            ticker.addPawn(connections.first(), pawn)
//        val replica = Replica("REPLICA", Data(listOf(pawn), false))
            val replica = Data(listOf(pawn), false)
            ticker.registerTickable(pawn)
//        broker.broadcast(TextMessage(jacksonObjectMapper().writeValueAsString(replica)))
            broker.broadcast(Topic.REPLICA, replica)
            ticker.gameLoop()
        }.start()
        log.info("New thread is started")
    }

    fun addUser(session: WebSocketSession, player: User) {
        connections.add(session)
        connectionPool.add(session, player)
    }

    fun receive(session: WebSocketSession, msg: String){
        log.info("RECEIVED: $msg")
        val message: Message = JsonHelper.fromJson(msg)
        ticker.input(session, message)
    }

    fun curCountOfPlayers(): Int = connectionPool.getCountOfPlayers()
}
