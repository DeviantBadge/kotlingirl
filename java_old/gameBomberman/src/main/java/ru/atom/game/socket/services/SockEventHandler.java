package ru.atom.game.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.repos.ConnectionPool;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;
import ru.atom.game.socket.util.UriHelper;

import java.util.Map;

@Service
public class SockEventHandler extends TextWebSocketHandler {

    private static Logger log = LoggerFactory.getLogger(SockEventHandler.class);

    @Autowired
    private GameSessionRepo sessions;

    @Autowired
    private ConnectionPool connections;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("haha, session established, or not");
        Map<String, String> uriParams = UriHelper.getParamsFromUri(session.getUri().getQuery());
        if (!checkConnectionUriParams(uriParams)) {
            session.close();
            log.error("Wrong parameters are: " + uriParams);
            log.error("Error while connecting player, for more information watch logs");
            return;
        }
        connectPlayer(uriParams.get("gameId"), uriParams.get("name"), session);
        // TODO login here
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        IncomingMessage mes = JsonHelper.fromJson(message.getPayload(), IncomingMessage.class);
        OnlinePlayer player = connections.getPlayer(session);
        GameSession gameSession = sessions.getSession(mes.getGameId());
        if (!confirmPrivacy(player, mes)) {
            return;
        }
        gameSession.addOrder(player, mes);
    }

    private boolean confirmPrivacy(OnlinePlayer player, IncomingMessage mes) {
        GameSession session = sessions.getSession(mes.getGameId());
        if (session == null) {
            log.warn("Trying to send message to session with unknown id");
            return false;
        }

        if (player.getGame() != session) {
            log.warn("Trying to send message to session where player doesnt logged in");
            return false;
        }
        return true;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason() + "; socket id [" + session.getId() + "]");
        OnlinePlayer player = connections.getPlayer(session);
        if(player != null)
            connections.unlink(player, player.getGame());
        super.afterConnectionClosed(session, closeStatus);
    }

    private boolean checkConnectionUriParams(Map<String, String> uriParams) {
        boolean right = true;
        if (uriParams.get("name") == null) {
            log.warn("Sent connection request without \"name\" parameter");
            right = false;
        }
        if (uriParams.get("gameId") == null) {
            log.warn("Sent connection request without \"gameId\" parameter");
            right = false;
        }
        return right;
    }

    private void connectPlayer(String gameId, String name, WebSocketSession session) {
        OnlinePlayer player = connections.connected(name, session);
        if (player == null) {
            log.error("Unknown player name");
            return;
        }
        GameSession gameSession = sessions.getSession(gameId);
        connections.link(player, gameSession);
    }
}
