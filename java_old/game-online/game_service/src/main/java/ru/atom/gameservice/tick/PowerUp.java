package ru.atom.gameservice.tick;

import java.util.Random;

public class PowerUp extends GameObject {

    private final PowerUpType type;

    private PowerUp(int x, int y, Field field) {
        super(x, y, field);
        type = PowerUpType.getRandomType();
    }

    public static PowerUp generateNewPowerUp(int x, int y, Field field) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(100);
        return (number>79) ? new PowerUp(x, y, field) : null;
    }

    public PowerUpType getType() {
        return type;
    }


    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d}, \"bonusType\": \"%s\", \"id\":%d, \"type\":\"Bonus\"}",
                x * Field.tile,
                y * Field.tile,
                type.toString(),
                id);
    }
}
