package ru.atom.game.gamesession.session.processors;

import ru.atom.game.enums.Direction;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.MovingObject;
import ru.atom.game.objects.base.util.CellsSpace;
import ru.atom.game.objects.base.util.ColliderFrame;
import ru.atom.game.objects.base.util.Frame;
import ru.atom.game.objects.ingame.Pawn;

import java.util.List;

public class MovingProcessor {
    private static final double EPS = 0.001;

    private GameSessionProperties properties;
    private GameState gameState;
    private CollisionProcessor collisionProcessor;

    public MovingProcessor(GameSessionProperties properties, GameState gameState) {
        this.properties = properties;
        this.gameState = gameState;
        collisionProcessor = new CollisionProcessor(properties);
    }

    public void movePlayers(long ms) {
        Frame newPosition;
        ColliderFrame collidingSpace;
        CellsSpace passedThrough;
        for (Pawn pawn : gameState.getPawns()) {
            if (!pawn.isAlive() || !pawn.isMoving())
                continue;
            pawn.setMoved(true);
            pawn.setMoving(false);
            newPosition = move(pawn, ms);

            // можно сделать 1 большой блок, от начала до конца и смотреть с кем провзаимодействовал он
            // выч нач и кон позиции -> построение огромного collideFrame`a -> collide этого фрейма со всем что движется и нет)
            collidingSpace = new ColliderFrame(getCollidingSpace(pawn, newPosition));
            // calculate cells that we will went through
            passedThrough = new CellsSpace(collidingSpace);

            // calculate end point of his way, if he stopped when colliding with wall use onCollision
            newPosition = getResultPos(pawn, collidingSpace, newPosition, passedThrough);

            // calculate cells that we will went through 100%
            collidingSpace = new ColliderFrame(getCollidingSpace(pawn, newPosition));
            passedThrough = new CellsSpace(collidingSpace);

            // collide with that cells;
            collide(pawn, collidingSpace, passedThrough);

            // так же, после того как он провзаимодействовал, можно провернить, коллайдится с объектом наша последняя позиция, тип для того что бы вызвать onEscape
            // escape from this cells (для новых механик нажимных плит) - это нужно делать в функции collide

            leaveAndJoinCells(pawn, newPosition);

            // todo make some improvements
            // todo, make everything in one function !
            // todo solve problem - we can collide twice with some objects (that objects placed in several cells) - solution - every step we change position and check collision
            pawn.setSuperPosition(newPosition.getPosition());
        }
    }

    private void leaveAndJoinCells(MovingObject pawn, Frame newPosition) {
        int stepAmountY;
        int stepAmountX;
        CellsSpace wasOn = new CellsSpace(pawn);
        CellsSpace nowOn = new CellsSpace(newPosition);
        CellsSpace left = wasOn.getUnsharedSpace(nowOn);
        CellsSpace join = nowOn.getUnsharedSpace(wasOn);
        if(left != null) {
            stepAmountX = left.getRightX() - left.getLeftX();
            stepAmountY = left.getTopY() - left.getBotY();
            for (int i = 0; i <= stepAmountX; i++) {
                for (int j = 0; j <= stepAmountY; j++) {
                    gameState.get(
                            left.getLeftX() + i,
                            left.getBotY() + j).removeObject(pawn);
                }
            }
        }
        if(join != null) {
            stepAmountX = join.getRightX() - join.getLeftX();
            stepAmountY = join.getTopY() - join.getBotY();
            for (int i = 0; i <= stepAmountX; i++) {
                for (int j = 0; j <= stepAmountY; j++) {
                    gameState.get(
                            join.getLeftX() + i,
                            join.getBotY() + j).addObject(pawn);
                }
            }
        }
    }

