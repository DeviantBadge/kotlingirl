package ru.atom.game.socket.message.request.messagedata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Name {
    private final String name;


    @JsonCreator
    public Name(
            @JsonProperty("name") String name)
            throws IllegalArgumentException {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{name:" + name + "}";
    }
}
