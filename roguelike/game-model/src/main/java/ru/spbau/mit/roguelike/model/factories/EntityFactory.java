package ru.spbau.mit.roguelike.model.factories;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Factory for random entities generation
 */
public final class EntityFactory {
    static {
        try {
            Configuration.addFromStream(EntityFactory.class.getResourceAsStream("/EntityFactory.properties"));
        } catch (IOException e) {
            Logging.log(e);
        }
    }


    public static BarrierEntity getImmortalBarrier() {
        return new BarrierEntity(new StatDescriptor(), 0, true);
    }

    public static BarrierEntity getBounty(int level) {
        return new BarrierEntity(new StatDescriptor(), level, false);
    }

    public static DropEntity getDrop(int level) {
        int count = RandomUtils.getInt(1, Configuration.getInt("ENTITY_FACTORY_DROP_MAX_COUNT"));
        double levelFactor = Configuration.getDouble("ENTITY_FACTORY_DROP_LEVEL_FACTOR");
        Set<Item> dropContent = new HashSet<>();
        for (int i = 0; i < count; i++) {
            dropContent.add(ItemFactory.getItem(
                    RandomUtils.getInt(Math.max(1, (int)(level * (1. - levelFactor))), (int)(level * (1. + levelFactor)))
            ));
        }
        return new DropEntity(dropContent);
    }

    public static CreepEntity getCreep(int level) {
        StatDescriptor statDescriptor = new StatDescriptor();
        List<Integer> stats = RandomUtils.asSum(
                level * Configuration.getInt("ENTITY_FACTORY_MONSTER_STATS_PER_LEVEL"), 6);
        int baseStat = Configuration.getInt("ENTITY_FACTORY_MONSTER_BASE_STATS");
        statDescriptor
                .setStrength(stats.get(0) + baseStat)
                .setDexterity(stats.get(1) + baseStat)
                .setStamina(stats.get(2) + baseStat)
                .setIntelligence(stats.get(3) + baseStat)
                .setWisdom(stats.get(4) + baseStat)
                .setLuck(stats.get(5) + baseStat);
        statDescriptor.resetSlowness();
        List<Integer> limbs = RandomUtils.asSum(Configuration.getInt("ENTITY_FACTORY_MONSTER_LIMB_BASE") +
                (int)(Math.pow(level, Configuration.getDouble("ENTITY_FACTORY_MONSTER_LIMB_LEVEL_FACTOR"))), 3);
        statDescriptor.setLegsCount(limbs.get(0));
        statDescriptor.setHandsCount(limbs.get(1));
        statDescriptor.setHeadsCount(limbs.get(2));

        List<String> baseName = Configuration.getList("ENTITY_FACTORY_MONSTER_NAMES");
        List<String> additionalNames = new ArrayList<>();
        if (statDescriptor.getHeadsCount() == 0) {
            additionalNames.addAll(Configuration.getList("ENTITY_FACTORY_MONSTER_WITHOUT_HEAD"));
        }
        if (statDescriptor.getHeadsCount() == 3) {
            additionalNames.addAll(Configuration.getList("ENTITY_FACTORY_MONSTER_THREE_HEAD"));
        }
        if (statDescriptor.getHandsCount() > 2) {
            additionalNames.addAll(Configuration.getList("ENTITY_FACTORY_MONSTER_MUCH_HANDS"));
        }
        if (statDescriptor.getLegsCount() == 0) {
            additionalNames.addAll(Configuration.getList("ENTITY_FACTORY_MONSTER_WITHOUT_LEGS"));
        }
        if (statDescriptor.getLegsCount() > 2) {
            additionalNames.addAll(Configuration.getList("ENTITY_FACTORY_MONSTER_MUCH_LEGS"));
        }
        String name = additionalNames.isEmpty() ? RandomUtils.chooseRandom(baseName) :
                RandomUtils.chooseRandom(additionalNames) + " " + RandomUtils.chooseRandom(baseName);

        CreepEntity creep = new CreepEntity(statDescriptor, level, name);
        for (int i = 0; i < statDescriptor.getHeadsCount(); i++) {
            creep.addEquipment(ItemFactory.getHeadArmor(level));
        }
        for (int i = 0; i < statDescriptor.getHandsCount(); i++) {
            creep.addEquipment(ItemFactory.getHandArmor(level));
            creep.addEquipment(ItemFactory.getWeapon(level));
        }
        for (int i = 0; i < statDescriptor.getLegsCount(); i++) {
            creep.addEquipment(ItemFactory.getLegArmor(level));
        }
        creep.addEquipment(ItemFactory.getBodyArmor(level));
        return creep;
    }

    public static CharacterEntity getCharacter(String name) {
        StatDescriptor statDescriptor = new StatDescriptor();
        int baseStat = Configuration.getInt("ENTITY_FACTORY_CHARACTER_BASE_STATS");
        statDescriptor
                .setStrength(baseStat)
                .setDexterity(baseStat)
                .setStamina(baseStat)
                .setIntelligence(baseStat)
                .setWisdom(baseStat)
                .setLuck(baseStat);
        statDescriptor.resetSlowness();
        statDescriptor.setLegsCount(2);
        statDescriptor.setHandsCount(2);
        statDescriptor.setHeadsCount(1);
        return new CharacterEntity(statDescriptor, 1, name);
    }
}
