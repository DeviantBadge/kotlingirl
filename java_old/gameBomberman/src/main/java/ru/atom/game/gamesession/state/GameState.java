package ru.atom.game.gamesession.state;

import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.CellsSpace;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.objects.ingame.Bomb;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.gamesession.properties.GameSessionProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class GameState {

    private final List<Bomb> bombs;
    private final List<Pawn> pawns;
    private boolean warmUp;
    private boolean gameEnded = false;

    private ObjectCreator creator;
    private GameField gameField;
    private GameSessionProperties properties;

    public GameState(GameSessionProperties properties, ObjectCreator creator) {
        this.properties = properties;
        this.creator = creator;
        gameField = new GameField(creator, properties);
        bombs = new ArrayList<>();
        pawns = new CopyOnWriteArrayList<>();

        createWarmUpField();
    }

    private void createWarmUpField() {
        bombs.clear();
        gameField.createField(properties.getWarmUpFieldType());
        warmUp = true;
    }

    private void createGameField() {
        bombs.clear();
        gameField.createField(properties.getGameFieldType());
        warmUp = false;
    }

    public void recreate() {
        createGameField();
        resetPawns();
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    // todo im deleting wrong players and do not deleting them right too !!!!!!!!!!!!!!!!!!!!!!!
    // todo im deleting wrong players and do not deleting them right too !!!!!!!!!!!!!!!!!!!!!!!
    // todo im deleting wrong players and do not deleting them right too !!!!!!!!!!!!!!!!!!!!!!!
    // todo im deleting wrong players and do not deleting them right too !!!!!!!!!!!!!!!!!!!!!!!
    private void resetPawns() {
        int pawnAmount = pawns.size();
        getPawns().clear();
        for (int i = 0; i < pawnAmount; i++)
            addPlayer();
    }

    public void addPlayer() {
        Point playerPos = gameField.getRandomStartPos();
        Pawn player = creator.createPawn(playerPos);

        pawns.add(player);
        get(playerPos).addObject(player);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        Cell cell = get(bomb.getCenter());
        bomb.setPosition(cell.getPosition());
        cell.addObject(bomb);
    }

    public Point checkFieldBorders(Point newPoint) {
        double x = newPoint.getX(), y = newPoint.getY();
        if (newPoint.getX() < 0)
            x = 0;
        if (newPoint.getY() < 0)
            y = 0;
        if (newPoint.getX() + Cell.CELL_SIZE_X > getSizeX() * Cell.CELL_SIZE_X)
            x = getSizeX() * Cell.CELL_SIZE_X - Cell.CELL_SIZE_X;
        if (newPoint.getY() + Cell.CELL_SIZE_Y > getSizeY() * Cell.CELL_SIZE_Y)
            y = getSizeY() * Cell.CELL_SIZE_Y - Cell.CELL_SIZE_Y;
        return new Point(x, y);
    }

    public Cell get(int x, int y) {
        return gameField.get(x, y);
    }

    public Cell get(Point point) {
        return get((int) (point.getIntX() / Cell.CELL_SIZE_X),
                (int) (point.getIntY() / Cell.CELL_SIZE_Y));
    }

    public int getSizeX() {
        return gameField.getSizeX();
    }

    public int getSizeY() {
        return gameField.getSizeY();
    }

    public boolean isWarmUp() {
        return warmUp;
    }

    public int playerNum(Pawn pawn) {
        return pawns.indexOf(pawn);
    }

    public int getAliveNum() {
        for (int i = 0; i < pawns.size(); i++) {
            if (pawns.get(i).isAlive())
                return i;
        }
        return -1;
    }

    public int deadPawnsAmount() {
        int amount = 0;
        for (int i = 0; i < pawns.size(); i++) {
            if (!pawns.get(i).isAlive())
                amount++;
        }
        return amount;
    }
    public void forEachCell (Consumer<? super Cell> action) {
        gameField.forEach(action);
    }

    // sometimes player will be under field
    public List<Replicable> getFieldReplica() {
        return gameField.getFieldReplica();
    }

    public void removeObject(GameObject object) {
        get(object.getCenter()).removeObject(object);
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }
}
