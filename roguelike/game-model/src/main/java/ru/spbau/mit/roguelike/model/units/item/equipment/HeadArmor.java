package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Any head armor
 */
public class HeadArmor extends Equipment {
    public HeadArmor(String name, int level) {
        super(name, level);
    }

    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
