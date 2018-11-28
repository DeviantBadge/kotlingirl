package ru.atom.game.gamesession.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.atom.game.enums.Direction;
import ru.atom.game.enums.MessageType;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.gamesession.lists.WaitingPlayersList;
import ru.atom.game.gamesession.session.processors.BombProcessor;
import ru.atom.game.gamesession.session.processors.MovingProcessor;
import ru.atom.game.repos.ConnectionPool;
import ru.atom.game.socket.message.response.OutgoingMessage;
import ru.atom.game.socket.message.response.messagedata.Possess;
import ru.atom.game.socket.message.response.messagedata.Replica;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.ingame.Bonus;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.gamesession.lists.order.Order;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;

import java.util.List;


// TODO mb i can add some bots :)
public class GameSession extends OnlineSession {
    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    @Autowired
    private GameSessionRepo sessionRepo;

    @Autowired
    private ConnectionPool connections;

    private GameState gameState;

    // i wanna create game settings, for this i have to create some properties
    private GameSessionProperties properties;
    private ObjectCreator creator;

    private final MovingProcessor movingProcessor;
    private final BombProcessor bombProcessor;

    private final WaitingPlayersList waitingDeisconnect;

    // add objects to replica where we change them
    private Replica replica;
    // todo - idea, objects have hp, lasers and other mechanics

    //TODO mb add something like game type - deathMatch singleLife - or it could be made by properties but how ?

    // TODO when player disconnects, we have to remember his state and if he reconnects, we check if his sessions are active or not - make it by data bases

    // TODO after death, player have to see how he was killed, so, we have to disconnect him from server after small delay

    // TODO add chat

    public GameSession(GameSessionProperties properties) {
        super(properties.getMaxPlayerAmount());
        this.properties = properties;
        creator = new ObjectCreator(properties);
        gameState = new GameState(properties, creator);
        replica = new Replica();

        movingProcessor = new MovingProcessor(properties, gameState);
        bombProcessor = new BombProcessor(properties, gameState, creator, replica);
        waitingDeisconnect = new WaitingPlayersList(3000);
    }

    //**************************
    // SESSION LOGIC
    //**************************


    @Override
    protected void act(long ms) {
        super.act(ms);
        addAliveObjectsToReplica();
        sendReplica();
    }

    @Override
    protected void performTick(long ms) {
        movingProcessor.movePlayers(ms);
        bombProcessor.tickBomb(ms);
        servicesTick(ms);
        clearCells();
    }

    private void servicesTick(long ms) {
        disconnectWaitingPlayers(ms);
        if (gameState.isGameEnded()) {
            if(waitingDeisconnect.waitingTime() <= 0) {
                stop();
            }
        }
    }

    private void disconnectWaitingPlayers(long ms) {
        List<OnlinePlayer> players = waitingDeisconnect.tick(ms);
        players.forEach(player -> {
            String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU DIED"));
            sendTo(player, message);
            connections.unlink(player, this);
        });
    }

    // todo игрок дергается когда ставишь бомбу - исправить
    @Override
    protected void carryOut(Order order) {
        switch ((order.getIncomingTopic())) {
            case CONNECT:
                break;
            case READY:
                // sendTo(order.getPlayerNum(), getPossess());
                if (playersAmount() < properties.getMaxPlayerAmount()) {
                    sendReplicaTo(order.getPlayerNum(), JsonHelper.toJson(gameState.getFieldReplica()));
                } else {
                    clearOrders();
                    String rep = JsonHelper.toJson(gameState.getFieldReplica());
                    for (int i = 0; i < playersAmount(); i++)
                        if (i != order.getPlayerNum())
                            sendReplicaTo(i, rep);
                    gameState.recreate();
                    replica.addAllToReplica(gameState.getFieldReplica());
                }
                break;

            case JUMP:
                break;

            case MOVE:
                Pawn curPlayer = gameState.getPawns().get(order.getPlayerNum());
                if (curPlayer.isMoving())
                    break;
                switch (order.getMovementType()) {
                    case UP:
                        curPlayer.setDirection(Direction.UP);
                        break;
                    case RIGHT:
                        curPlayer.setDirection(Direction.RIGHT);
                        break;
                    case DOWN:
                        curPlayer.setDirection(Direction.DOWN);
                        break;
                    case LEFT:
                        curPlayer.setDirection(Direction.LEFT);
                        break;
                }
                curPlayer.setMoving(true);
                break;
            case PLANT_BOMB:
                bombProcessor.plantBombBy(gameState.getPawns().get(order.getPlayerNum()));
                break;
        }
    }

