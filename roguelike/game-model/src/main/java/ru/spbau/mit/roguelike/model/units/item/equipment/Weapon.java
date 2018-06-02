package ru.spbau.mit.roguelike.model.units.item.equipment;

import ru.spbau.mit.roguelike.model.units.effect.Effect;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Any weapon
 */
public class Weapon extends Equipment {
    private final Set<Effect> targetEffects = new HashSet<>();

    public Weapon(String name, int level) {
        super(name, level);
    }

    public Weapon addtargetEffect(Effect e) {
        targetEffects.add(e);
        return this;
    }

    public Set<Effect> getTargetEffects() {
        return targetEffects;
    }

    public Set<EffectInstance> getTargetEffectInstances() {
        return targetEffects.stream().map(Effect::instantiate).collect(Collectors.toSet());
    }

    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
