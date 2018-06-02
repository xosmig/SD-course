package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Class for body armor
 */
public class BodyArmor extends Equipment {
    public BodyArmor(String name, int level) {
        super(name, level);
    }

    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