    @Override
    protected void stop() {
        super.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void addPlayer(OnlinePlayer player) {
        super.addPlayer(player);
        gameState.addPlayer();
    }

    // TODO надо четко определить фукционирование и области действия классов, один удаляет сокеты, другой не имеет права с ними работать
    // TODO третий наоборот все делает с сокетами а первым двум только позволяет произвести какие то действия (реакция на событие, не больше)
    // Все это осложниться тем, что мы будем дисконнектить после определенного события
    @Override
    public void onPlayerDisconnect(OnlinePlayer player) {
        int playerNum = playerNum(player);
        if (gameState.isWarmUp()) {
            gameState.getPawns().remove(playerNum);
            removePlayer(playerNum);
        } else {
            gameState.getPawns().get(playerNum).die();
            if (areAllDead()) {
                gameEnded();
            }
        }
    }

    private void gameEnded() {
        gameState.setGameEnded(true);
        int lastPlayer = gameState.getAliveNum();
        if (lastPlayer != -1) {
            String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU WON"));
            sendTo(lastPlayer, message);

            // todo я сейчас вижу проблему одну, когда игрок отключается от игры мы его будем отключать от сервера, и нужно будет откючить от других сессий, не будет ли с этим проблем?
            // TODO thats what i dislike, it doesnt belong to game process, we have to do it smwhere else
            // It will be solved when i will create rules to classes and their rights
            connections.unlink(getPlayer(lastPlayer), this);
            sessionRepo.onSessionEnd(this);
        }
    }

    //**************************
    // GAME LOGIC
    //**************************

    private void clearCells() {
        gameState.forEachCell(cell -> {
            if (cell.isChanged()) {
                List<GameObject> objects = cell.getObjects();
                int size = objects.size();
                for (int i = 0; i < size; i++) {
                    GameSession.this.onDestroy(objects.get(i));
                }
                cell.deleteIf(GameObject::isDeleted);
                cell.setChanged(false);
            }
        });
    }

    private void onDestroy(GameObject object) {
        if (!object.isDestroyed() || object.isDeleted())
            return;
        switch (object.getType()) {
            case Bonus:
                if (((Bonus) object).isPickedUp()) {
                    addObjectToReplica(object);
                    break;
                }
            case Wood:
                Cell cell = gameState.get(object.getPosition());
                addObjectToReplica(object);
                Bonus bonus = creator.createBonus(cell.getPosition(), true);
                if (bonus != null) {
                    cell.addObject(bonus);
                    addObjectToReplica(bonus);
                }
                break;

            case Pawn:
                Pawn pawn = (Pawn) object;
                if (!gameState.isWarmUp()) {
                    onPlayerDeath(pawn);
                    if (areAllDead()) {
                        gameEnded();
                    }
                }
                pawn.restore();
                break;

            case Bomb:
            case Fire:
            case Wall:
                break;
            default:
        }
        if (object.isDestroyed())
            object.delete();
    }

    private void onPlayerDeath(Pawn pawn) {
        pawn.die();
        waitingDeisconnect.addPlayer(getPlayer(gameState.playerNum(pawn)));
    }

    private boolean areAllDead() {
        return gameState.deadPawnsAmount() >= properties.getMaxPlayerAmount() - 1;
    }

    //********************************
    // REPLICA
    //********************************

    private void addAliveObjectsToReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(gameState.getPawns());
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    private void sendReplica() {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica.toStringAndClear()));
        sendAll(message);
    }

    private void sendReplicaTo(int playerNum, String replica) {
        replica = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica));
        sendTo(playerNum, replica);
    }

    private String getPossess() {
        Possess possess = new Possess(gameState.getSizeY(), gameState.getSizeX(), 0);
        return JsonHelper.toJson(new OutgoingMessage(MessageType.POSSESS, JsonHelper.toJson(possess)));
    }
}
