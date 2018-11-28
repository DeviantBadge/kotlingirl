package io.rybalkinsd.kotlinbootcamp.socket

import io.rybalkinsd.kotlinbootcamp.util.getUriQuery
import io.rybalkinsd.kotlinbootcamp.util.logger
import io.rybalkinsd.kotlinbootcamp.util.queryToMap
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
class SockEventHandler : TextWebSocketHandler() {

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        logger().info("haha, session established, or not")
        logger().info("Query string was ${session.uri?.query}")
        val uriParams = session.uri?.query?.queryToMap() ?: mapOf()
        logger().info("Logged user got such params - $uriParams")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger().info("Got message - ${message.payload}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        logger().info("Socket Closed: [" + closeStatus.code + "] " + closeStatus.reason + "; socket id [" + session.id + "]")
        super.afterConnectionClosed(session, closeStatus)
    }
}