package ru.atom.game.gamesession.lists;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.session.OnlineSession;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayer {
    private static final Logger log = LoggerFactory.getLogger(OnlinePlayer.class);
    private WebSocketSession socket;
    private final String name;
    private GameSession game;
    // private final List<OnlineSession> linkedSessions;

    public OnlinePlayer(String name, WebSocketSession socket) {
        this.socket = socket;
        this.name = name;
        game = null;
        // linkedSessions = new ArrayList<>();
    }

    public OnlinePlayer(String name) {
        this(name, null);
    }

    public void setSocket(WebSocketSession socket) {
        if (this.socket != null && socket != null) {
            if(this.socket.equals(socket))
                log.warn("Smbdy want take place of existing player");
            else
                log.warn("You are trying to connect same socket to the player");
        }
        else
            this.socket = socket;
    }

    public WebSocketSession getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public GameSession getGame() {
        return game;
    }

    public synchronized void linkWithSession(GameSession session) {
        if (game != null)
            log.warn("Already linked");
        else
            game = session;
    }

    public synchronized void unlinkWithSession(GameSession session) {
        if(game != session)
            log.warn("Does not in this session");
        else
            game = null;
    }

    /*
    public List<OnlineSession> getLinkedSessions() {
        return linkedSessions;
    }

    public synchronized void linkWithSession(OnlineSession session) {
        linkedSessions.add(session);
    }

    public synchronized void unlinkWithSession(OnlineSession session) {
        linkedSessions.remove(session);
    }
    */

}
