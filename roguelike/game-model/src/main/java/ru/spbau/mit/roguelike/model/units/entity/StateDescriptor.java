package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Savable;

/**
 * Describes current state of entity
 */
public class StateDescriptor implements Savable {
    private int health;
    private int mana;
    private int regeneration;
    private int manaRegeneration;
    private boolean frozen = false;
    private boolean silenced = false;
    private int physicalDamage;
    private int magicalDamage;
    private final StatDescriptor control;

    public StateDescriptor(StatDescriptor control) {
        this.control = control;
    }

    public StateDescriptor reset() {
        setRegeneration(control.getStamina() / 10);
        setManaRegeneration(control.getWisdom() / 8);
        setPhysicalDamage(control.getStrength());
        setMagicalDamage(control.getIntelligence());
        frozen = false;
        silenced = false;
        return this;
    }

    public int getHealth() {
        return health;
    }

    public StateDescriptor setHealth(int health) {
        this.health = Math.max(0, Math.min(health, control.getStamina() * Configuration.getInt("STATS_STAMINA_TO_HP")));
        return this;
    }

    public int getRegeneration() {
        return regeneration;
    }

    public StateDescriptor setRegeneration(int regeneration) {
        this.regeneration = Math.max(0, regeneration);
        return this;
    }

    public int getMana() {
        return mana;
    }

    public StateDescriptor setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, control.getWisdom() * Configuration.getInt("STATS_WISDOM_TO_MP")));
        return this;
    }

    public int getManaRegeneration() {
        return manaRegeneration;
    }

    public StateDescriptor setManaRegeneration(int manaRegeneration) {
        this.manaRegeneration = Math.max(0, manaRegeneration);
        return this;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public StateDescriptor setFrozen(boolean frozen) {
        this.frozen = frozen;
        return this;
    }

    public boolean isSilenced() {
        return silenced;
    }

    public StateDescriptor setSilenced(boolean silenced) {
        this.silenced = silenced;
        return this;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }

    public StateDescriptor setPhysicalDamage(int physicalDamage) {
        this.physicalDamage = Math.max(0, physicalDamage);
        return this;
    }

    public int getMagicalDamage() {
        return magicalDamage;
    }

    public StateDescriptor setMagicalDamage(int magicalDamage) {
        this.magicalDamage = Math.max(0, magicalDamage);
        return this;
    }
}
