package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


import com.kotlingirl.gameservice.communication.Broker
import com.kotlingirl.gameservice.communication.ConnectionPool
import com.kotlingirl.gameservice.communication.Message
import com.kotlingirl.gameservice.communication.Replica
import com.kotlingirl.gameservice.communication.MessageManager
import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import org.springframework.web.socket.WebSocketSession

@Component
@Scope("prototype")
class Game(val count: Int) {
    companion object {
        private val log = logger()
        private val idGen = IntIdGen()
    }
    val id = 0//idGen.getId()
    val ticker = Ticker()
    val connectionPool = ConnectionPool()
    lateinit var broker: Broker
    var mechanics = Mechanics()
    lateinit var messageManager: MessageManager

    fun start() {
        log.info("HAHA, game number $id started, congratulations!")
        Thread {
            gameInit()
            ticker.gameLoop()
        }.start()
        log.info("New thread is started")
    }

    fun addUser(session: WebSocketSession, user: User) {
        connectionPool.add(session, user)
        mechanics.createPawn(session, user)
    }

    fun receive(session: WebSocketSession, msg: String){
//        log.info("RECEIVED: $msg")
        val message: Message = msg.fromJsonString()
        messageManager.addMessage(session, message)
    }

    private fun gameInit() {
        broker = Broker(connectionPool)
        messageManager = MessageManager(mechanics, broker)
        ticker.messageManager = messageManager
        messageManager.broadcastInitState()
    }

    fun curCountOfPlayers(): Int = connectionPool.getCountOfPlayers()
}
