package ru.spbau.mit.roguelike.logic.visitors.effects;

import org.junit.Test;
import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.units.effect.*;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.game.Cell;
import ru.spbau.mit.roguelike.model.units.game.Field;
import ru.spbau.mit.roguelike.model.units.game.Game;

import static org.junit.Assert.*;

public class EffectVisitorTest {
    @Test
    public void process() throws Exception {
        EntityFactory.getImmortalBarrier(); //To init configuration
        Game game = new Game(new Field(new Cell[10][10], 10, 10));
        StatDescriptor descriptor = new StatDescriptor()
                .setDexterity(100)
                .setIntelligence(110)
                .setLuck(120)
                .setWisdom(130)
                .setStrength(140)
                .setStamina(150)
                .setHandsCount(160)
                .setHeadsCount(170)
                .setLegsCount(180);
        descriptor.resetSlowness();
        CharacterEntity entity = new CharacterEntity(descriptor, 10, "Rofler");
        entity.addEffectInstance(new BurnEffect(1, "", 100).instantiate());
        entity.addEffectInstance(new ManaBurnEffect(1, "", 150).instantiate());
        entity.addEffectInstance(new BodyEffect(1, "", 8, 9, 10).instantiate());
        entity.addEffectInstance(new DiseaseEffect(1, "", 12, 13,
                10, 11).instantiate());
        entity.addEffectInstance(new RegenerationEffect(1, "", 5, 4).instantiate());
        entity.addEffectInstance(new StatsEffect(1, "", 1, 2, 3,
                4, 5, 6, 100).instantiate());

        entity.resetCurrentStatDescriptor();
        entity.resetStateDescriptor();
        StateDescriptor defaultDescriptor = descriptor.generateStateDescriptor();
        ApplyStateEffectVisitor.process(entity, game);
        assertEquals(entity.getBaseStatDescriptor(), entity.getCurrentStatDescriptor());
        assertEquals(defaultDescriptor.getHealth() - 100, entity.getStateDescriptor().getHealth());
        assertEquals(defaultDescriptor.getMana() - 150, entity.getStateDescriptor().getMana());
        assertEquals(defaultDescriptor.getRegeneration() - 5, entity.getStateDescriptor().getRegeneration());
        assertEquals(defaultDescriptor.getManaRegeneration() - 7, entity.getStateDescriptor().getManaRegeneration());
        assertEquals(defaultDescriptor.getMagicalDamage() - 13, entity.getStateDescriptor().getMagicalDamage());
        assertEquals(defaultDescriptor.getPhysicalDamage() - 12, entity.getStateDescriptor().getPhysicalDamage());

        ApplyStatEffectVisitor.process(entity);
        assertEquals(descriptor.getDexterity() + 2, entity.getCurrentStatDescriptor().getDexterity());
        assertEquals(descriptor.getStrength() + 1, entity.getCurrentStatDescriptor().getStrength());
        assertEquals(descriptor.getStamina() + 3, entity.getCurrentStatDescriptor().getStamina());
        assertEquals(descriptor.getLuck() + 6, entity.getCurrentStatDescriptor().getLuck());
        assertEquals(descriptor.getIntelligence() + 4, entity.getCurrentStatDescriptor().getIntelligence());
        assertEquals(descriptor.getWisdom() + 5, entity.getCurrentStatDescriptor().getWisdom());
        assertEquals(descriptor.getLegsCount() + 8, entity.getCurrentStatDescriptor().getLegsCount());
        assertEquals(descriptor.getHeadsCount() + 9, entity.getCurrentStatDescriptor().getHeadsCount());
        assertEquals(descriptor.getHandsCount() + 10, entity.getCurrentStatDescriptor().getHandsCount());
    }
}