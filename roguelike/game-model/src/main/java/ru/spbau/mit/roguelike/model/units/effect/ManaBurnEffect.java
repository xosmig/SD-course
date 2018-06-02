package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Decreases current mana count per turn
 */
public class ManaBurnEffect extends Effect {
    private final int manaDamage;

    public ManaBurnEffect(int duration, String name, int manaDamage) {
        super(duration, name);
        this.manaDamage = manaDamage;
    }

    public int getManaDamage() {
        return manaDamage;
    }

    @Override
    public void accept(EffectVisitor visitor) {
        visitor.visit(this);
    }
}
