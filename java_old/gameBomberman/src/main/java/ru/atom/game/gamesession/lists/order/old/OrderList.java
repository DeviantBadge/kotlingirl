package ru.atom.game.gamesession.lists.order.old;

import ru.atom.game.enums.Direction;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;
import ru.atom.game.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderList {
    private final ConcurrentLinkedQueue<Order> orders;

    public OrderList() {
        orders = new ConcurrentLinkedQueue<>();
    }

    public void newOrder(int playerNum, IncomingTopic topic) {
        orders.add(new Order(playerNum, topic));
    }

    public void newOrder(int playerNum, IncomingMessage message) {
        if(message.getTopic() == IncomingTopic.MOVE) {
            Direction direction = JsonHelper.fromJson(message.getData(), InGameMovement.class).getDirection();
            orders.add(new Order(playerNum, direction));
        } else {
            orders.add(new Order(playerNum, message.getTopic()));
        }
    }

    public List<Order> getOrders() {
        int size = orders.size();
        ArrayList<Order> readyOrders = new ArrayList<>(size);
        for (int i = 0; i < size; i ++) {
            readyOrders.add(orders.poll());
        }
        return readyOrders;
    }

    public void clear() {
        orders.clear();
    }

    public void newPlayer() {
    }

    public void removePlayer(int playerNum) {
    }
}