package ru.spbau.mit.roguelike.logic.visitors.effects;

import ru.spbau.mit.roguelike.model.units.effect.*;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.units.game.Game;

/**
 * Applies all state effects to entity
 */
public class ApplyStateEffectVisitor extends ApplyEffectVisitor {
    private final StateDescriptor state;

    public static void process(WorldEntity target, Game game) {
        if (target.getStateDescriptor() != null) {
            new ApplyStateEffectVisitor(target).process();
            if (target.getStateDescriptor().getHealth() == 0) {
                game.removeEntity(target.getId());
            }
            target.getStateDescriptor().setHealth(target.getStateDescriptor().getHealth() +
                    target.getStateDescriptor().getRegeneration());
            target.getStateDescriptor().setMana(target.getStateDescriptor().getMana() +
                    target.getStateDescriptor().getManaRegeneration());
        }
    }

    protected ApplyStateEffectVisitor(WorldEntity target) {
        super(target);
        state = target.getStateDescriptor();
    }

    @Override
    public void visit(BodyEffect effect) {
        //Not state effect
    }

    @Override
    public void visit(BurnEffect effect) {
        state.setHealth(state.getHealth() - effect.getDamage());
    }

    @Override
    public void visit(DisableEffect effect) {
        if (effect.isFreeze()) {
            state.setFrozen(true);
        }
        state.setSilenced(true);
    }

    @Override
    public void visit(DiseaseEffect effect) {
        state.setRegeneration(state.getRegeneration() - effect.getRegenerationReduce());
        state.setManaRegeneration(state.getManaRegeneration() - effect.getManaRegenerationReduce());
        state.setPhysicalDamage(state.getPhysicalDamage() - effect.getPhysicalDamageDelta());
        state.setMagicalDamage(state.getMagicalDamage() - effect.getMagicalDamageDelta());
    }

    @Override
    public void visit(ManaBurnEffect effect) {
        state.setMana(state.getMana() - effect.getManaDamage());
    }

    @Override
    public void visit(RegenerationEffect effect) {
        state.setRegeneration(state.getRegeneration() + effect.getHealthRegeneration());
        state.setManaRegeneration(state.getManaRegeneration() + effect.getManaRegeneration());
    }

    @Override
    public void visit(StatsEffect effect) {
        //Not state effect
    }
}
