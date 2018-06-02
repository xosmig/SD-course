package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.util.Set;

/**
 * Contains some useful item
 */
public class DropEntity extends WorldEntity {
    private final Set<Item> content;

    public DropEntity(Set<Item> content) {
        super(new StatDescriptor(), 0);
        this.content = content;
    }

    public Set<Item> getContent() {
        return content;
    }

    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
