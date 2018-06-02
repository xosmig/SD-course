package ru.spbau.mit.roguelike.model.visitors;

import ru.spbau.mit.roguelike.model.units.item.equipment.*;

/**
 * Interface for any item visitor
 */
public interface ItemVisitor {
    void visit(BodyArmor item);
    void visit(HandArmor item);
    void visit(HeadArmor item);
    void visit(LegArmor item);
    void visit(Weapon item);
}
