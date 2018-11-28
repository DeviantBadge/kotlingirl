package ru.atom.game.gamesession.session;

import org.slf4j.LoggerFactory;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.gamesession.lists.order.OrderList;
import ru.atom.game.gamesession.lists.PlayersList;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.objects.base.util.IdGen;
import ru.atom.game.gamesession.lists.order.Order;

import java.util.List;

public abstract class OnlineSession extends Ticker {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(OnlineSession.class);
    private static final IdGen idGen = new IdGen();
    private final Integer id;

    // just now if we want new features we have to add it manually
    private PlayersList playersList;
    private OrderList orderList;
    private int max;

    public OnlineSession(int maxPlayerAmount) {
        super(60);
        id = idGen.generateId();

        playersList = new PlayersList(maxPlayerAmount);
        orderList = new OrderList();
        max = maxPlayerAmount;
    }

    // **********************
    // SESSION functions
    // **********************

    public boolean onPlayerConnect(OnlinePlayer player) {
        int playerNum = playerNum(player);
        if (playerNum == -1) {
            log.warn("Connecting to lobby where we does not logged in");
            return false;
        }

        orderList.newOrder(playerNum, IncomingTopic.CONNECT);
        return true;
    }

    public void addOrder(OnlinePlayer player, IncomingMessage message) {
        if (!checkMessage(player, message))
            return;
        orderList.newOrder(playerNum(player), message);
    }

    @Override
    protected void act(long ms) {
        performOrders();
        performTick(ms);
    }

    private void performOrders() {
        List<Order> orders = orderList.getOrders();
        orders.forEach(this::carryOut);
    }

    protected abstract void carryOut(Order order);

    protected abstract void performTick(long ms);

    protected boolean checkMessage(OnlinePlayer player, IncomingMessage message) {
        int playerNum = playerNum(player);
        if (playerNum < 0) {
            log.warn("Player isn`t in this group. Group id - " + getId());
            return false;
        }

        return true;
    }

    @Override
    protected void onStop() {
        clearId();
    }

    public Integer getId() {
        return id;
    }

    private void clearId() {
        idGen.addDeletedId(getId());
    }

    // **********************
    // players list functions
    // **********************

    protected int playerNum(OnlinePlayer player) {
        return playersList.playerNum(player);
    }

    protected OnlinePlayer getPlayer(int num) {
        return playersList.getPlayer(num);
    }

    protected void sendAll(String data) {
        playersList.sendAll(data);
    }

    protected void sendTo(OnlinePlayer player, String data) {
        playersList.sendTo(player, data);
    }

    protected void sendTo(int playerNum, String data) {
        if (playerNum < 0 || playerNum >= playersList.size())
            return;
        playersList.sendTo(playerNum, data);
    }

    protected int playersAmount() {
        return playersList.size();
    }

    public void addPlayer(OnlinePlayer player) {
        playersList.addPlayer(player);
        orderList.newPlayer();
    }

    public boolean isFull() {
        return playersAmount() == max;
    }

    public abstract void onPlayerDisconnect(OnlinePlayer player);

    protected void removePlayer(int playerNum) {
        playersList.removePlayer(playerNum);
        orderList.removePlayer(playerNum);
    }


    // **********************
    // orders list functions
    // **********************

    protected void clearOrders() {
        orderList.clear();
    }
}
