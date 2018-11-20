package ru.atom.gameservice.message;

public class Message {
    private final Topic topic;

    private  final String name;

    public Message(Topic topic, String name) {
        this.topic = topic;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Topic getTopic() {
        return topic;
    }


}
