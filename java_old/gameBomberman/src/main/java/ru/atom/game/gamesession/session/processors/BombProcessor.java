package ru.atom.game.gamesession.session.processors;

import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.ingame.Bomb;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.socket.message.response.messagedata.Replica;

import java.util.ArrayList;
import java.util.List;

public class BombProcessor {
    private final GameSessionProperties properties;
    private final GameState gameState;
    private final ObjectCreator creator;
    private final Replica replica;

    public BombProcessor(GameSessionProperties properties, GameState gameState, ObjectCreator creator, Replica replica) {
        this.properties = properties;
        this.gameState = gameState;
        this.creator = creator;
        this.replica = replica;
    }

    public void plantBombBy(Pawn playerPawn) {
        Cell standingOn = gameState.get(playerPawn.getCenter());
        if (playerPawn.getBombCount() < playerPawn.getMaxBombAmount()) {
            boolean canPlant = true;
            List<GameObject> objects = standingOn.getObjects();
            for (GameObject object : objects) {
                switch (object.getType()) {
                    case Pawn:
                    case Fire:
                        break;
                    default:
                        canPlant = false;
                        break;
                }
            }
            if (canPlant) {
                gameState.addBomb(creator.createBomb(standingOn.getPosition(), playerPawn));
                playerPawn.incBombCount();
            }
        }
    }

    public void tickBomb(long ms) {
        List<Bomb> bombs = gameState.getBombs();
        List<Bomb> blowIt = new ArrayList<>();
        for (Bomb bomb : bombs) {
            bomb.tick(ms);
            if (bomb.isReady()) {
                blowIt.add(bomb);
            }
        }
        blowAll(blowIt);
        bombs.removeIf(Bomb::isDestroyed);
    }

    private void blowAll(List<Bomb> blowIt) {
        List<Bomb> nextBlow;
        do {
            blowIt.forEach(bomb -> bomb.setBlown(true));
            nextBlow = new ArrayList<>();

            for (Bomb bomb : blowIt) {
                blowBomb(bomb, nextBlow);
            }

            blowIt.forEach(Bomb::destroy);
            blowIt = nextBlow;
        } while (blowIt.size() != 0);
    }

    private void blowBomb(Bomb bomb, List<Bomb> nextBlow) {

        int x = bomb.getIntX() / 32;
        int y = bomb.getIntY() / 32;
        Cell cell = gameState.get(x, y);
        cell.setChanged(true);
        blow(cell, nextBlow);
        int blowRange = bomb.getOwner().getBlowRange();

        for (int j = 1; j <= blowRange && (y + j) < gameState.getSizeY(); j++) {
            if (blow(gameState.get(x, y + j), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (x + j) < gameState.getSizeX(); j++) {
            if (blow(gameState.get(x + j, y), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (y - j) >= 0; j++) {
            if (blow(gameState.get(x, y - j), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (x - j) >= 0; j++) {
            if (blow(gameState.get(x - j, y), nextBlow))
                break;
        }
        bomb.getOwner().decBombCount();
    }

    // todo we add a lot of fires - need to optimise
    private boolean blow(Cell cell, List<Bomb> nextBlow) {
        // if we return true we stop blowing !
        boolean destroyed = true;
        boolean stopDestroying = false;
        boolean changed = false;

        for (GameObject object : cell.getObjects()) {
            switch (object.getType()) {
                case Bomb:
                    // todo idea : blow bombs after delay - bomb blown, hit another, another decrease timer and blow after some delay
                    Bomb bomb = (Bomb) object;
                    if (!bomb.isBlown()) {
                        if (!bomb.isReady()) {
                            bomb.stop();
                            nextBlow.add(bomb);
                            changed = true;
                        }
                    }
                    stopDestroying = stopDestroying
                            || properties.isBlowStopsOnWall()
                            && (!object.isDestroyed() || properties.isBombBlowAsOne());
                    break;

                case Bonus:
                case Wood:
                    stopDestroying = stopDestroying || properties.isBlowStopsOnWall() && (!object.isDestroyed() || properties.isBombBlowAsOne());
                    changed = true;
                    destroyed = destroyed && object.destroy();
                    break;

                case Pawn:
                    changed = true;
                    destroyed = destroyed && object.destroy();
                    break;

                default:
                    destroyed = destroyed && object.destroy();
            }
        }

        if (changed)
            cell.setChanged(true);
        if (destroyed)
            addObjectToReplica(creator.createFire(cell.getPosition()));

        return !destroyed || stopDestroying;
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }
}
