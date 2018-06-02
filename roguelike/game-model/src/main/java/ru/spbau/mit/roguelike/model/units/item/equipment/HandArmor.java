package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Any hand armor
 */
public class HandArmor extends Equipment {
    public HandArmor(String name, int level) {
        super(name, level);
    }

    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
