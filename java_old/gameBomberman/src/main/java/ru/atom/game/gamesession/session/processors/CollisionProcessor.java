package ru.atom.game.gamesession.session.processors;

import ru.atom.game.enums.ObjectType;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.MovingObject;
import ru.atom.game.objects.base.util.ColliderFrame;
import ru.atom.game.objects.ingame.Bonus;
import ru.atom.game.objects.ingame.Pawn;

public class CollisionProcessor {
    private GameSessionProperties properties;

    public CollisionProcessor(GameSessionProperties properties) {
        this.properties = properties;
    }

    // todo - different colliders can be blocked by different objects, mb realise that in future (make another class that will handle it)
    boolean isBlocked(MovingObject movingObject, GameObject object, ColliderFrame collidingSpace) {
        switch (movingObject.getType()) {
            case Pawn:
                return object.isBlocking() && !object.collide(movingObject) && collidingSpace.collide(object);

            default:
                return false;
        }
    }

    boolean onCollide(MovingObject moving, GameObject object) {
        boolean changed = false;
        switch (moving.getType()) {
            case Pawn:
                Pawn pawn = ((Pawn) moving);
                switch (object.getType()) {
                    case Bonus:
                        switch (((Bonus) object).getBonusType()) {
                            case BOMBS:
                                pawn.incMaxBombAmount();
                                break;
                            case RANGE:
                                pawn.incBlowRange();
                                break;
                            case SPEED:
                                pawn.incSpeedBonus();
                                break;
                        }
                        ((Bonus) object).pickUp();
                        changed = true;
                        break;

                    case Pawn:
                    case Bomb:
                    case Fire:
                    case Wall:
                    case Wood:
                        break;
                }
                break;
            default:
                return false;
        }
        return changed;
    }
}
