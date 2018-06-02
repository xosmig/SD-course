package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.units.effect.Effect;
import ru.spbau.mit.roguelike.model.units.item.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class for any equipable items
 */
public abstract class Equipment extends Item {
    private final Set<Effect> effects = new HashSet<>();

    public Equipment(String name, int level) {
        super(name, level);
    }

    public Equipment addEffect(Effect effect) {
        effects.add(effect);
        return this;
    }

    public Set<Effect> getEffects() {
        return effects;
    }
}
