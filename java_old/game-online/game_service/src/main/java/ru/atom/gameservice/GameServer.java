package ru.atom.gameservice;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class GameServer {
    private AtomicLong idGenerator;
    private ConcurrentMap<String, GameSession> currentGames;

    public long getNextId(){
        return idGenerator.getAndIncrement();
    }

    public GameServer() {
        this.idGenerator = new AtomicLong(1);
        this.currentGames = new ConcurrentHashMap<>();
    }

    public GameSession getGameSessionByName(String name) {
        return currentGames.get(name);
    }

    public GameSession getGameSessionByPlayer(String name) {
        return currentGames.entrySet().stream()
                .filter(entry -> entry.getValue().getPlayers().contains(name))
                .map(Map.Entry::getValue).findFirst().orElse(null);
    }


    public void addGameSession(String name, GameSession gameSession) {
        currentGames.put(name,gameSession);
    }
}
