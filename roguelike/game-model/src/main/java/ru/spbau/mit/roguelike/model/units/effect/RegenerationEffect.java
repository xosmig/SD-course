package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Increases health and mana regeneration rate
 */
public class RegenerationEffect extends Effect {
    private final int healthRegeneration;
    private final int manaRegeneration;

    public RegenerationEffect(int duration, String name, int healthRegeneration, int manaRegeneration) {
        super(duration, name);
        this.healthRegeneration = healthRegeneration;
        this.manaRegeneration = manaRegeneration;
    }

    public int getHealthRegeneration() {
        return healthRegeneration;
    }

    public int getManaRegeneration() {
        return manaRegeneration;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
