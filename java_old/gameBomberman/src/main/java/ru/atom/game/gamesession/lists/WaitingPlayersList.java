package ru.atom.game.gamesession.lists;

import ru.atom.game.objects.base.interfaces.Ticking;

import java.util.ArrayList;
import java.util.List;

public class WaitingPlayersList {

    private static class WaitingPlayer implements Ticking {
        private OnlinePlayer waiting;
        private long timeLeftMs;

        private WaitingPlayer (OnlinePlayer player, long timeLeftMs) {
            waiting = player;
            this.timeLeftMs = timeLeftMs;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            timeLeftMs = 0;
        }

        @Override
        public void tick(long ms) {
            timeLeftMs -= ms;
        }

        @Override
        public boolean isReady() {
            return timeLeftMs <= 0;
        }
    }

    private final long standardWaitingTime;
    private final List<WaitingPlayer> waiting;

    public WaitingPlayersList(long standardWaitingTime) {
        this.standardWaitingTime = standardWaitingTime;
        waiting = new ArrayList<>();
    }

    public void addPlayer(OnlinePlayer player) {
        waiting.add(new WaitingPlayer(player, standardWaitingTime));
    }

    public void addPlayer(OnlinePlayer player, long waitingTime) {
        waiting.add(new WaitingPlayer(player, waitingTime));
    }

    public List<OnlinePlayer> tick(long ms) {
        waiting.forEach(player -> player.tick(ms));
        ArrayList<OnlinePlayer> readyPlayers = new ArrayList<>();

        waiting.forEach(player -> {
            if(player.isReady())
                readyPlayers.add(player.waiting);
        });
        waiting.removeIf(Ticking::isReady);
        return readyPlayers;
    }

    public long waitingTime() {
        long time = 0;
        for (WaitingPlayer player : waiting) {
            if(player.timeLeftMs > time)
                time = player.timeLeftMs;
        }
        return time;
    }
}
