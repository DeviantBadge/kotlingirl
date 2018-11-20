package ru.atom.matchmaker;

public class Connection {

    private String name;

    private long gameId;

    private boolean available = true;


    public boolean isAvailable() {
        return available;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Connection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getGameId() {
        return gameId;
    }

    public Connection setGameId(long gameId) {
        this.gameId = gameId;
        return this;
    }
}
