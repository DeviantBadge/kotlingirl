package ru.atom.game.socket.message.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.game.enums.IncomingTopic;

public class IncomingMessage {
    private final IncomingTopic topic;
    private final String gameId;
    private final String data;

    @JsonCreator
    public IncomingMessage(@JsonProperty("topic") IncomingTopic topic,
                           @JsonProperty("gameId") String gameId,
                           @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        this.gameId = gameId;
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    public IncomingTopic getTopic() {
        return topic;
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "{topic:" + topic + ",data:" + data + "}";
    }
}

