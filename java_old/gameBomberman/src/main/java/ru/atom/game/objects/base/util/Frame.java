package ru.atom.game.objects.base.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

public class Frame {
    private Point position;
    private Double sizeX;
    private Double sizeY;

    public Frame(Frame frame) {
        position = new Point(frame.getX(), frame.getY());
        this.sizeX = frame.getSizeX();
        this.sizeY = frame.getSizeY();
    }

    public Frame(Double x, Double y, Double sizeX, Double sizeY) {
        position = new Point(x, y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Frame(@NotNull Point point, Double sizeX, Double sizeY) {
        position = point;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Frame(@NotNull Point point) {
        position = point;
        sizeX = 0d;
        sizeY = 0d;
    }

    public Frame(Point bottomLeftPoint, Point topRightPoint) {
        position = bottomLeftPoint;
        sizeX = topRightPoint.getX() - bottomLeftPoint.getX();
        sizeY = topRightPoint.getY() - bottomLeftPoint.getY();
    }


    @JsonIgnore
    public Point getCenter() {
        return new Point(
                getX() + getSizeX() / 2,
                getY() + getSizeY() / 2
        );
    }

    @JsonIgnore
    public Point getBottomLeftPoint() {
        return new Point(getX(), getY());
    }

    @JsonIgnore
    public Point getTopLeftPoint() {
        return new Point(getX(), getY() + getSizeY());
    }

    @JsonIgnore
    public Point getTopRightPoint() {
        return new Point(getX() + getSizeX(), getY() + getSizeY());
    }

    @JsonIgnore
    public Point getBottomRightPoint() {
        return new Point(getX() + getSizeX(), getY());
    }

    @JsonIgnore
    public Double getLeftBotX() {
        return getX();
    }

    @JsonIgnore
    public Double getLeftBotY() {
        return getY();
    }

    @JsonIgnore
    public Double getRightTopX() {
        return getX() + getSizeX();
    }

    @JsonIgnore
    public Double getRightTopY() {
        return getY() + getSizeY();
    }

    @JsonIgnore
    public boolean isInFrame(Point point) {
        return point.getX() >= getX()
                && point.getY() >= getY()
                && point.getX() <= (getX() + getSizeX())
                && point.getY() <= (getY() + getSizeY());
    }

    @JsonIgnore
    public Integer getIntX() {
        return position.getIntX();
    }

    @JsonIgnore
    public Integer getIntY() {
        return position.getIntY();
    }

    @JsonIgnore
    public Double getX() {
        return position.getX();
    }

    @JsonIgnore
    public Double getY() {
        return position.getY();
    }

    @JsonIgnore
    public Double getSizeX() {
        return sizeX;
    }

    @JsonIgnore
    public Double getSizeY() {
        return sizeY;
    }

    public Point getPosition() {
        return position;
    }

    public void setSizeX(Double sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(Double sizeY) {
        this.sizeY = sizeY;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "{position:" + position + ", sizeX:" + sizeX + ", sizeY:" + sizeY + "}";
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Point) {
            Point pos = (Point) anObject;
            return this.getX().equals(pos.getX())
                    && this.getY().equals(pos.getY())
                    && this.getSizeX().equals(0d)
                    && this.getSizeY().equals(0d);
        }
        if (anObject instanceof Frame) {
            Frame pos = (Frame) anObject;
            return this.getX().equals(pos.getX())
                    && this.getY().equals(pos.getY())
                    && this.getSizeX().equals(pos.getSizeX())
                    && this.getSizeY().equals(pos.getSizeY());
        }
        return false;
    }
}
