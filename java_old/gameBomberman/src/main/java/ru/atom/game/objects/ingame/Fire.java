package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

public class Fire extends GameObject {
    Fire(Integer id, Point point, boolean blocking) {
        super(id, ObjectType.Fire, point, new Point(0,0), Cell.CELL_SIZE_X, Cell.CELL_SIZE_Y, blocking);
    }

}
