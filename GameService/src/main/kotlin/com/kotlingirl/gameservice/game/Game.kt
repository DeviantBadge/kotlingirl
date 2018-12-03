package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


import com.kotlingirl.gameservice.communication.Broker
import com.kotlingirl.gameservice.communication.ConnectionPool
import com.kotlingirl.gameservice.communication.Data
import com.kotlingirl.gameservice.communication.Message
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.gameservice.communication.Topic
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedDeque

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
            ticker.makeBG()
            val user = connectionPool.getPlayer(connections.first())
            val pawn = Pawn(user!!.id, Position(0, 32), true, "")
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
        val message: Message = msg.fromJsonString()
        ticker.input(session, message)
    }

    fun curCountOfPlayers(): Int = connectionPool.getCountOfPlayers()
}
