package com.kotlingirl.gameservice.communication

import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import com.kotlingirl.serverconfiguration.util.extensions.toJsonString
import org.springframework.web.socket.WebSocketSession


class Broker(private val connectionPool: ConnectionPool) {


    fun receive(session: WebSocketSession, msg: String) {
//        log.info("RECEIVED: $msg")
        val replica: Replica = msg.fromJsonString()
        //TODO TASK2 implement message processing
    }

    fun send(session: WebSocketSession, topic: Topic, data: Any) {
        val message = Replica(topic, data).toJsonString()
        //val session = connectionPool.getSession(player)
        log.info("send: $message")
        connectionPool.send(session, message)
    }

    fun broadcast(topic: Topic, data: Any) {
        val message = Replica(topic, data).toJsonString()
        log.info(message)
        connectionPool.broadcast(message)
    }

    companion object {
        private val log = logger()
    }
}
