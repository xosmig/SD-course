package ru.spbau.mit.roguelike.model.factories;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.model.units.game.*;

import java.io.IOException;
import java.util.*;

/**
 * Factory that generates random fields for game
 */
public final class FieldFactory {
    static {
        try {
            Configuration.addFromStream(FieldFactory.class.getResourceAsStream("/FieldFactory.properties"));
        } catch (IOException e) {
            Logging.log(e);
        }
    }

    public static Field generateField(int height, int width) {
        return generateField(height, width,
                Configuration.getDouble("FIELD_FACTORY_FF_PROBABILITY"),
                Configuration.getDouble("FIELD_FACTORY_FB_PROBABILITY"),
                Configuration.getDouble("FIELD_FACTORY_BF_PROBABILITY"),
                Configuration.getDouble("FIELD_FACTORY_BB_PROBABILITY")
        );
    }

    public static Field generateField(
            int height,
            int width,
            double freeFreeProbability,
            double freeBusyProbability,
            double busyFreeProbability,
            double busyBusyProbability
    ) {
        Cell[][] cells = new Cell[width + 2][height + 2];
        //Init borders
        for (int i = 0; i < height + 2; i++) {
            cells[0][i] = new CellWithEntity(EntityFactory.getImmortalBarrier());
            cells[width + 1][i] = new CellWithEntity(EntityFactory.getImmortalBarrier());
        }
        for (int i = 0; i < width + 2; i++) {
            cells[i][0] = new CellWithEntity(EntityFactory.getImmortalBarrier());
            cells[i][height + 1] = new CellWithEntity(EntityFactory.getImmortalBarrier());
        }
        //Generate field
        for (int j = 1; j < height + 1; j++) {
            boolean freeState = RandomUtils.getBoolean();
            for (int i = 1; i < width + 1; i++) {
                cells[i][j] = freeState ? new EmptyCell() : new CellWithEntity(EntityFactory.getImmortalBarrier());
                freeState = freeState ?
                        (cells[i + 1][j - 1] instanceof EmptyCell ?
                                RandomUtils.getBoolean(freeFreeProbability) :
                                RandomUtils.getBoolean(freeBusyProbability)) :
                        (cells[i + 1][j - 1] instanceof EmptyCell ?
                                RandomUtils.getBoolean(busyFreeProbability) :
                                RandomUtils.getBoolean(busyBusyProbability));
            }
        }
        checkConnectivety(height, width, cells);
        return new Field(cells, width, height);
    }

    private static void checkConnectivety(int height, int width, Cell[][] field) {
        List<Point> emptyCellsPositions = new ArrayList<>();
        for (int i = 0; i < width + 2; i++) {
            for (int j = 0; j < height + 2; j++) {
                if (field[i][j] instanceof EmptyCell) {
                    emptyCellsPositions.add(Point.of(i, j));
                }
            }
        }
        RandomUtils.shuffle(emptyCellsPositions);
        Set<Point> painted = new HashSet<>();
        paintCells(height, width, emptyCellsPositions.get(0), field, painted);
        for (Point start : emptyCellsPositions) {
            if (painted.contains(start)) {
                continue;
            }
            DistancePositionPair resultingPath = null;
            PriorityQueue<DistancePositionPair> queue = new PriorityQueue<>();
            Set<Point> checked = new HashSet<>();
            queue.add(new DistancePositionPair(0, start, null));
            while (resultingPath == null) {
                DistancePositionPair current = queue.poll();
                Point p = current.position;
                if (painted.contains(p)) {
                    resultingPath = current;
                    continue;
                }
                checked.add(p);
                if (p.getX() > 0) {
                    Point newP = Point.of(p.getX() - 1, p.getY());
                    if (!checked.contains(newP)) {
                        checked.add(newP);
                        queue.add(new DistancePositionPair(
                                field[newP.getX()][newP.getY()] instanceof EmptyCell ?
                                        current.distance : current.distance + 1,
                                newP, current));
                    }
                }
                if (p.getX() < width + 1) {
                    Point newP = Point.of(p.getX() + 1, p.getY());
                    if (!checked.contains(newP)) {
                        checked.add(newP);
                        queue.add(new DistancePositionPair(
                                field[newP.getX()][newP.getY()] instanceof EmptyCell ?
                                        current.distance : current.distance + 1,
                                newP, current));
                    }
                }
                if (p.getY() > 0) {
                    Point newP = Point.of(p.getX(), p.getY() - 1);
                    if (!checked.contains(newP)) {
                        checked.add(newP);
                        queue.add(new DistancePositionPair(
                                field[newP.getX()][newP.getY()] instanceof EmptyCell ?
                                        current.distance : current.distance + 1,
                                newP, current));
                    }
                }
                if (p.getY() < height + 1) {
                    Point newP = Point.of(p.getX(), p.getY() + 1);
                    if (!checked.contains(newP)) {
                        checked.add(newP);
                        queue.add(new DistancePositionPair(
                                field[newP.getX()][newP.getY()] instanceof EmptyCell ?
                                        current.distance : current.distance + 1,
                                newP, current));
                    }
                }
            }
            while (resultingPath != null) {
                field[resultingPath.position.getX()][resultingPath.position.getY()] = new EmptyCell();
                resultingPath = resultingPath.previous;
            }
            paintCells(height, width, start, field, painted);
        }
    }

    private static void paintCells(int height, int width, Point p, Cell[][] field, Set<Point> painted) {
        LinkedList<Point> points = new LinkedList<>();
        points.add(p);
        painted.add(p);
        while (!points.isEmpty()) {
            Point target;
            p = points.poll();
            target = Point.of(p.getX() - 1, p.getY());
            if (p.getX() > 0 && field[p.getX() - 1][p.getY()] instanceof EmptyCell && !painted.contains(target)) {
                points.add(target);
                painted.add(target);
            }
            target = Point.of(p.getX() + 1, p.getY());
            if (p.getX() < width + 1 && field[p.getX() + 1][p.getY()] instanceof EmptyCell && !painted.contains(target)) {
                points.add(target);
                painted.add(target);
            }
            target = Point.of(p.getX(), p.getY() - 1);
            if (p.getY() > 0 && field[p.getX()][p.getY() - 1] instanceof EmptyCell && !painted.contains(target)) {
                points.add(target);
                painted.add(target);
            }
            target = Point.of(p.getX(), p.getY() + 1);
            if (p.getY() < height + 1 && field[p.getX()][p.getY() + 1] instanceof EmptyCell && !painted.contains(target)) {
                points.add(target);
                painted.add(target);
            }
        }
    }

    private static class DistancePositionPair implements Comparable {
        public final int distance;
        public final Point position;
        public final DistancePositionPair previous;

        private DistancePositionPair(int distance, Point position, DistancePositionPair previous) {
            this.distance = distance;
            this.position = position;
            this.previous = previous;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof DistancePositionPair) {
                return Integer.compare(this.distance, ((DistancePositionPair) o).distance);
            }
            return 0;
        }
    }
}
