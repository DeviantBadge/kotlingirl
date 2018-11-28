package ru.atom.game.socket.message.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.game.enums.MessageType;

public class OutgoingMessage {
    private final MessageType topic;
    private final String data;


    public OutgoingMessage(MessageType topic, String data) {
        this.topic = topic;
        this.data = data;
    }

    @JsonCreator
    public OutgoingMessage(@JsonProperty("topic") MessageType topic, @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    public MessageType getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "{topic:" + topic + ",data:" + data + "}";
    }
}