    private void collide(MovingObject movingObject, ColliderFrame collidingSpace, CellsSpace passedThrough) {
        int stepAmount;
        int cellsBreadth;
        Cell cell;
        boolean changed;
        switch (movingObject.getDirection()) {
            case UP:
                stepAmount = passedThrough.getTopY() - passedThrough.getBotY();
                cellsBreadth = passedThrough.getRightX() - passedThrough.getLeftX();
                for (int i = 0; i <= stepAmount; i++) {
                    for (int j = 0; j <= cellsBreadth; j++) {
                        changed = false;
                        cell = gameState.get(
                                passedThrough.getLeftX() + j,
                                passedThrough.getBotY() + i);

                        for (GameObject object : cell.getObjects()) {
                            if (!object.collide(movingObject) && collidingSpace.collide(object)) {
                                changed = changed || collisionProcessor.onCollide(movingObject, object);
                            }
                        }
                        if (changed)
                            cell.setChanged(true);
                    }
                }
                break;

            case DOWN:
                stepAmount = passedThrough.getTopY() - passedThrough.getBotY();
                cellsBreadth = passedThrough.getRightX() - passedThrough.getLeftX();
                for (int i = 0; i <= stepAmount; i++) {
                    for (int j = 0; j <= cellsBreadth; j++) {
                        changed = false;
                        cell = gameState.get(
                                passedThrough.getLeftX() + j,
                                passedThrough.getTopY() - i);

                        for (GameObject object : cell.getObjects()) {
                            if (!object.collide(movingObject) && collidingSpace.collide(object)) {
                                changed = changed || collisionProcessor.onCollide(movingObject, object);
                            }
                        }
                        if (changed)
                            cell.setChanged(true);
                    }
                }
                break;

            case LEFT:
                stepAmount = passedThrough.getRightX() - passedThrough.getLeftX();
                cellsBreadth = passedThrough.getTopY() - passedThrough.getBotY();
                for (int i = 0; i <= stepAmount; i++) {
                    for (int j = 0; j <= cellsBreadth; j++) {
                        changed = false;
                        cell = gameState.get(
                                passedThrough.getRightX() - i,
                                passedThrough.getBotY() + j
                        );
                        for (GameObject object : cell.getObjects()) {
                            if (!object.collide(movingObject) && collidingSpace.collide(object)) {
                                changed = changed || collisionProcessor.onCollide(movingObject, object);
                            }
                        }
                        if (changed)
                            cell.setChanged(true);
                    }
                }
                break;

            case RIGHT:
                stepAmount = passedThrough.getRightX() - passedThrough.getLeftX();
                cellsBreadth = passedThrough.getTopY() - passedThrough.getBotY();
                for (int i = 0; i <= stepAmount; i++) {
                    for (int j = 0; j <= cellsBreadth; j++) {
                        changed = false;
                        cell = gameState.get(
                                passedThrough.getLeftX() + i,
                                passedThrough.getBotY() + j
                        );
                        for (int k = 0; k < cell.getObjects().size(); k ++) {
                            if (!cell.get(k).collide(movingObject) && collidingSpace.collide(cell.get(k))) {
                                changed = changed || collisionProcessor.onCollide(movingObject, cell.get(k));
                            }
                        }
                        if (changed)
                            cell.setChanged(true);
                    }
                }
                break;

            default:
                break;
        }
    }

    private Frame getCollidingSpace(MovingObject pawn, Frame newPosition) {
        switch (pawn.getDirection()) {
            case RIGHT:
                return new Frame(pawn.getBottomLeftPoint(), newPosition.getTopRightPoint());
            case LEFT:
                return new Frame(newPosition.getBottomLeftPoint(), pawn.getTopRightPoint());
            case DOWN:
                return new Frame(newPosition.getBottomLeftPoint(), pawn.getTopRightPoint());
            case UP:
                return new Frame(pawn.getBottomLeftPoint(), newPosition.getTopRightPoint());
            default:
                return null;
        }
    }

