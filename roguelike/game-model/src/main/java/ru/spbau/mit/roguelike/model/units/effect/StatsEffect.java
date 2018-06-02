package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Affects base stats of entity
 */
public class StatsEffect extends Effect {
    private final int strengthDelta;
    private final int dextirityDelta;
    private final int staminaDelta;
    private final int intelligenceDelta;
    private final int wisdomDelta;
    private final int luckDelta;
    private final int slownessDelta;

    public StatsEffect(
            int duration,
            String name,
            int strengthDelta,
            int dextirityDelta,
            int staminaDelta,
            int intelligenceDelta,
            int wisdomDelta,
            int luckDelta,
            int slownessDelta
    ) {
        super(duration, name);
        this.strengthDelta = strengthDelta;
        this.dextirityDelta = dextirityDelta;
        this.staminaDelta = staminaDelta;
        this.intelligenceDelta = intelligenceDelta;
        this.wisdomDelta = wisdomDelta;
        this.luckDelta = luckDelta;
        this.slownessDelta = slownessDelta;
    }

    public int getStrengthDelta() {
        return strengthDelta;
    }

    public int getDextirityDelta() {
        return dextirityDelta;
    }

    public int getStaminaDelta() {
        return staminaDelta;
    }

    public int getIntelligenceDelta() {
        return intelligenceDelta;
    }

    public int getWisdomDelta() {
        return wisdomDelta;
    }

    public int getLuckDelta() {
        return luckDelta;
    }

    public int getSlownessDelta() {
        return slownessDelta;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
