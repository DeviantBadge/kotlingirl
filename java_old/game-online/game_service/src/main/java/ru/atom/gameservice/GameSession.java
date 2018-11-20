package ru.atom.gameservice;

import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import ru.atom.gameservice.message.Direction;
import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.MoveMessage;
import ru.atom.gameservice.network.ConnectionPool;
import ru.atom.gameservice.tick.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class GameSession implements Runnable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameSession.class);
    private static final ConnectionPool connectionPool = BeanUtil.getBean(ConnectionPool.class);

    private final int numberOfPlayers;
    private CopyOnWriteArrayList<String> players;
    private final ConcurrentLinkedQueue<Message> inputQueue = new ConcurrentLinkedQueue<>();

    private Field field;

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String name) {
        players.add(name);
    }

    public void addInput(Message message) {
        inputQueue.add(message);
    }



    public GameSession(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        players = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        while (players.size() != numberOfPlayers) {
            try {
                log.info("Waiting for players");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        field = new Field(27, 17, players);
        final int FPS = 60;
        final long FRAME_TIME = 1000 / FPS;
        //int i=0;
        while (!Thread.currentThread().isInterrupted()) {
            //i++;
            long started = System.currentTimeMillis();

            List<Message> messages = readInputQueue();
            for (Message message : messages) {
                switch (message.getTopic()) {
                    case PLANT_BOMB:
                        field.plantBomb(field.getPlayerByName(message.getName()));
                        break;
                    case MOVE:
                        Player p = field.getPlayerByName(message.getName());
                        if (p != null) {
                            Direction direction = ((MoveMessage) message).getDirection();
                            p.changeVelocity(direction.getX(), direction.getY());
                            //if (i%600 == 0 ) {
                            //    log.info("direction x - {}, y - {}", direction.getX(),direction.getY());
                            //}
                            p.setDirection(direction);
                        }
                        break;

                }
            }
            field.gameLogic(FRAME_TIME);

            long elapsed = System.currentTimeMillis() - started;

            if (elapsed < FRAME_TIME) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }

            String replica = field.getReplica();
            for (String name : players) {
                try {
                    connectionPool.getSession(name).sendMessage(new TextMessage(replica));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private List<Message> readInputQueue() {
        List<Message> out;
        synchronized (inputQueue) {  // Сделал на всякий случай , если здесь не нужна синхронизация , то удали)
            out = new ArrayList<>(inputQueue);
            inputQueue.clear();
        }
        return out;
    }
}
