package ru.atom.game.enums;

public enum IncomingTopic {
    CONNECT,
    READY,
    PLANT_BOMB,
    JUMP,
    MOVE;
    public int getPriority() {
        return this.ordinal();
    }
}
