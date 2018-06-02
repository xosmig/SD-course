package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Deals constant damage per turn
 */
public class BurnEffect extends Effect {
    private final int damage;

    public BurnEffect(int duration, String name, int damage) {
        super(duration, name);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
