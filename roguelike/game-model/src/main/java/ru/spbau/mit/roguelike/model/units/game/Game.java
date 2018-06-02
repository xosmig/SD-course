package ru.spbau.mit.roguelike.model.units.game;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.Savable;
import ru.spbau.mit.roguelike.commons.logging.Event;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.model.units.entity.BarrierEntity;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds information about world and perform non-trivial actions with it
 */
public class Game implements Savable {
    private final Field field;
    private final Map<Integer, WorldEntity> id2entity = new HashMap<>();
    private final Map<Integer, Point> id2entityPosition = new HashMap<>();
    private int worldLevel = 1;
    private int worldExp = 1;

    static {
        try {
            Configuration.addFromStream(Game.class.getResourceAsStream("/Game.properties"));
        } catch (IOException e) {
            //TODO log
        }
    }

    public Game(Field field) {
        this.field = field;
    }

    public int getWidth() {
        return field.getWidth();
    }

    public int getHeight() {
        return field.getHeight();
    }

    public Set<WorldEntity> getEntitities() {
        return id2entity.values().stream().filter(entity ->
                !(entity instanceof BarrierEntity && ((BarrierEntity)entity).isImmortal())).collect(Collectors.toSet());
    }

    public boolean isCellPassable(Point target) {
        return field.getCellAt(target) instanceof EmptyCell;
    }

    public WorldEntity getEntityById(int id) {
        return id2entity.get(id);
    }

    public Point getEntityPositionById(int id) {
        return id2entityPosition.get(id);
    }

    public boolean moveOrSpawnEntity(Point position, WorldEntity entity) {
        if (!isCellPassable(position)) {
            return false;
        }

        if (id2entity.containsKey(entity.getId())) {
            field.swapCellsAt(id2entityPosition.get(entity.getId()), position);
        } else {
            id2entity.put(entity.getId(), entity);
            field.setCellAt(position, new CellWithEntity(entity));
        }
        id2entityPosition.put(entity.getId(), position);
        return true;
    }

    public void removeEntity(int id) {
        if (id2entity.containsKey(id)) {
            id2entity.remove(id);
            field.setCellAt(id2entityPosition.remove(id), new EmptyCell());
        }
    }

    public void visitEntityAt(Point position, EntityVisitor visitor) {
        if (position.getX() < 0 || position.getX() > field.getWidth() + 1 ||
                position.getY() < 0 || position.getY() > field.getHeight() + 1) {
            return;
        }
        Cell cell = field.getCellAt(position);
        if (cell instanceof CellWithEntity) {
            ((CellWithEntity) cell).getEntity().accept(visitor);
        }
    }

    public int getWorldLevel() {
        return worldLevel;
    }

    public void increaseWorldExp(int level) {
        worldExp += level;
        if (worldExp > Math.pow(worldLevel, Configuration.getDouble("GAME_WORLD_EXPERIENCE_POWER"))) {
            Logging.log(new Event(Point.of(worldLevel, worldExp), " level up!"));
            worldExp = 0;
            worldLevel += 1;
        }
    }
}
