package ru.atom.gameservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public static Message parse(String jsonString, String name) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readValue(jsonString, JsonNode.class);
            Topic topic = Topic.valueOf(rootNode.get("topic").asText());
            switch (topic) {
                case MOVE:
                    Direction direction= Direction.valueOf(rootNode.get("data").get("direction").asText());
                    return  new MoveMessage(topic, direction, name);
                case PLANT_BOMB:
                    return new BombMessage(topic, name);
            }
            logger.info("JSON -> Message", jsonString);
        } catch (JsonGenerationException e) {
            logger.error("Wrong JSON");
        } catch (IOException e) {
            logger.error("Can't JSON -> Message");
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
}
