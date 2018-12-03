package io.rybalkinsd.kotlinbootcamp.socket

import io.rybalkinsd.kotlinbootcamp.game.Game
import io.rybalkinsd.kotlinbootcamp.game.GameRepository
import io.rybalkinsd.kotlinbootcamp.game.User
import io.rybalkinsd.kotlinbootcamp.util.IntIdGen
import io.rybalkinsd.kotlinbootcamp.util.logger
import io.rybalkinsd.kotlinbootcamp.util.queryToMap
import org.springframework.beans.factory.annotation.Autowired
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

    val idGen = IntIdGen()

    @Volatile
    var sessions2games = ConcurrentHashMap<WebSocketSession, Game>()

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        logger().info("haha, session established, or not")
        logger().info("Query string was ${session.uri?.query}")
        val uriParams = session.uri?.query?.queryToMap() ?: mapOf()
        logger().info("Logged user got such params - $uriParams")
        val gameId = uriParams["gameId"]?.toInt() ?: 0
        val userId = idGen.getId()
        val game = gameRepository.getGame(gameId) ?: Game(0)
        synchronized(game){
            game.addUser(session, User(userId, uriParams["name"] ?: ""))
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
}