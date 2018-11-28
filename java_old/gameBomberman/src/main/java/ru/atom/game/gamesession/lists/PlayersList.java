package ru.atom.game.gamesession.lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayersList {
    private static final Logger log = LoggerFactory.getLogger(PlayersList.class);

    private List<OnlinePlayer> players;
    private int maxAmount;

    public PlayersList(int maxPlayerAmount) {
        players = new CopyOnWriteArrayList<>();
        this.maxAmount = maxPlayerAmount;
    }

    public void sendAll(String data) {
        players.forEach(onlinePlayer -> {
            sendTo(onlinePlayer, data);
        });
    }

    // todo move it into player
    public void sendTo(OnlinePlayer player, String data) {
        try {
            WebSocketSession socketSession = player.getSocket();
            if (socketSession != null && socketSession.isOpen())
                socketSession.sendMessage(new TextMessage(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendTo(int playerNum, String data) {
        try {
            WebSocketSession socketSession = players.get(playerNum).getSocket();
            if (socketSession != null && socketSession.isOpen())
                socketSession.sendMessage(new TextMessage(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OnlinePlayer getPlayer(int num) {
        return players.get(num);
    }

    public int playerNum(OnlinePlayer player) {
        return players.indexOf(player);
    }

    public int size() {
        return players.size();
    }

    public boolean contains(OnlinePlayer player) {
        return players.contains(player);
    }

    public void addPlayer(OnlinePlayer player) {
        if(players.size() < maxAmount)
            players.add(player);
    }

    public void removePlayer(int playerNum) {
        players.remove(playerNum);
    }
}
