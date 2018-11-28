package ru.atom.game.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.game.enums.Direction;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.MovingObject;
import ru.atom.game.objects.base.util.CellsSpace;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

import java.util.ArrayList;

public class Pawn extends MovingObject {
    private boolean alive = true;

    // BONUSES
    private int blowRange;
    private int maxBombAmount;
    private int speedBonus;

    // BOMBS
    private int bombCount = 0;

    Pawn(Integer id, Point point, Point colliderShift, Double colSizeX, Double colSizeY, boolean blocking,
         int blowRange, int maxBombAmount, int speedBonus) {
        super(id, ObjectType.Pawn, point, colliderShift, colSizeX, colSizeY, blocking);
        this.blowRange = blowRange;
        this.maxBombAmount = maxBombAmount;
        this.speedBonus = speedBonus;
    }

    //setters
    public void die() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void incMaxBombAmount() {
        maxBombAmount++;
    }

    public void incSpeedBonus() {
        speedBonus++;
    }

    public void incBlowRange() {
        blowRange++;
    }

    public void incBombCount() {
        bombCount++;
    }

    public void decBombCount() {
        bombCount--;
    }

    @JsonIgnore
    public int getBlowRange() {
        return blowRange;
    }

    @JsonIgnore
    public int getMaxBombAmount() {
        return maxBombAmount;
    }

    @JsonIgnore
    public int getSpeedBonus() {
        return speedBonus;
    }

    @JsonIgnore
    public int getBombCount() {
        return bombCount;
    }

    @Override
    @JsonIgnore
    protected String getEntrails() {
        return super.getEntrails();
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return true;
    }

    public void riseAgain() {
        alive = true;
    }


}
