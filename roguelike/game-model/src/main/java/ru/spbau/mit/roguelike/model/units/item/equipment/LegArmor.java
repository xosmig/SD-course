package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Any leg armor
 */
public class LegArmor extends Equipment {
    public LegArmor(String name, int level) {
        super(name, level);
    }

    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
