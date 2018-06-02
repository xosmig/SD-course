package ru.spbau.mit.roguelike.logic.visitors.action;

import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.util.function.Function;

/**
 * Applies action of entity
 */
public class MakeTurnEntityVisitor implements EntityVisitor {
    private final Function<CharacterEntity, EntityAction> onPlayerTurn;
    private final Game world;

    public MakeTurnEntityVisitor(
            Function<CharacterEntity, EntityAction> onPlayerTurn,
            Game world
    ) {
        this.onPlayerTurn = onPlayerTurn;
        this.world = world;
    }

    @Override
    public void visit(BarrierEntity entity) {
        //Can't move
    }

    @Override
    public void visit(CharacterEntity entity) {
        EntityAction action = onPlayerTurn.apply(entity);
        performAction(entity, action, entity.getName());
    }

    @Override
    public void visit(DropEntity entity) {
        //Can't move
    }

    @Override
    public void visit(CreepEntity entity) {
        EntityAction action = CreepAI.getAction(world, entity);
        performAction(entity, action, entity.getName());
    }

    private void performAction(WorldEntity entity, EntityAction action, String actorName) {
        Point targetPoint = world.getEntityPositionById(entity.getId());
        switch (action) {
            case DOWN: {
                if (targetPoint.getY() == world.getHeight()) {
                    return;
                }
                targetPoint = Point.of(targetPoint.getX(), targetPoint.getY() + 1);
                break;
            }
            case LEFT: {
                if (targetPoint.getY() == 1) {
                    return;
                }
                targetPoint = Point.of(targetPoint.getX() - 1, targetPoint.getY());
                break;
            }
            case RIGHT: {
                if (targetPoint.getY() == world.getHeight()) {
                    return;
                }
                targetPoint = Point.of(targetPoint.getX() + 1, targetPoint.getY());
                break;
            }
            case UP: {
                if (targetPoint.getY() == 1) {
                    return;
                }
                targetPoint = Point.of(targetPoint.getX(), targetPoint.getY() - 1);
                break;
            }
            default: {
                return;
            }
        }
        world.visitEntityAt(targetPoint, new BattleTargetEntityVisitor(entity, actorName, world));
        if (world.isCellPassable(targetPoint)) {
            world.moveOrSpawnEntity(targetPoint, entity);
        }
    }
}
