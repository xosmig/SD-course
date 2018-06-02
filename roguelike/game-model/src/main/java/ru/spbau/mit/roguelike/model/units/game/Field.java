package ru.spbau.mit.roguelike.model.units.game;

import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.Savable;

/**
 * Simple field representation
 */
public class Field implements Savable {
    private final Cell[][] cells;
    private final int width;
    private final int height;

    public Field(Cell[][] cells, int width, int heigth) {
        this.cells = cells;
        this.width = width;
        this.height = heigth;
    }

    public Cell getCellAt(Point point) {
        return cells[point.getX()][point.getY()];
    }

    public Field swapCellsAt(Point target1, Point target2) {
        if (target1.getX() < 1 || target1.getX() > width || target1.getY() < 1 || target1.getY() > height) {
            return this;
        }
        if (target2.getX() < 1 || target2.getX() > width || target2.getY() < 1 || target2.getY() > height) {
            return this;
        }

        Cell tmp = cells[target1.getX()][target1.getY()];
        cells[target1.getX()][target1.getY()] = cells[target2.getX()][target2.getY()];
        cells[target2.getX()][target2.getY()] = tmp;
        return this;
    }

    public void setCellAt(Point target, Cell cell) {
        cells[target.getX()][target.getY()] = cell;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
