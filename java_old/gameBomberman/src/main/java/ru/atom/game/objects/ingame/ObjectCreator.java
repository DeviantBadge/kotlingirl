package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.IdGen;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.gamesession.properties.GameObjectProperties;

public class ObjectCreator {
    private final IdGen idGen;
    private final GameObjectProperties properties;

    public ObjectCreator(GameObjectProperties properties) {
        this.properties = properties;
        idGen = new IdGen(false);
    }

    public Bomb createBomb(Point point, Pawn owner) {
        Bomb bomb = new Bomb(idGen.generateId(), point, owner, properties.getBombBlowTimeMs(), true);
        bomb.start();
        return bomb;
    }

    public Bonus createBonus(Point point, boolean considerProbability) {
        double bonusCast = Math.random();
        double bonusTypeCast = Math.random();
        if (!considerProbability || bonusCast < properties.getBonusProbability()) {
            if (bonusTypeCast < properties.getSpeedProbability())
                return new Bonus(idGen.generateId(), point, Bonus.BonusType.SPEED, false);
            else
                bonusTypeCast -= properties.getSpeedProbability();
            if (bonusTypeCast < properties.getBombsProbability())
                return new Bonus(idGen.generateId(), point, Bonus.BonusType.BOMBS, false);
            return new Bonus(idGen.generateId(), point, Bonus.BonusType.RANGE, false);
        }
        return null;
    }

    public Bonus createBonus(Point point, Bonus.BonusType type) {
        return new Bonus(idGen.generateId(), point, type, false);
    }

    public Fire createFire(Point point) {
        return new Fire(idGen.generateId(), point, false);
    }

    public Pawn createPawn(Point point) {
        return new Pawn(idGen.generateId(), point,
                new Point(
                        Cell.CELL_SIZE_X * (1 - properties.getColliderSize()) / 2,
                        Cell.CELL_SIZE_Y * (1 - properties.getColliderSize()) / 2),
                Cell.CELL_SIZE_X * properties.getColliderSize(),
                Cell.CELL_SIZE_Y * properties.getColliderSize(),
                properties.isPlayerStopsPlayer(), /* поставить параметр из пропертей */
                properties.getRangeOnStart(),
                properties.getBombsOnStart(),
                properties.getSpeedOnStart());
    }

    public Wall createWall(Point point) {
        return new Wall(idGen.generateId(), point, true);
    }

    public Wood createWood(Point point) {
        return new Wood(idGen.generateId(), point, true);
    }
}
