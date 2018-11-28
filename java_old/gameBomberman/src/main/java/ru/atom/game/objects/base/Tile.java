package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

public abstract class Tile extends GameObject {
    private final boolean destroyable;

    public Tile(Integer id, ObjectType type, Point absolutePosition, boolean destroyable, boolean blocking) {
        super(id, type, absolutePosition,new Point(0,0),  Cell.CELL_SIZE_X, Cell.CELL_SIZE_Y, blocking);
        this.destroyable = destroyable;
    }

    @JsonIgnore
    @Override
    public boolean isDestroyable() {
        return destroyable;
    }

    @JsonIgnore
    @Override
    protected String getEntrails() {
        return super.getEntrails() + ",destroyable:" + destroyable;
    }
}
