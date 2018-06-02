package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Sickness that decreases regeneration and damage
 */
public class DiseaseEffect extends Effect {
    private final int physicalDamageDelta;
    private final int magicalDamageDelta;
    private final int regenerationReduce;
    private final int manaRegenerationReduce;

    public DiseaseEffect(int duration, String name,
                         int physicalDamageDelta, int magicalDamageDelta,
                         int regenerationReduce, int manaRegenerationReduce) {
        super(duration, name);
        this.physicalDamageDelta = physicalDamageDelta;
        this.magicalDamageDelta = magicalDamageDelta;
        this.regenerationReduce = regenerationReduce;
        this.manaRegenerationReduce = manaRegenerationReduce;
    }

    public int getPhysicalDamageDelta() {
        return physicalDamageDelta;
    }

    public int getMagicalDamageDelta() {
        return magicalDamageDelta;
    }

    public int getRegenerationReduce() {
        return regenerationReduce;
    }

    public int getManaRegenerationReduce() {
        return manaRegenerationReduce;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
