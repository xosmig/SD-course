package ru.spbau.mit.roguelike.logic.visitors.effects;

import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Abstract effect applier
 */
public abstract class ApplyEffectVisitor implements EffectVisitor {
    private final WorldEntity target;
    protected EffectInstance currentInstance = null;

    protected ApplyEffectVisitor(WorldEntity target) {
        this.target = target;
    }

    public void process() {
        target.getEquipment().forEach((eq) -> eq.getEffects().forEach(e -> e.accept(this)));
        target.getEffectInstances().forEach((ei) -> {
            currentInstance = ei;
            ei.getEffect().accept(this);
        });
    }
}
