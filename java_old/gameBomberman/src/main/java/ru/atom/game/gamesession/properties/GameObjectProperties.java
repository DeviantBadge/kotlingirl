package ru.atom.game.gamesession.properties;

public class GameObjectProperties {
    GameObjectProperties(GameObjectPropertiesCreator creator) {
        bombBlowTimeMs = creator.getBombBlowTimeMs();

        bonusProbability = creator.getBonusProbability();
        speedProbability = creator.getSpeedProbability();
        bombsProbability = creator.getBombsProbability();

        speedOnStart = creator.getSpeedOnStart();
        bombsOnStart = creator.getBombsOnStart();
        rangeOnStart = creator.getRangeOnStart();
        colliderSize = creator.getColliderSize();
        playerStopsPlayer = creator.isPlayerStopsPlayer();
    }

    // ********************************
    // Bombs
    // ********************************
    private final long bombBlowTimeMs;

    public long getBombBlowTimeMs() {
        return bombBlowTimeMs;
    }


    // ********************************
    // Bonuses
    // ********************************
    private final double bonusProbability;
    private final double speedProbability;
    private final double bombsProbability;

    private double rangeProbability() {
        return 1 - speedProbability - bombsProbability;
    }

    public double getBonusProbability() {
        return bonusProbability;
    }

    public double getSpeedProbability() {
        return speedProbability;
    }

    public double getBombsProbability() {
        return bombsProbability;
    }

    public double getRangeProbability() {
        return rangeProbability();
    }


    // ********************************
    // Pawn
    // ********************************
    private final int speedOnStart;
    private final int bombsOnStart;
    private final int rangeOnStart;

    private final double colliderSize;
    private final boolean playerStopsPlayer;

    public int getSpeedOnStart() {
        return speedOnStart;
    }

    public int getBombsOnStart() {
        return bombsOnStart;
    }

    public int getRangeOnStart() {
        return rangeOnStart;
    }

    public double getColliderSize() {
        return colliderSize;
    }

    public boolean isPlayerStopsPlayer() {
        return playerStopsPlayer;
    }

    // ********************************
    // Wall
    // ********************************
    // mb destroyable


    // ********************************
    // Wood
    // ********************************
    // mb destroyable


    // ********************************
    // Util
    // ********************************
    protected static int intervalCheck(int value, int leftBorder, int rightBorder) {
        if (leftBorder > rightBorder)
            return value;
        if (value < leftBorder)
            return leftBorder;
        if (value > rightBorder)
            return rightBorder;
        return value;
    }

    protected static double intervalCheck(double value, double leftBorder, double rightBorder) {
        if (leftBorder > rightBorder)
            return value;
        if (value < leftBorder)
            return leftBorder;
        if (value > rightBorder)
            return rightBorder;
        return value;
    }
}
