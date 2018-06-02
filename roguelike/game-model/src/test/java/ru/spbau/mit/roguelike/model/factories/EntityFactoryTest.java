package ru.spbau.mit.roguelike.model.factories;

import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.DropEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;

import static org.junit.Assert.*;

public class EntityFactoryTest {
    @Test
    public void getImmortalBarrier() throws Exception {
        assertTrue(EntityFactory.getImmortalBarrier().isImmortal());
    }

    @Test
    public void getDrop() throws Exception {
        DropEntity drop = EntityFactory.getDrop(RandomUtils.getInt(1, 1000));
        assertTrue(drop.getContent().size() > 0 &&
                drop.getContent().size() <= Configuration.getInt("ENTITY_FACTORY_DROP_MAX_COUNT"));
    }

    @Test
    public void getCreep() throws Exception {
        CreepEntity creep = EntityFactory.getCreep(RandomUtils.getInt(1, 1000));
        StatDescriptor descriptor = creep.getBaseStatDescriptor();
        assertEquals(Configuration.getInt("ENTITY_FACTORY_MONSTER_BASE_STATS") * 6 +
                creep.getLevel() * Configuration.getInt("ENTITY_FACTORY_MONSTER_STATS_PER_LEVEL"),
                descriptor.getDexterity() + descriptor.getLuck() + descriptor.getIntelligence() +
                        descriptor.getWisdom() + descriptor.getStrength() + descriptor.getStamina());
        assertEquals(descriptor.getHandsCount() * 2 + descriptor.getHeadsCount() + descriptor.getLegsCount() + 1,
                creep.getEquipment().size());
    }

    @Test
    public void getCharacter() throws Exception {
        CharacterEntity player = EntityFactory.getCharacter("Hero");
        assertTrue(player.getInventory().getItems().isEmpty());
        assertTrue(player.getEquipment().isEmpty());
        StatDescriptor descriptor = player.getBaseStatDescriptor();
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getDexterity());
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getStamina());
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getStrength());
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getWisdom());
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getIntelligence());
        assertEquals(Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS"), descriptor.getLuck());
        assertEquals(1, descriptor.getHeadsCount());
        assertEquals(2, descriptor.getHandsCount());
        assertEquals(2, descriptor.getLegsCount());
    }

}