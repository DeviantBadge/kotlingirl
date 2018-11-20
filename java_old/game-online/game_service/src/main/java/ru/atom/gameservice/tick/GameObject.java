package ru.atom.gameservice.tick;


import java.util.Objects;

public abstract class GameObject {

    protected final int id;
    protected final Field field;

    protected int x;
    protected int y;

    protected boolean alive = true;

    public GameObject(int x, int y, Field field) {
        this.id = field.getNextId();
        this.x = x;
        this.y = y;
        this.field = field;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public abstract String toJson();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id &&
                Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
