package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.stereotype.Component


import com.kotlingirl.gameservice.communication.Broker
import com.kotlingirl.gameservice.communication.ConnectionPool
import com.kotlingirl.gameservice.communication.Message
import com.kotlingirl.gameservice.communication.MessageManager
import com.kotlingirl.gameservice.communication.MoveMessage
import com.kotlingirl.gameservice.communication.Topic
import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import org.springframework.web.socket.WebSocketSession


@Component
class Game(val count: Int) {

    private val connectionPool = ConnectionPool()
    private val mechanics = Mechanics()
    private val broker = Broker(connectionPool)
    private val messageManager = MessageManager(mechanics, broker)
    private val ticker = Ticker(messageManager)
    val id = idGen.getId()
    val users = mutableSetOf<User>()

    fun start() {
        log.info("HAHA, game number $id started, congratulations!")
        Thread {
            gameInit()
            ticker.gameLoop()
        }.start()
        log.info("New thread is started")
    }

    fun addUser(user: User) {
        users.add(user)
    }

    fun linkUser(session: WebSocketSession) {
        val user = users.first { !it.linked }.also { it.linked = true; it.webSocketSession = session }
        connectionPool.add(session, user)
        mechanics.createPawn(session, user)
    }

    fun linkUser(session: WebSocketSession, user: User) {
        connectionPool.add(session, user)
        mechanics.createPawn(session, user)
    }

    fun receive(session: WebSocketSession, msg: String) {
//        log.info("RECEIVED: $msg")
        val message: Message = msg.fromJsonString()
        if(message.topic == Topic.MOVE) {
            val move : MoveMessage = msg.fromJsonString()
            messageManager.addMessage(session, move)
        } else {
            messageManager.addMessage(session, message)
        }
    }

    /** when first player connected */
    private fun gameInit() {
        messageManager.broadcastCurrentState()
    }

    /** when middle players connected */
    fun warmInit(session: WebSocketSession) {
        messageManager.sendCurrentState(session)
    }

    /** when last player connected */
    fun mainInit() {
        ticker.needReInit = true
    }

    fun curCountOfPlayers(): Int = connectionPool.getCountOfPlayers()

    companion object {
        private val log = logger()
        private val idGen = IntIdGen()
    }
}
