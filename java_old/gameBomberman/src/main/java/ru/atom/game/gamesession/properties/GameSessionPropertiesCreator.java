package ru.atom.game.gamesession.properties;

import ru.atom.game.enums.FieldType;

public class GameSessionPropertiesCreator extends GameObjectPropertiesCreator {

    public GameSessionProperties createProperties() {
        return new GameSessionProperties(this);
    }

    // ********************************
    // Session
    // ********************************
    private int maxPlayerAmount = 2;
    private FieldType warmUpFieldType = FieldType.WARM_UP;
    private FieldType gameFieldType = FieldType.BONUS_VEIN;

    private int fieldSizeX = 27;
    private int fieldSizeY = 17;
    private boolean ranked = false;

    public GameSessionPropertiesCreator setMaxPlayerAmount(int maxPlayerAmount) {
        maxPlayerAmount = intervalCheck(maxPlayerAmount, 2, 4);
        this.maxPlayerAmount = maxPlayerAmount;
        return this;
    }

    public GameSessionPropertiesCreator setWarmUpFieldType(FieldType warmUpFieldType) {
        this.warmUpFieldType = warmUpFieldType;
        return this;
    }

    public GameSessionPropertiesCreator setGameFieldType(FieldType gameFieldType) {
        this.gameFieldType = gameFieldType;
        return this;
    }

    public GameSessionPropertiesCreator setFieldSizeX(int fieldSizeX) {
        fieldSizeX = intervalCheck(fieldSizeX, 10, 50);
        this.fieldSizeX = fieldSizeX;
        return this;
    }

    public GameSessionPropertiesCreator setFieldSizeY(int fieldSizeY) {
        fieldSizeY = intervalCheck(fieldSizeY, 10, 50);
        this.fieldSizeY = fieldSizeY;
        return this;
    }

    public GameSessionPropertiesCreator setRanked(boolean ranked) {
        this.ranked = ranked;
        return this;
    }

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
    private boolean blowStopsOnWall = true;

    // not implemented
    private boolean bombBlowAsOne = true;
    private boolean additiveBombRadius = false;

    public GameSessionPropertiesCreator setBlowStopsOnWall(boolean blowStopsOnWall) {
        this.blowStopsOnWall = blowStopsOnWall;
        return this;
    }

    public GameSessionPropertiesCreator setBombBlowAsOne(boolean bombBlowAsOne) {
        this.bombBlowAsOne = bombBlowAsOne;
        return this;
    }

    public GameSessionPropertiesCreator setAdditiveBombRadius(boolean additiveBombRadius) {
        this.additiveBombRadius = additiveBombRadius;
        return this;
    }

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
    private double speedBonusCoef = 1 / 3.0;
    private double movingSpeedX = 60;
    private double movingSpeedY = 60;

    public GameSessionPropertiesCreator setSpeedBonusCoef(double speedBonusCoef) {
        speedBonusCoef = intervalCheck(speedBonusCoef, 0, 1);
        this.speedBonusCoef = speedBonusCoef;
        return this;
    }

    public GameSessionPropertiesCreator setMovingSpeed(double movingSpeed) {
        movingSpeed = intervalCheck(movingSpeed, 10, 1000);
        this.movingSpeedX = movingSpeed;
        this.movingSpeedY = movingSpeed;
        return this;
    }

    public GameSessionPropertiesCreator setMovingSpeedX(double movingSpeedX) {
        movingSpeedX = intervalCheck(movingSpeedX, 10, 1000);
        this.movingSpeedX = movingSpeedX;
        return this;
    }

    public GameSessionPropertiesCreator setMovingSpeedY(double movingSpeedY) {
        movingSpeedY = intervalCheck(movingSpeedY, 10, 1000);
        this.movingSpeedY = movingSpeedY;
        return this;
    }

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
    // Game Field
    // ********************************
    private int borderWidth = 1;

    public GameSessionPropertiesCreator setBorderWidth(int borderWidth) {
        borderWidth = intervalCheck(borderWidth, 0, 3);
        this.borderWidth = borderWidth;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    // ************************
    // Super methods
    // ************************

    @Override
    public GameSessionPropertiesCreator random() {
        return ((GameSessionPropertiesCreator) super.random())
                .setMaxPlayerAmount((int) (Math.random() * 2) + 2)
                .setBlowStopsOnWall(((int) (Math.random() * 2)) > 0)
                .setSpeedBonusCoef(Math.random() / 2)
                .setMovingSpeed(Math.random() * 30 + 45)
                .setGameFieldType(FieldType.values()[(int) (Math.random() * FieldType.values().length)])
                .setWarmUpFieldType(FieldType.values()[(int) (Math.random() * FieldType.values().length)]);
                //.setFieldSizeX(27 + (int) (Math.random() * 5))
                //.setFieldSizeY(17 + (int) (Math.random() * 5));
    }

    @Override
    public GameSessionPropertiesCreator setBombBlowTimeMs(long bombBlowTimeMs) {
        return (GameSessionPropertiesCreator) super.setBombBlowTimeMs(bombBlowTimeMs);
    }

    @Override
    public GameSessionPropertiesCreator setBonusProbability(double bonusProbability) {
        return (GameSessionPropertiesCreator) super.setBonusProbability(bonusProbability);
    }

    @Override
    public GameSessionPropertiesCreator setProbabilities(double speedProbability, double bombsProbability, double rangeProbability) {
        return (GameSessionPropertiesCreator) super.setProbabilities(speedProbability, bombsProbability, rangeProbability);
    }

    @Override
    public GameSessionPropertiesCreator setSpeedOnStart(int speedOnStart) {
        return (GameSessionPropertiesCreator) super.setSpeedOnStart(speedOnStart);
    }

    @Override
    public GameSessionPropertiesCreator setBombsOnStart(int bombsOnStart) {
        return (GameSessionPropertiesCreator) super.setBombsOnStart(bombsOnStart);
    }

    @Override
    public GameSessionPropertiesCreator setRangeOnStart(int rangeOnStart) {
        return (GameSessionPropertiesCreator) super.setRangeOnStart(rangeOnStart);
    }

    @Override
    public GameSessionPropertiesCreator setColliderSize(double colliderSize) {
        return (GameSessionPropertiesCreator) super.setColliderSize(colliderSize);
    }

    @Override
    public GameSessionPropertiesCreator setPlayerStopsPlayer(boolean playerStopsPlayer) {
        return (GameSessionPropertiesCreator) super.setPlayerStopsPlayer(playerStopsPlayer);
    }
}
