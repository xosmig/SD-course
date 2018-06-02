package ru.spbau.mit.roguelike.model.units.entity;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Savable;

/**
 * Describes stats of entity
 */
public class StatDescriptor implements Savable {
    private int strength = 1;
    private int dexterity = 1;
    private int stamina = 1;
    private int intelligence = 1;
    private int wisdom = 1;
    private int luck = 1;
    private int handsCount = 1;
    private int headsCount = 1;
    private int legsCount = 1;
    private int slowness = 1;

    public int getStrength() {
        return strength;
    }

    public StatDescriptor setStrength(int strength) {
        this.strength = Math.max(1, strength);
        return this;
    }

    public int getDexterity() {
        return dexterity;
    }

    public StatDescriptor setDexterity(int dexterity) {
        this.dexterity = Math.max(1, dexterity);
        return this;
    }

    public int getStamina() {
        return stamina;
    }

    public StatDescriptor setStamina(int stamina) {
        this.stamina = Math.max(1, stamina);
        return this;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public StatDescriptor setIntelligence(int intelligence) {
        this.intelligence = Math.max(1, intelligence);
        return this;
    }

    public int getWisdom() {
        return wisdom;
    }

    public StatDescriptor setWisdom(int wisdom) {
        this.wisdom = Math.max(1, wisdom);
        return this;
    }

    public int getLuck() {
        return luck;
    }

    public StatDescriptor setLuck(int luck) {
        this.luck = Math.max(1, luck);
        return this;
    }

    public int getHandsCount() {
        return handsCount;
    }

    public StatDescriptor setHandsCount(int handsCount) {
        this.handsCount = Math.max(0, handsCount);
        return this;
    }

    public int getHeadsCount() {
        return headsCount;
    }

    public StatDescriptor setHeadsCount(int headsCount) {
        this.headsCount = Math.max(0, headsCount);
        return this;
    }

    public int getLegsCount() {
        return legsCount;
    }

    public StatDescriptor setLegsCount(int legsCount) {
        this.legsCount = Math.max(0, legsCount);
        return this;
    }

    public int getSlowness() {
        return slowness;
    }

    public StatDescriptor setSlowness(int slowness) {
        this.slowness = Math.max(1, slowness);
        return this;
    }

    public StatDescriptor setSlownessModifier(int slowness) {
        this.slowness = Math.max(1, 10 + slowness - dexterity / 10);
        return this;
    }

    public StatDescriptor resetSlowness() {
        this.slowness = Math.max(1, 10 - dexterity / 10);
        return this;
    }

    public StateDescriptor generateStateDescriptor() {
        StateDescriptor result = new StateDescriptor(this);
        result.setHealth(stamina * Configuration.getInt("STATS_STAMINA_TO_HP"));
        result.setMana(wisdom * Configuration.getInt("STATS_WISDOM_TO_MP"));
        result.reset();
        return result;
    }

    public StatDescriptor clone() {
        StatDescriptor result = new StatDescriptor();
        return cloneTo(result);
    }

    protected StatDescriptor cloneTo(StatDescriptor target) {
        target.setStrength(strength)
                .setDexterity(dexterity)
                .setStamina(stamina)
                .setIntelligence(intelligence)
                .setWisdom(wisdom)
                .setLuck(luck)
                .setSlowness(slowness)
                .setHeadsCount(headsCount)
                .setHandsCount(handsCount)
                .setLegsCount(legsCount);
        return target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatDescriptor) {
            StatDescriptor other = (StatDescriptor) obj;
            return other.getDexterity() == dexterity &&
                    other.getStamina() == stamina &&
                    other.getStrength() == strength &&
                    other.getLuck() == luck &&
                    other.getWisdom() == wisdom &&
                    other.getIntelligence() == intelligence &&
                    other.getLegsCount() == legsCount &&
                    other.getHeadsCount() == headsCount &&
                    other.getHandsCount() == handsCount;
        }
        return super.equals(obj);
    }
}
