package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.commons.Savable;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for an world entity
 */
public abstract class WorldEntity implements Savable {
    private static final AtomicInteger ID_GIVER = new AtomicInteger();

    private final StateDescriptor stateDescriptor;
    private final StatDescriptor baseStatDescriptor;
    private final StatDescriptor currentStatDescriptor;
    private final Set<Equipment> equipment = new HashSet<>();
    private final Set<EffectInstance> effectInstances = new HashSet<>();
    private int level;
    private int id = ID_GIVER.getAndIncrement();

    public WorldEntity(StatDescriptor baseStatDescriptor, int level) {
        this.level = level;
        this.baseStatDescriptor = baseStatDescriptor;
        currentStatDescriptor = baseStatDescriptor.clone();
        stateDescriptor = currentStatDescriptor.generateStateDescriptor();
    }

    public StateDescriptor getStateDescriptor() {
        return stateDescriptor;
    }

    public StatDescriptor getBaseStatDescriptor() {
        return baseStatDescriptor;
    }

    public void resetCurrentStatDescriptor() {
        baseStatDescriptor.cloneTo(currentStatDescriptor);
    }

    public void resetStateDescriptor() {
        stateDescriptor.reset();
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Set<EffectInstance> getEffectInstances() {
        return effectInstances;
    }

    public void removeEffectInstance(EffectInstance instance) {
        effectInstances.remove(instance);
    }

    public void addEffectInstance(EffectInstance instance) {
        effectInstances.add(instance);
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean addEquipment(Equipment e) {
        //TODO check conditions
        equipment.add(e);
        return true;
    }

    public void removeEquipment(Equipment e) {
        equipment.remove(e);
    }

    public StatDescriptor getCurrentStatDescriptor() {
        return currentStatDescriptor;
    }

    public abstract void accept(EntityVisitor visitor);
}
