package ru.atom.game.gamesession.lists.order.notimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.enums.Direction;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;
import ru.atom.game.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// mb we have way to make it better
class OrderInfo {
    private static final Logger log = LoggerFactory.getLogger(OrderInfo.class);
    private static final int CONNECT_PRIORITY = 0;
    private static final int READY_PRIORITY = 1;
    private static final int PLANT_BOMB_PRIORITY = 2;
    private static final int JUMP_PRIORITY = 3;
    private static final int MOVE_PRIORITY = 4;

    // order
    private ArrayList<AtomicBoolean> messagesArrived;
    // order as in Direction enum
    private ArrayList<AtomicInteger> directions;

    OrderInfo() {
        int size;
        size = IncomingTopic.values().length;
        messagesArrived = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            messagesArrived.add(new AtomicBoolean(false));

        directions = new ArrayList<>(size);
        size = Direction.values().length;
        for (int i = 0; i < size; i++)
            directions.add(new AtomicInteger(0));
    }


    void newMessage(IncomingTopic topic) {
        switch (topic) {
            case CONNECT:
            case READY:
            case PLANT_BOMB:
            case JUMP:
                messagesArrived.get(topic.getPriority()).set(true);
                break;

            case MOVE:
            default:
                log.warn("Unexpected message topic - " + topic);
        }
    }

    void newMessage(IncomingMessage message) {
        IncomingTopic topic = message.getTopic();
        switch (topic) {
            case CONNECT:
            case READY:
            case PLANT_BOMB:
            case JUMP:
                messagesArrived.get(topic.getPriority()).set(true);
                break;

            case MOVE:
                messagesArrived.get(MOVE_PRIORITY).set(true);
                Direction direction = JsonHelper.fromJson(message.getData(), InGameMovement.class).getDirection();
                switch (direction) {
                    case UP:
                    case RIGHT:
                    case DOWN:
                    case LEFT:
                        directions.get(direction.ordinal()).incrementAndGet();
                        break;
                    default:
                        log.warn("Unexpected direction - " + direction);
                }
                break;

            default:
                log.warn("Unexpected message topic - " + message.getTopic());
        }
    }

    Order buildOrder(int playerNum) {
        for (int i = CONNECT_PRIORITY; i < MOVE_PRIORITY; i++) {
            if (messagesArrived.get(i).get()) {
                messagesArrived.get(i).set(false);
                return new Order(playerNum, IncomingTopic.values()[i]);
            }
        }

        if (messagesArrived.get(MOVE_PRIORITY).get()) {
            Direction best = findBestDirection();
            if (best == null) {
                messagesArrived.get(MOVE_PRIORITY).set(false);
                return null;
            }
            else
                return new Order(playerNum, best);
        }
        return null;
    }

    private Direction findBestDirection() {
        int[] numbers = new int[4];
        for (int i = 0; i < directions.size(); i++) {
            numbers[i] = directions.get(i).get();
        }
        int i = -1;
        int max = 0;
        for (int j = 0; j < numbers.length; j++) {
            if (numbers[j] > max) {
                i = j;
                max = numbers[j];
            }
        }
        if(i < 0)
            return null;
        else {
            directions.get(i).set(0);
            return Direction.values()[i];
        }
    }

    public void clear() {
        messagesArrived.forEach(mes -> mes.set(false));
        directions.forEach(direction -> direction.set(0));
    }
}
