package ru.atom.gameservice.tick;

public class Wall extends GameObject{
    /*
     просто стена ничего не делает
     попробовал перегрузить поле alive как final и сделал true
     не уверен, что это сработает
     */
    private boolean alive = true;

    public Wall(int x, int y, Field field) {
        super(x, y, field);
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d}, \"id\":%d, \"type\":\"Wall\"}",
                x * Field.tile,
                y * Field.tile,
                id);
    }
}
