package ru.atom.gameservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.gameservice.message.JsonParser;
import ru.atom.gameservice.network.ConnectionPool;

@Component
public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final ConnectionPool connectionPool;
    private final GameServer gameServer;

    @Autowired
    public EventHandler(ConnectionPool connectionPool, GameServer gameServer)
    {
        this.connectionPool = connectionPool;
        this.gameServer = gameServer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String name = session.getAttributes().get("name").toString();
        String gameId = session.getAttributes().get("gameId").toString();
        logger.info("{} connected to game {}", name, gameId);
        connectionPool.addPlayer(session, name);
        gameServer.getGameSessionByName(gameId).addPlayer(name);
        System.out.println("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage inputMessage) throws Exception {
        String name = connectionPool.getName(session);
        GameSession gameSession = gameServer.getGameSessionByPlayer(name);
        gameSession.addInput(JsonParser.parse(inputMessage.getPayload(), name));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }

}
