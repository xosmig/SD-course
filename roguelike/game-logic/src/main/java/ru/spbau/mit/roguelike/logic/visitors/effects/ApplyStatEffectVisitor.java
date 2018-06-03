package ru.spbau.mit.roguelike.logic.visitors.effects;

import ru.spbau.mit.roguelike.model.units.effect.*;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;

/**
 * Applies all stat effects to entity
 */
public class ApplyStatEffectVisitor extends ApplyEffectVisitor {
    private final StatDescriptor stat;
    private int slownessDelta = 0;

    public static void process(WorldEntity target) {
        if (target.getCurrentStatDescriptor() != null) {
            ApplyStatEffectVisitor visitor = new ApplyStatEffectVisitor(target);
            visitor.process();
            target.getCurrentStatDescriptor().setSlownessModifier(visitor.slownessDelta);
        }
    }

    protected ApplyStatEffectVisitor(WorldEntity target) {
        super(target);
        stat = target.getCurrentStatDescriptor();
    }

    @Override
    public void visit(BodyEffect effect) {
        stat.setHeadsCount(stat.getHeadsCount() + effect.getHeadDelta());
        stat.setHandsCount(stat.getHandsCount() + effect.getHandDelta());
        stat.setLegsCount(stat.getLegsCount() + effect.getLegsDelta());
    }

    @Override
    public void visit(BurnEffect effect) {
        //Not stat effect
    }

    @Override
    public void visit(DisableEffect effect) {
        //Not stat effect
    }

    @Override
    public void visit(DiseaseEffect effect) {
        //Not stat effect
    }

    @Override
    public void visit(ManaBurnEffect effect) {
        //Not stat effect
    }

    @Override
    public void visit(RegenerationEffect effect) {
        //Not stat effect
    }

    @Override
    public void visit(StatsEffect effect) {
        stat.setStrength(stat.getStrength() + effect.getStrengthDelta());
        stat.setDexterity(stat.getDexterity() + effect.getDextirityDelta());
        stat.setStamina(stat.getStamina() + effect.getStaminaDelta());
        stat.setIntelligence(stat.getIntelligence() + effect.getIntelligenceDelta());
        stat.setWisdom(stat.getWisdom() + effect.getWisdomDelta());
        stat.setLuck(stat.getLuck() + effect.getLuckDelta());
        slownessDelta += effect.getSlownessDelta();
    }
}