    // Проблема - с направлениями (пришлось через кейс делать)
    private Frame getResultPos(MovingObject pawn, ColliderFrame collidingSpace, Frame newPosition, CellsSpace passedThrough) {
        int stepAmount;
        int cellsBreadth;
        GameObject blockingObject;
        switch (pawn.getDirection()) {
            case UP:
                stepAmount = passedThrough.getTopY() - passedThrough.getBotY();
                cellsBreadth = passedThrough.getRightX() - passedThrough.getLeftX();
                for (int i = 0; i <= stepAmount; i++) {
                    blockingObject = null;
                    for (int j = 0; j <= cellsBreadth && (blockingObject == null); j++) {
                        blockingObject = getBlockingObject(pawn, collidingSpace,
                                gameState.get(
                                        passedThrough.getLeftX() + j,
                                        passedThrough.getBotY() + i)
                        );
                    }
                    if (blockingObject != null) {
                        return new Frame(
                                newPosition.getX(),
                                blockingObject.getLeftBotY() - newPosition.getSizeY() - EPS,
                                newPosition.getSizeX(),
                                newPosition.getSizeY()
                        );
                    }
                }
                break;

            case DOWN:
                stepAmount = passedThrough.getTopY() - passedThrough.getBotY();
                cellsBreadth = passedThrough.getRightX() - passedThrough.getLeftX();
                for (int i = 0; i <= stepAmount; i++) {
                    blockingObject = null;
                    for (int j = 0; j <= cellsBreadth && (blockingObject == null); j++) {
                        blockingObject = getBlockingObject(
                                pawn, collidingSpace,
                                gameState.get(
                                        passedThrough.getLeftX() + j,
                                        passedThrough.getTopY() - i)
                        );
                    }
                    if (blockingObject != null) {
                        return new Frame(
                                newPosition.getX(),
                                blockingObject.getRightTopY() + EPS,
                                newPosition.getSizeX(),
                                newPosition.getSizeY()
                        );
                    }
                }
                break;

            case LEFT:
                stepAmount = passedThrough.getRightX() - passedThrough.getLeftX();
                cellsBreadth = passedThrough.getTopY() - passedThrough.getBotY();
                for (int i = 0; i <= stepAmount; i++) {
                    blockingObject = null;
                    for (int j = 0; j <= cellsBreadth && (blockingObject == null); j++) {
                        blockingObject = getBlockingObject(
                                pawn, collidingSpace,
                                gameState.get(
                                        passedThrough.getRightX() - i,
                                        passedThrough.getBotY() + j)
                        );
                    }
                    if (blockingObject != null) {
                        return new Frame(
                                blockingObject.getRightTopX() + EPS,
                                newPosition.getY(),
                                newPosition.getSizeX(),
                                newPosition.getSizeY()
                        );
                    }
                }
                break;

            case RIGHT:
                stepAmount = passedThrough.getRightX() - passedThrough.getLeftX();
                cellsBreadth = passedThrough.getTopY() - passedThrough.getBotY();
                for (int i = 0; i <= stepAmount; i++) {
                    blockingObject = null;
                    for (int j = 0; j <= cellsBreadth && (blockingObject == null); j++) {
                        blockingObject = getBlockingObject(
                                pawn, collidingSpace,
                                gameState.get(
                                        passedThrough.getLeftX() + i,
                                        passedThrough.getBotY() + j)
                        );
                    }
                    if (blockingObject != null) {
                        return new Frame(
                                blockingObject.getLeftBotX() - newPosition.getSizeX() - EPS,
                                newPosition.getY(),
                                newPosition.getSizeX(),
                                newPosition.getSizeY()
                        );
                    }
                }
                break;

            default:
                break;
        }
        return newPosition;
    }

    private GameObject getBlockingObject(MovingObject movingObject, ColliderFrame collidingSpace, Cell cell) {
        GameObject blocksFirst = null;
        List<GameObject> objects = cell.getObjects();
        for (GameObject object : objects) {

            // means that if player is collided with object (like when he planted bomb) it doesnt have to block him
            if (collisionProcessor.isBlocked(movingObject, object, collidingSpace)) {
                if (blocksFirst == null)
                    blocksFirst = object;
                else {
                    switch (movingObject.getDirection()) {
                        case UP:
                            if (blocksFirst.getLeftBotY() > object.getLeftBotY())
                                blocksFirst = object;
                            break;
                        case DOWN:
                            if (blocksFirst.getRightTopY() < object.getRightTopY())
                                blocksFirst = object;
                            break;
                        case LEFT:
                            if (blocksFirst.getRightTopX() < object.getRightTopX())
                                blocksFirst = object;
                            break;
                        case RIGHT:
                            if (blocksFirst.getLeftBotX() > object.getLeftBotX())
                                blocksFirst = object;
                            break;
                    }
                }
            }
        }
        return blocksFirst;
    }

    private Frame move(Frame target, double speedX, double speedY, Direction direction, long ms) {
        switch (direction) {
            case UP:
                return new Frame(target.getX(), target.getY() + speedY * ms / 1000.0,
                        target.getSizeX(), target.getSizeY());

            case RIGHT:
                return new Frame(target.getX() + speedX * ms / 1000.0, target.getY(),
                        target.getSizeX(), target.getSizeY());

            case DOWN:
                return new Frame(target.getX(), target.getY() - speedY * ms / 1000.0,
                        target.getSizeX(), target.getSizeY());

            case LEFT:
                return new Frame(target.getX() - speedX * ms / 1000.0, target.getY(),
                        target.getSizeX(), target.getSizeY());

            default:
                return target;
        }
    }

    private Frame move(MovingObject object, long ms) {
        double speedX;
        double speedY;

        switch (object.getType()) {
            case Pawn:
                Pawn player = (Pawn) object;
                speedX = properties.getMovingSpeedX()
                        * (1 + properties.getSpeedBonusCoef() * player.getSpeedBonus());
                speedY = properties.getMovingSpeedY()
                        * (1 + properties.getSpeedBonusCoef() * player.getSpeedBonus());
                break;
            default:
                speedX = properties.getMovingSpeedX();
                speedY = properties.getMovingSpeedY();
                break;
        }

        return move(object, speedX, speedY, object.getDirection(), ms);
    }
}
