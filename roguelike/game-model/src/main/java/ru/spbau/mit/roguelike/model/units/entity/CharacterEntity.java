package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.model.units.entity.inventory.Inventory;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

/**
 * Player's character in this world
 */
public class CharacterEntity extends WorldEntity {
    private final String name;
    private final Inventory inventory = new Inventory();

    public CharacterEntity(StatDescriptor statDescriptor, int level, String name) {
        super(statDescriptor, level);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
