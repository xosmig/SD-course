package ru.spbau.mit.roguelike.model.visitors;

import ru.spbau.mit.roguelike.model.units.entity.BarrierEntity;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.DropEntity;

/**
 * Interface for any entity visitor
 */
public interface EntityVisitor {
    void visit(BarrierEntity entity);
    void visit(CharacterEntity entity);
    void visit(DropEntity entity);
    void visit(CreepEntity entity);
}
