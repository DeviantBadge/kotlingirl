package ru.atom.gameservice.network;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ConnectionPool {
    private final ConcurrentMap<WebSocketSession, String> connections;

    public ConnectionPool() {
        this.connections = new ConcurrentHashMap<>();
    }

    public void addPlayer(WebSocketSession session, String name) {
        connections.put(session, name);
    }

    public void removePlayer(WebSocketSession session) {
        connections.remove(session);
    }

    public String getName(WebSocketSession session) {
        return connections.get(session);
    }

    //требуются уникальные имена игроков
    public WebSocketSession getSession(String player) {
        return connections.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
