package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.util.Point;
import ru.atom.game.objects.base.Tile;
import ru.atom.game.enums.ObjectType;

public class Wall extends Tile {

    Wall(Integer id, Point point, boolean blocking) {
        super(id, ObjectType.Wall, point, false, blocking);
    }
}
