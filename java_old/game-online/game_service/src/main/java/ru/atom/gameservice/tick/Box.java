package ru.atom.gameservice.tick;


public class Box extends GameObject {
    public Box(int x, int y, Field field) {
        super(x, y, field);
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d},\"id\":%d,\"type\":\"Wood\"}",
                x * Field.tile,
                y * Field.tile,
                id);
    }


}
