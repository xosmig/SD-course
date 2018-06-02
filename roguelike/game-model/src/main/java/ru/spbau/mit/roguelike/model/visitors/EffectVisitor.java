package ru.spbau.mit.roguelike.model.visitors;

import ru.spbau.mit.roguelike.model.units.effect.*;

/**
 * Interface for visiting effects
 */
public interface EffectVisitor {
    void visit(BodyEffect effect);
    void visit(BurnEffect effect);
    void visit(DisableEffect effect);
    void visit(DiseaseEffect effect);
    void visit(ManaBurnEffect effect);
    void visit(RegenerationEffect effect);
    void visit(StatsEffect effect);
}
