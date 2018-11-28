package ru.atom.game.objects.base.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.interfaces.Colliding;

public class ColliderFrame extends Frame implements Colliding {
    // owner position
    @JsonIgnore
    private Point shift;

    public ColliderFrame(Point absolutePosition, Double sizeX, Double sizeY, Point shift) {
        super(absolutePosition, sizeX, sizeY);
        this.shift = shift;
    }

    public ColliderFrame(Frame collidingSpace) {
        super(collidingSpace);
        shift = new Point(0,0);
    }

    @Override
    public Double getX() {
        return super.getX() + shift.getX();
    }

    @Override
    public Double getY() {
        return super.getY() + shift.getY();
    }

    public void setSuperPosition(Point absolutePosition) {
        super.setPosition(new Point(
                absolutePosition.getX() - shift.getX(),
                absolutePosition.getY() - shift.getY())
        );
    }

    private boolean collide(ColliderFrame collider) {
        return !(getLeftBotX() > collider.getRightTopX()
                || getLeftBotY() > collider.getRightTopY()
                || getRightTopX() < collider.getLeftBotX()
                || getRightTopY() < collider.getLeftBotY());
    }

    @Override
    public boolean collide(Colliding colliding) {
        if(colliding instanceof ColliderFrame)
            return collide(((ColliderFrame) colliding));
        return false;
    }
}
