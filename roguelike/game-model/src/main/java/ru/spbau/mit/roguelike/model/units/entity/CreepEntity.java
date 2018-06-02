package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

/**
 * Describes one of many creeps that lives in the world and trying to kill player
 */
public class CreepEntity extends WorldEntity {
    private final String name;

    public CreepEntity(StatDescriptor statDescriptor, int level, String name) {
        super(statDescriptor, level);
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
