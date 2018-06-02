package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Silence or freeze entity
 */
public class DisableEffect extends Effect {
    private final boolean freeze;

    public DisableEffect(int duration, String name, boolean freeze) {
        super(duration, name);
        this.freeze = freeze;
    }

    public boolean isFreeze() {
        return freeze;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
