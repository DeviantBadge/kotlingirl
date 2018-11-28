package ru.atom.game.gamesession.state;

import ru.atom.game.enums.FieldType;
import ru.atom.game.enums.ObjectType;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.objects.ingame.ObjectCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameField {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;

    //todo add new maps
    private final List<List<Cell>> cells;
    private List<Point> initialPoints;
    private List<Point> freePoints;
    private final int sizeX;
    private final int sizeY;
    private final int borderWidth;

    private ObjectCreator creator;


    GameField(ObjectCreator creator, GameSessionProperties properties) {
        this.creator = creator;
        this.sizeX = properties.getFieldSizeX();
        this.sizeY = properties.getFieldSizeY();
        cells = new ArrayList<>();
        this.borderWidth = properties.getBorderWidth();

        initialPoints = new ArrayList<>();
        freePoints = new ArrayList<>();
    }

    public void createField(FieldType type) {
        switch (type) {
            case WARM_UP:
                createWarmUpField();
                break;
            case GAME_LOW_DENSITY:
                createGameFieldLow();
                break;
            case GAME_HIGH_DENSITY:
                createGameFieldHigh();
                break;
            case BONUS_VEIN:
                createBonusVein();
                break;
            default:
                break;
        }
    }

    private void createWarmUpField() {
        Cell cell;
        cells.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Point(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i < borderWidth) || (j < borderWidth)
                        || (i > (sizeX - (1 + borderWidth)))
                        || (j > (sizeY - (1 + borderWidth)))) {
                    cell.addObject(creator.createWall(cell.getPosition()));
                    continue;
                }

                if ((i > borderWidth) && (j > borderWidth)
                        && (i < (sizeX - (1 + borderWidth)))
                        && (j < (sizeY - (1 + borderWidth)))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions(borderWidth);
    }

    private void createGameFieldLow() {
        Cell cell;
        cells.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Point(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i < borderWidth) || (j < borderWidth)
                        || (i > (sizeX - (1 + borderWidth)))
                        || (j > (sizeY - (1 + borderWidth)))) {
                    cell.addObject(creator.createWall(cell.getPosition()));
                    continue;
                }

                if ((i > borderWidth) && (j > borderWidth)
                        && (i < (sizeX - (1 + borderWidth)))
                        && (j < (sizeY - (1 + borderWidth)))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        cell.addObject(creator.createWall(cell.getPosition()));
                        continue;
                    }
                }

                if ((i > (1 + borderWidth)) && (i < (sizeX - (2 + borderWidth)))
                        || (j > (1 + borderWidth)) && (j < (sizeY - (2 + borderWidth)))) {
                    if (((i % 2) ^ (j % 2)) == 1)
                        cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions(borderWidth);
    }

    private void createGameFieldHigh() {
        Cell cell;
        cells.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Point(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i < borderWidth) || (j < borderWidth)
                        || (i > (sizeX - (1 + borderWidth)))
                        || (j > (sizeY - (1 + borderWidth)))) {
                    cell.addObject(creator.createWall(cell.getPosition()));
                    continue;
                }

                if ((i > borderWidth) && (j > borderWidth)
                        && (i < (sizeX - (1 + borderWidth)))
                        && (j < (sizeY - (1 + borderWidth)))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        cell.addObject(creator.createWall(cell.getPosition()));
                        continue;
                    }
                }

                if ((i > (1 + borderWidth)) && (i < (sizeX - (2 + borderWidth)))
                        || (j > (1 + borderWidth)) && (j < (sizeY - (2 + borderWidth)))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions(borderWidth);
    }

    private void createBonusVein() {
        Cell cell;
        cells.clear();

        int veinSizeX = Math.max(sizeX, sizeY) / 5;
        int veinSizeY = Math.max(sizeX, sizeY) / 5;
        if ((veinSizeX & 1) != (sizeX & 1))
            --veinSizeX;
        if ((veinSizeY & 1) != (sizeY & 1))
            --veinSizeY;
        int veinPosX = (sizeX - veinSizeX) / 2;
        int veinPosY = (sizeY - veinSizeY) / 2;

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Point(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i < borderWidth) || (j < borderWidth)
                        || (i > (sizeX - (1 + borderWidth)))
                        || (j > (sizeY - (1 + borderWidth)))) {
                    cell.addObject(creator.createWall(cell.getPosition()));
                    continue;
                }

                if ((i >= veinPosX) && (i < veinPosX + veinSizeX)
                        && (j >= veinPosY) && (j < veinPosY + veinSizeY)) {
                    cell.addObject(creator.createBonus(cell.getPosition(), false));
                    continue;
                }

                if ((i > borderWidth) && (j > borderWidth)
                        && (i < (sizeX - (1 + borderWidth)))
                        && (j < (sizeY - (1 + borderWidth)))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        cell.addObject(creator.createWall(cell.getPosition()));
                        continue;
                    }
                }

                if ((i > (1 + borderWidth)) && (i < (sizeX - (2 + borderWidth)))
                        || (j > (1 + borderWidth)) && (j < (sizeY - (2 + borderWidth)))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions(1);
    }

    private void generateCommonPlayerPositions(int borderWidth) {
        initialPoints.clear();
        freePoints.clear();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    initialPoints.add(get(borderWidth, borderWidth).getPosition());
                    break;
                case 1:
                    initialPoints.add(get(sizeX - (1 + borderWidth), sizeY - (1 + borderWidth) ).getPosition());
                    break;
                case 2:
                    initialPoints.add(get(borderWidth, sizeY - (1 + borderWidth)).getPosition());
                    break;
                case 3:
                    initialPoints.add(get(sizeX - (1 + borderWidth), borderWidth).getPosition());
                    break;
            }
        }
        freePoints.addAll(initialPoints);
    }

    Point getRandomStartPos() {
        if (freePoints.size() == 0) {
            freePoints.addAll(initialPoints);
        }
        int num = ((int) (freePoints.size() * Math.random()));
        return freePoints.remove(num);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Cell get(int x, int y) {
        return cells.get(x).get(y);
    }

    public List<Replicable> getFieldReplica() {
        List<Replicable> replicables = new ArrayList<>();
        List<Cell> column;
        Cell cell;
        GameObject object;
        for (int i = 0; i < cells.size(); i++) {
            column = cells.get(i);
            for (int j = 0; j < column.size(); j++) {
                cell = column.get(j);
                for (int k = 0; k < cell.getObjects().size(); k++) {
                    object = cell.get(k);
                    if (object.getType() != ObjectType.Pawn && object.getType() != ObjectType.Bomb)
                        replicables.add(object);
                }
            }
        }
        return replicables;
    }

    public void forEach(Consumer<? super Cell> action) {
        cells.forEach(columns -> columns.forEach(action::accept));
    }
}
