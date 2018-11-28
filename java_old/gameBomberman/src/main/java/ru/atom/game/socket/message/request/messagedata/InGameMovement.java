package ru.atom.game.socket.message.request.messagedata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.game.enums.Direction;

public class InGameMovement {
    private final Direction direction;
    private final String data;


    @JsonCreator
    public InGameMovement(
            @JsonProperty("direction") Direction direction,
            @JsonProperty("data") JsonNode data)
            throws IllegalArgumentException {
        this.direction = direction;
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "{direction:" + direction + "data:" + data + "}";
    }
}
