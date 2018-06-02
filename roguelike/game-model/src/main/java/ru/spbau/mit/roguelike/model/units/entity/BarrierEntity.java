package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Any kind of barrier (both mortal and immortal)
 */
public class BarrierEntity extends WorldEntity {
    private final boolean immortal;

    public BarrierEntity(StatDescriptor statDescriptor, int level, boolean immortal) {
        super(statDescriptor, level);
        this.immortal = immortal;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
