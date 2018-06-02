package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Affects number of limbs
 */
public class BodyEffect extends Effect {
    private final int legsDelta;
    private final int headDelta;
    private final int handDelta;

    public BodyEffect(int duration, String name, int legsDelta, int headDelta, int handDelta) {
        super(duration, name);
        this.legsDelta = legsDelta;
        this.headDelta = headDelta;
        this.handDelta = handDelta;
    }

    public int getLegsDelta() {
        return legsDelta;
    }

    public int getHeadDelta() {
        return headDelta;
    }

    public int getHandDelta() {
        return handDelta;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
