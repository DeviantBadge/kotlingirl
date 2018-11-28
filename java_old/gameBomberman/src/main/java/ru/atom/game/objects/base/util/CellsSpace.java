package ru.atom.game.objects.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.objects.base.Cell;

// идея - игрок хранит не массив клеток на которых он стоит а 4 элемента - i1 j1 i2 j2 - как показано на рисунке:
//
//  i2   -------
//      |       |
//      |       |
//  i1   -------
//      j1      j2
//
// по этим четырем числам однозначно определяется область с которой взаимодействует игрок, те клетки на которых он стоит,
// а с помощью простейших операций над этими числами запросто можно узнать на какую клетку он наступил, с какой сошел, а какие "пролетел"
public class CellsSpace {
    private static final Logger log = LoggerFactory.getLogger(CellsSpace.class);

    private int leftBotX;
    private int leftBotY;
    private int rightTopX;
    private int rightTopY;

    public CellsSpace() {
        leftBotX = -1;
        leftBotY = -1;
        rightTopX = -1;
        rightTopY = -1;
    }

    public CellsSpace(Frame position) {
        leftBotX = ((int) (position.getLeftBotX() / Cell.CELL_SIZE_X));
        leftBotY = ((int) (position.getLeftBotY() / Cell.CELL_SIZE_Y));
        rightTopX = ((int) (position.getRightTopX() / Cell.CELL_SIZE_X));
        rightTopY = ((int) (position.getRightTopY() / Cell.CELL_SIZE_Y));
    }

    public CellsSpace(int leftBotX, int leftBotY, int rightTopX, int rightTopY) {
        this.leftBotX = leftBotX;
        this.leftBotY = leftBotY;
        this.rightTopX = rightTopX;
        this.rightTopY = rightTopY;
    }

    public static void main(String[] args) {
        CellsSpace space1;
        CellsSpace space2;
        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(0, 0, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));
        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(0, 0, 5, 10);
        System.out.println(space1.getUnsharedSpace(space2));
        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(5, 0, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));
        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(0, 5, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));
        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(0, 0, 10, 5);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(1, 0, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(5, 0, 15, 10);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(5, 0, 15, 10);
        space2 = new CellsSpace(0, 0, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(20, 0, 30, 10);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(20, 0, 30, 10);
        space2 = new CellsSpace(0, 0, 10, 10);
        System.out.println(space1.getUnsharedSpace(space2));

        space1 = new CellsSpace(0, 0, 10, 10);
        space2 = new CellsSpace(0, 0, 30, 10);
        System.out.println(space1.getUnsharedSpace(space2));

    }

    public CellsSpace getUnsharedSpace(CellsSpace anotherSpace) {
        // суть - одна область включает другую, найти область, которой одна владеет, а вторая нет, если такой нет, то null
        //
        //  i2   -----------------      Пусть весь прямоугольник (1 объединенный с 2) это this, а 1 - это another space,
        //      |       |         |     Тогда нам нужно найти прямоугольник 2 (Главное что бы решалась задача указанная на
        //      |   1   |     2   |     рисунке, когда прямоугольники имеют 2 общие вершины, что будет с другими случаями нам не важно
        //  i1   -----------------
        //      j1      j2
        //

        boolean trivial = true;

        int newLeftBotX = leftBotX;
        int newLeftBotY = leftBotY;
        int newRightTopX = rightTopX;
        int newRightTopY = rightTopY;

        if (leftBotX < anotherSpace.leftBotX) {
            trivial = false;
            if (anotherSpace.leftBotX <= rightTopX) {
                newRightTopX = anotherSpace.leftBotX - 1;
            }
        }

        if (leftBotY < anotherSpace.leftBotY) {
            trivial = false;
            if (anotherSpace.leftBotY <= rightTopY) {
                newRightTopY = anotherSpace.leftBotY - 1;
            }
        }

        if (rightTopX > anotherSpace.rightTopX) {
            trivial = false;
            if (anotherSpace.rightTopX >= leftBotX) {
                newLeftBotX = anotherSpace.rightTopX + 1;
            }
        }

        if (rightTopY > anotherSpace.rightTopY) {
            trivial = false;
            if (anotherSpace.rightTopY >= leftBotY) {
                newLeftBotY = anotherSpace.rightTopY + 1;
            }
        }

        CellsSpace space = new CellsSpace(newLeftBotX, newLeftBotY, newRightTopX, newRightTopY);
        if (trivial || !space.isCanonical())
            return null;
        return space;
    }

    public boolean isCanonical() {
        return leftBotX <= rightTopX &&
                leftBotY <= rightTopY;
    }

    public int getLeftBotX() {
        return leftBotX;
    }

    public int getLeftBotY() {
        return leftBotY;
    }

    public int getRightTopX() {
        return rightTopX;
    }

    public int getRightTopY() {
        return rightTopY;
    }

    public int getLeftX() {
        return leftBotX;
    }

    public int getRightX() {
        return rightTopX;
    }

    public int getBotY() {
        return leftBotY;
    }

    public int getTopY() {
        return rightTopY;
    }

    public void setLeftBotX(int leftBotX) {
        if (leftBotX > rightTopX) {
            log.error("Left x border have to be less then right");
            this.leftBotX = rightTopX;
            this.rightTopX = leftBotX;
        } else
            this.leftBotX = leftBotX;
    }

    public void setLeftBotY(int leftBotY) {
        if (leftBotY > rightTopY) {
            log.error("bot y border have to be less then right");
            this.leftBotY = rightTopY;
            this.rightTopY = leftBotY;
        } else
            this.leftBotY = leftBotY;
    }

    public void setRightTopX(int rightTopX) {
        if (leftBotX > rightTopX) {
            log.error("Left x border have to be less then right");
            this.rightTopX = leftBotX;
            this.leftBotX = rightTopX;
        } else
            this.rightTopX = rightTopX;
    }

    public void setRightTopY(int rightTopY) {
        if (leftBotY > rightTopY) {
            log.error("bot y border have to be less then right");
            this.rightTopY = leftBotY;
            this.leftBotY = rightTopY;
        } else
            this.rightTopY = rightTopY;
    }

    @Override
    public String toString() {
        return leftBotX + " " + leftBotY + " " + rightTopX + " " + rightTopY;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof CellsSpace) {
            CellsSpace cells = (CellsSpace) anObject;
            return leftBotX == cells.leftBotX
                    && leftBotY == cells.leftBotY
                    && rightTopX == cells.rightTopX
                    && rightTopY == cells.rightTopY;
        }
        return false;
    }
}
