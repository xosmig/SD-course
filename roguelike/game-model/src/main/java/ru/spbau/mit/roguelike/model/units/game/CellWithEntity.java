package ru.spbau.mit.roguelike.model.units.game;

import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;

/**
 * Created by ArgentumWalker on 30.05.2018.
 */
public class CellWithEntity extends Cell {
    private final WorldEntity entity;

    public CellWithEntity(WorldEntity entity) {
        this.entity = entity;
    }

    public WorldEntity getEntity() {
        return entity;
    }
}
