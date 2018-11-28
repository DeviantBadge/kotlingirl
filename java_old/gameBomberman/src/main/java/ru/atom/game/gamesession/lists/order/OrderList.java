package ru.atom.game.gamesession.lists.order;

import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.socket.message.request.IncomingMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderList {
    private final CopyOnWriteArrayList<OrderInfo> orders;

    public OrderList() {
        orders = new CopyOnWriteArrayList<>();
    }

    public void newOrder(int playerNum, IncomingTopic topic) {
        orders.get(playerNum).newMessage(topic);
    }

    public void newOrder(int playerNum, IncomingMessage message) {
        orders.get(playerNum).newMessage(message);
    }

    public List<Order> getOrders() {
        ArrayList<Order> readyOrders = new ArrayList<>();
        Order order;
        for (int i = 0; i < orders.size(); i ++) {
            order = orders.get(i).buildOrder(i);
            if(order != null)
                readyOrders.add(order);
        }
        return readyOrders;
    }

    public void clear() {
        orders.forEach(OrderInfo::clear);
    }

    public void newPlayer() {
        orders.add(new OrderInfo());
    }

    public void removePlayer(int playerNum) {
        orders.remove(playerNum);
    }
}