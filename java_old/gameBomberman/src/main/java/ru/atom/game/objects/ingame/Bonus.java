package ru.atom.game.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

public class Bonus extends GameObject {
    public static enum  BonusType {
        SPEED,
        BOMBS,
        RANGE
    }

    private final BonusType bonusType;
    private boolean pickedUp = false;

    Bonus(Integer id, Point point, BonusType bonusType, boolean blocking) {
        super(id, ObjectType.Bonus, point, new Point(0,0), Cell.CELL_SIZE_X, Cell.CELL_SIZE_Y, blocking);
        this.bonusType = bonusType;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    @JsonIgnore
    public boolean isPickedUp() {
        return pickedUp;
    }

    public void pickUp() {
        pickedUp = true;
        destroy();
    }

    @Override
    @JsonIgnore
    protected String getEntrails() {
        return super.getEntrails() + ",bonusType" + bonusType;
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return true;
    }
}
