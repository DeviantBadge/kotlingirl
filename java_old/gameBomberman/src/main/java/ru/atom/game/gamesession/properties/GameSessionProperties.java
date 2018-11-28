package ru.atom.game.gamesession.properties;

import ru.atom.game.enums.FieldType;

public class GameSessionProperties extends GameObjectProperties {

    GameSessionProperties(GameSessionPropertiesCreator creator) {
        super(creator);
        maxPlayerAmount = creator.getMaxPlayerAmount();
        warmUpFieldType = creator.getWarmUpFieldType();
        gameFieldType = creator.getGameFieldType();
        fieldSizeX = creator.getFieldSizeX();
        fieldSizeY = creator.getFieldSizeY();

        blowStopsOnWall = creator.isBlowStopsOnWall();
        bombBlowAsOne = creator.isBombBlowAsOne();
        additiveBombRadius = creator.isAdditiveBombRadius();
        ranked = creator.isRanked();

        speedBonusCoef = creator.getSpeedBonusCoef();
        movingSpeedX = creator.getMovingSpeedX();
        movingSpeedY = creator.getMovingSpeedY();

        borderWidth = creator.getBorderWidth();
    }

    // ********************************
    // Session
    // ********************************
    private final int maxPlayerAmount;
    private final FieldType warmUpFieldType;
    private final FieldType gameFieldType;

    private final int fieldSizeX;
    private final int fieldSizeY;
    private final boolean ranked;

    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }

    public FieldType getWarmUpFieldType() {
        return warmUpFieldType;
    }

    public FieldType getGameFieldType() {
        return gameFieldType;
    }

    public int getFieldSizeX() {
        return fieldSizeX;
    }

    public int getFieldSizeY() {
        return fieldSizeY;
    }

    public boolean isRanked() {
        return ranked;
    }

    // ********************************
    // Bombs
    // ********************************
    private final boolean blowStopsOnWall;

    // not implemented
    private final boolean bombBlowAsOne;
    private final boolean additiveBombRadius;

    public boolean isBlowStopsOnWall() {
        return blowStopsOnWall;
    }

    public boolean isBombBlowAsOne() {
        return bombBlowAsOne;
    }

    public boolean isAdditiveBombRadius() {
        return additiveBombRadius;
    }


    // ********************************
    // Pawn
    // ********************************
    private final double speedBonusCoef;
    private final double movingSpeedX;
    private final double movingSpeedY;

    public double getSpeedBonusCoef() {
        return speedBonusCoef;
    }

    public double getMovingSpeedX() {
        return movingSpeedX;
    }

    public double getMovingSpeedY() {
        return movingSpeedY;
    }


    // ********************************
    // Field
    // ********************************
    private final int borderWidth;

    public int getBorderWidth() {
        return borderWidth;
    }
}
