package ru.spbau.mit.roguelike.logic.visitors.action;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of creep's action policy
 */
public final class CreepAI {
    private static final int AGGR_DISTANCE = Configuration.getInt("GAME_MONSTERS_AGGRESSIVE_DISTANCE");

    public static EntityAction getAction(Game game, CreepEntity entity) {
        List<EntityAction> variants = new ArrayList<>(Arrays.asList(EntityAction.LEFT, EntityAction.RIGHT,
                EntityAction.UP, EntityAction.DOWN));

        Point position = game.getEntityPositionById(entity.getId());
        TargetSelectionVisitor targetSelectionVisitor = new TargetSelectionVisitor(game, position);
        for (int dx = 0; dx <= AGGR_DISTANCE; dx++) {
            for (int dy = 0; dy <= AGGR_DISTANCE - dx; dy++) {
                game.visitEntityAt(Point.of(position.getX() + dx, position.getY() + dy), targetSelectionVisitor);
                game.visitEntityAt(Point.of(position.getX() - dx, position.getY() + dy), targetSelectionVisitor);
                game.visitEntityAt(Point.of(position.getX() + dx, position.getY() - dy), targetSelectionVisitor);
                game.visitEntityAt(Point.of(position.getX() - dx, position.getY() - dy), targetSelectionVisitor);
            }
        }
        if (targetSelectionVisitor.delta != null) {
            variants.clear();
            if (targetSelectionVisitor.delta.getX() < 0) {
                variants.add(EntityAction.LEFT);
            }
            if (targetSelectionVisitor.delta.getX() > 0) {
                variants.add(EntityAction.RIGHT);
            }
            if (targetSelectionVisitor.delta.getY() < 0) {
                variants.add(EntityAction.UP);
            }
            if (targetSelectionVisitor.delta.getY() > 0) {
                variants.add(EntityAction.DOWN);
            }
        }

        return RandomUtils.chooseRandom(variants);
    }

    private static class TargetSelectionVisitor implements EntityVisitor {
        private Game game;
        private Point delta = null;
        private final Point base;

        private TargetSelectionVisitor(Game game, Point base) {
            this.game = game;
            this.base = base;
        }

        @Override
        public void visit(BarrierEntity entity) {

        }

        @Override
        public void visit(CharacterEntity entity) {
            Point position = game.getEntityPositionById(entity.getId());
            if (delta == null || Math.abs(delta.getX()) + Math.abs(delta.getY()) >
                    Math.abs(position.getX() - base.getX()) + Math.abs(position.getY() - base.getY())) {
                delta = Point.of(position.getX() - base.getX(), position.getY() - base.getY());
            }
        }

        @Override
        public void visit(DropEntity entity) {}

        @Override
        public void visit(CreepEntity entity) {}
    }
}
