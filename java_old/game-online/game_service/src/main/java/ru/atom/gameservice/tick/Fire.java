package ru.atom.gameservice.tick;
/*
Вроде как огонь есть во фронте
добавлю заготовочку
 */
public class Fire extends GameObject implements Tickable{
    long timeToLive = 250;
    public Fire(int x, int y, Field field) {
        super(x, y, field);
    }

    @Override
    public void tick(long elapsed) {
        if (timeToLive > 0) {
            // дамажит только людей когда они по нему ходят
            GameObject fieldAt = field.getAt(this.x, this.y);
            if ( fieldAt instanceof Player) {
                fieldAt.setAlive(false);
            }

            timeToLive-= elapsed;
        }else {
            this.setAlive(false);
        }
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d},\"id\":%d,\"type\":\"Fire\"}",
                x * Field.tile,
                y * Field.tile,
                id);
    }
}
