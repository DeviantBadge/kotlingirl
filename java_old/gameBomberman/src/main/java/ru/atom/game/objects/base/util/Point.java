package ru.atom.game.objects.base.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;

public class Point {
    private final Double x;
    private final Double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(@NotNull Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    @JsonGetter("x")
    public Integer getIntX() {
        return x.intValue();
    }

    @JsonGetter("y")
    public Integer getIntY() {
        return y.intValue();
    }

    @JsonIgnore
    public Double getX() {
        return x;
    }

    @JsonIgnore
    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{x:" + x + "y:" + y + "}";
    }

    @Override
    public boolean equals(Object anObject) {
        if(anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Point) {
            Point pos = (Point) anObject;
            return pos.x.equals(this.x) && pos.y.equals(this.y);
        }
        return false;
    }
}
