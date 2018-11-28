package ru.atom.game.objects.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.enums.ObjectType;
import ru.atom.game.objects.base.util.Frame;
import ru.atom.game.objects.base.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Cell {
    private static final Logger log = LoggerFactory.getLogger(Cell.class);
    public static final double CELL_SIZE_X = 32;
    public static final double CELL_SIZE_Y = 32;
    private final List<GameObject> objects = new ArrayList<>();
    private Point point;
    private boolean changed = false;


    public Cell(Point point) {
        this.point = point;
    }

    public void addObject(GameObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
        } else {
            log.warn("You trying to add existing object to cell, object type " + object.getType());
        }
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
    }

    public void removeObject(int object) {
        objects.remove(object);
    }

    public GameObject get(int i) {
        return objects.get(i);
    }

    public int size() {
        return objects.size();
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Point getPosition() {
        return point;
    }

    public boolean contains(Frame objectPoint) {
        Point center = objectPoint.getCenter();

        return  (center.getX() < point.getX() + CELL_SIZE_X) &&
                (center.getX() >= point.getX()) &&
                (center.getY() < point.getY() + CELL_SIZE_Y) &&
                (center.getY() >= point.getY());
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    public void deleteDestroyed() {
        objects.removeIf(GameObject::isDestroyed);
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public void deleteIf(Predicate<? super GameObject> isDeleted) {
        objects.removeIf(isDeleted);
    }
}
