package ru.spbau.mit.roguelike.model.units.effect;

import ru.spbau.mit.roguelike.commons.Savable;
import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;

/**
 * Any effect in world ust extend this abstract class
 */
public abstract class Effect implements Savable {
    private final int duration;
    private final String name;

    public Effect(int duration, String name) {
        this.duration = duration;
        this.name = name;
    }

    public EffectInstance instantiate() {
        return new EffectInstance(this);
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public abstract void accept(EffectVisitor visitor);
}
