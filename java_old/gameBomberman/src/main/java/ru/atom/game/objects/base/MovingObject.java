package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.game.enums.Direction;
import ru.atom.game.enums.ObjectType;
import ru.atom.game.objects.base.util.Point;

public abstract class MovingObject extends GameObject {
    private Direction direction;
    private boolean moving = false;
    private boolean moved = false;

    public MovingObject(Integer id, ObjectType type, Point absolutePosition,
                      Point colliderShift, Double colliderSizeX, Double colliderSizeY, boolean blocking) {
        super(id, type, absolutePosition, colliderShift, colliderSizeX, colliderSizeY, blocking);
        setDirection(Direction.DOWN);
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    //Getters
    @JsonIgnore
    public Direction getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    private String getDirectionString() {
        if(moved) {
            moved = false;
            return direction.toString();
        }
        else
            return "";
    }

    //Also getters, but ignored
    @JsonIgnore
    public boolean isMoving() {
        return moving;
    }

    @JsonIgnore
    public boolean isMoved() {
        return !moving;
    }

    @Override
    @JsonIgnore
    protected String getEntrails() {
        return super.getEntrails();
    }
}
