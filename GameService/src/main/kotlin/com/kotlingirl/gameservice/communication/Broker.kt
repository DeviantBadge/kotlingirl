package io.rybalkinsd.kotlinbootcamp.communication

import io.rybalkinsd.kotlinbootcamp.game.User
import io.rybalkinsd.kotlinbootcamp.util.JsonHelper
import io.rybalkinsd.kotlinbootcamp.util.logger
import io.rybalkinsd.kotlinbootcamp.util.toJson
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedQueue


class Broker(private val connectionPool: ConnectionPool) {


    fun receive(session: WebSocketSession, msg: String) {
        log.info("RECEIVED: $msg")
        val message: Message = JsonHelper.fromJson(msg)
        //TODO TASK2 implement message processing
    }

    fun send(player: User, topic: Topic, data: Any) {
        val message = Message(topic, data.toJson()).toJson()
        val session = connectionPool.getSession(player)
        connectionPool.send(session!!, message)
    }

    fun broadcast(topic: Topic, data: Any) {
        val message = Message(topic, data).toJson()
        connectionPool.broadcast(message)
    }

    companion object {
        private val log = logger()
    }
}
