package ru.atom.gameservice.message;

public class MoveMessage extends Message {

    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public MoveMessage(Topic topic, Direction direction, String name) {
        super(topic, name);
        this.direction = direction;
    }



}
