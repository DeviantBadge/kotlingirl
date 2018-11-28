package ru.atom.game.repos;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.session.OnlineSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    private static final int PARALLELISM_LEVEL = 4;

    private final ConcurrentHashMap<WebSocketSession, OnlinePlayer> connectedPlayers;
    private final ConcurrentHashMap<String /*Player Name*/, OnlinePlayer> notConnectedPlayers;
    // todo we have to remember players, that disconnected in past, for some time and if they reconnected, put them back to match (mb we will ask them in front, want they reconnect or not)

    public ConnectionPool() {
        connectedPlayers = new ConcurrentHashMap<>(256);
        notConnectedPlayers = new ConcurrentHashMap<>(128);
    }

    //***************************
    // CONNECTED PLAYERS FUNC
    //***************************

    public void send(@NotNull WebSocketSession session, @NotNull String msg) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (IOException ignored) {
            }
        }
    }

    public void broadcast(@NotNull String msg) {
        connectedPlayers.forEachKey(PARALLELISM_LEVEL, session -> send(session, msg));
    }

    public void shutdown() {
        connectedPlayers.forEachKey(PARALLELISM_LEVEL, session -> {
            if (session.isOpen()) {
                try {
                    session.close();
                } catch (IOException ignored) {
                }
            }
        });
    }

    public OnlinePlayer getPlayer(WebSocketSession session) {
        return connectedPlayers.get(session);
    }

    public WebSocketSession getSession(OnlinePlayer player) {
        return connectedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(null);
    }

    public WebSocketSession getSession(String playerName) {
        return connectedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(playerName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(null);
    }

    public void remove(WebSocketSession session) {
        connectedPlayers.remove(session);
    }

    public void remove(OnlinePlayer player) {
        if (player.getSocket() == null)
            notConnectedPlayers.remove(player.getName());
        else
            connectedPlayers.remove(player.getSocket());
    }

    public void unlink(OnlinePlayer player, GameSession game) {
        game.onPlayerDisconnect(player);
        remove(player.getSocket());
        WebSocketSession socket = player.getSocket();
        if (socket != null && socket.isOpen()) {
            try {
                socket.close(CloseStatus.GOING_AWAY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void link(OnlinePlayer player, GameSession gameSession) {
        if (!gameSession.onPlayerConnect(player))
            return;
        player.linkWithSession(gameSession);
    }

    //***************************
    // NOT READY PLAYERS FUNC
    //***************************

    public void addNewPlayer(OnlinePlayer player) {
        if (notConnectedPlayers.putIfAbsent(player.getName(), player) != null) {
            log.warn("Recreation of player with name " + player.getName());
        }
    }

    public OnlinePlayer connected(OnlinePlayer player, WebSocketSession session) {
        if (!notConnectedPlayers.remove(player.getName(), player))
            log.warn("Unknown player connecting");

        if (connectedPlayers.putIfAbsent(session, player) != null) {
            log.warn("Reconnecting player to socket, player name " + player.getName());
        }
        return player;
    }

    public OnlinePlayer connected(String name, WebSocketSession session) {
        OnlinePlayer player = notConnectedPlayers.remove(name);
        if (player == null) {
            log.warn("Unknown player connecting");
            return null;
        }

        player.setSocket(session);
        if (connectedPlayers.putIfAbsent(session, player) != null) {
            log.warn("Reconnecting player to socket, player name " + player.getName());
        }
        return player;
    }
}
