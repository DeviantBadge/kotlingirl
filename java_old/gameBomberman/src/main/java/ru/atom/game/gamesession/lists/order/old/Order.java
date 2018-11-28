package ru.atom.game.gamesession.lists.order.old;

import ru.atom.game.enums.Direction;
import ru.atom.game.enums.IncomingTopic;

// приказ
// Если буду реализовывать чат, то надо будет работу ордера пересмотреть, плюс то что он хранит столько всего мне не в кайф
public class Order {
    private int playerNum;
    private IncomingTopic incomingTopic;
    private Direction movementType = null;

    public Order(int playerNum, IncomingTopic topic) {
        this.playerNum = playerNum;
        incomingTopic = topic;
    }

    public Order(int playerNum, Direction direction) {
        this.playerNum = playerNum;
        incomingTopic = IncomingTopic.MOVE;
        movementType = direction;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public IncomingTopic getIncomingTopic() {
        return incomingTopic;
    }

    public Direction getMovementType() {
        return movementType;
    }
}
