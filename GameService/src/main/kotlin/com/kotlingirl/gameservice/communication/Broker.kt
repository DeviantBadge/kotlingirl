package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.User
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import com.kotlingirl.serverconfiguration.util.extensions.toJsonString
import org.springframework.web.socket.WebSocketSession


class Broker(private val connectionPool: ConnectionPool) {


    fun receive(session: WebSocketSession, msg: String) {
        log.info("RECEIVED: $msg")
        val message: Message = msg.fromJsonString()
        //TODO TASK2 implement message processing
    }

    fun send(player: User, topic: Topic, data: Any) {
        val message = Message(topic, data.toJsonString()).toJsonString()
        val session = connectionPool.getSession(player)
        connectionPool.send(session!!, message)
    }

    fun broadcast(topic: Topic, data: Any) {
        val message = Message(topic, data).toJsonString()
        connectionPool.broadcast(message)
    }

    companion object {
        private val log = logger()
    }
}
