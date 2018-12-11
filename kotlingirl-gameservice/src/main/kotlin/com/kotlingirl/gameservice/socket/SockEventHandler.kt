package com.kotlingirl.gameservice.socket

import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.gameservice.game.Game
import com.kotlingirl.gameservice.game.GameRepository
import com.kotlingirl.serverconfiguration.util.IntIdGen
import org.springframework.beans.factory.annotation.Autowired
import com.kotlingirl.serverconfiguration.util.extensions.logger
import com.kotlingirl.serverconfiguration.util.extensions.queryToMap
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Service
class SockEventHandler : TextWebSocketHandler() {
    @Autowired
    lateinit var gameRepository: GameRepository

    @Volatile
    var sessions2games = ConcurrentHashMap<WebSocketSession, Game>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        logger().info("haha, session established, or not")
        logger().info("Query string was ${session.uri?.query}")
        val uriParams = session.uri?.query?.queryToMap() ?: mapOf()
        logger().info("Logged user got such params - $uriParams")
        val gameId = uriParams["gameId"]?.toInt() ?: 0
//        val userId = idGen.getId()
        val game = gameRepository.getGame(gameId) ?: Game(0)
        synchronized(game){
            game.linkUser(session, User(""))
            sessions2games[session] = game
            if (game.curCountOfPlayers() == game.count)
            game.start()
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger().info("Got message - ${message.payload}")
        sessions2games[session]?.receive(session, message.payload)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        logger().info("Socket Closed: [" + closeStatus.code + "] " + closeStatus.reason + "; socket id [" + session.id + "]")
        super.afterConnectionClosed(session, closeStatus)
    }

    companion object {
        val log = logger()
        val idGen = IntIdGen()
    }
}