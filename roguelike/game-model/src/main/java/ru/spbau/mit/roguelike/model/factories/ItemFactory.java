package ru.spbau.mit.roguelike.model.factories;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.units.effect.*;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Factory for random item generation
 */
public final class ItemFactory {
    static {
        try {
            Configuration.addFromStream(ItemFactory.class.getResourceAsStream("/ItemFactory.properties"));
        } catch (IOException e) {
            //TODO log
        }
    }


    public static Item getItem(int level) {
        return getEquipment(level);
    }

    public static Equipment getEquipment(int level) {
        List<Equipment> variants = Arrays.asList(
                getWeapon(level),
                getHeadArmor(level),
                getHandArmor(level),
                getLegArmor(level),
                getBodyArmor(level)
        );
        List<Double> probabilities = Arrays.asList(
                Configuration.getDouble("ITEM_FACTORY_HAND_EQUIPMENT_PROBABILITY") / 2,
                Configuration.getDouble("ITEM_FACTORY_HEAD_EQUIPMENT_PROBABILITY"),
                Configuration.getDouble("ITEM_FACTORY_HAND_EQUIPMENT_PROBABILITY") / 2,
                Configuration.getDouble("ITEM_FACTORY_LEG_EQUIPMENT_PROBABILITY")
        );
        return RandomUtils.chooseRandom(variants, probabilities);
    }

    public static Weapon getWeapon(int level) {
        InnerGenerationDescription description = InnerGenerationDescription.generate(level);
        Weapon weapon = new Weapon(generateEquipmentName(description,
                Configuration.getList("ITEM_FACTORY_EQUIPMENT_WEAPON_NAMES")), level);
        List<Integer> attackAndDefenseLevels = RandomUtils.asSum(description.realLevel, 2);
        if (attackAndDefenseLevels.get(0) > 0) {
            getTargetEffects(attackAndDefenseLevels.get(0)).forEach(weapon::addtargetEffect);
        }
        if (attackAndDefenseLevels.get(1) > 0) {
            getEquipmentEffects(attackAndDefenseLevels.get(1)).forEach(weapon::addEffect);
        }
        description.totalEffects.forEach(weapon::addEffect);
        return weapon;
    }

    public static HeadArmor getHeadArmor(int level) {
        InnerGenerationDescription description = InnerGenerationDescription.generate(level);
        HeadArmor armor = new HeadArmor(generateEquipmentName(description,
                Configuration.getList("ITEM_FACTORY_EQUIPMENT_HEAD_NAMES")), level);
        getEquipmentEffects(description.realLevel).forEach(armor::addEffect);
        description.totalEffects.forEach(armor::addEffect);
        return armor;
    }

    public static HandArmor getHandArmor(int level) {
        InnerGenerationDescription description = InnerGenerationDescription.generate(level);
        HandArmor armor = new HandArmor(generateEquipmentName(description,
                Configuration.getList("ITEM_FACTORY_EQUIPMENT_HAND_NAMES")), level);
        getEquipmentEffects(description.realLevel).forEach(armor::addEffect);
        description.totalEffects.forEach(armor::addEffect);
        return armor;
    }

    public static LegArmor getLegArmor(int level) {
        InnerGenerationDescription description = InnerGenerationDescription.generate(level);
        LegArmor armor = new LegArmor(generateEquipmentName(description,
                Configuration.getList("ITEM_FACTORY_EQUIPMENT_LEG_NAMES")), level);
        getEquipmentEffects(description.realLevel).forEach(armor::addEffect);
        description.totalEffects.forEach(armor::addEffect);
        return armor;
    }

    public static BodyArmor getBodyArmor(int level) {
        InnerGenerationDescription description = InnerGenerationDescription.generate(level);
        BodyArmor armor = new BodyArmor(generateEquipmentName(description,
                Configuration.getList("ITEM_FACTORY_EQUIPMENT_BODY_NAMES")), level);
        getEquipmentEffects(description.realLevel).forEach(armor::addEffect);
        description.totalEffects.forEach(armor::addEffect);
        return armor;
    }

    private static String generateEquipmentName(InnerGenerationDescription description, List<String> baseEquipmentNames) {
        StringBuilder name = new StringBuilder();
        if (description.heavy != null) {
            name.append(Configuration.get("ITEM_FACTORY_HEAVY_NAME"));
            name.append(" ");
        }
        if (description.vampiring != null) {
            name.append(Configuration.get("ITEM_FACTORY_VAMPIRING_NAME"));
            name.append(" ");
        }
        if (description.beznogim != null) {
            name.append(Configuration.get("ITEM_FACTORY_BEZNOGIM_NAME"));
        } else {
            name.append(RandomUtils.chooseRandom(baseEquipmentNames));
        }
        if (description.silence != null) {
            name.append(" ");
            name.append(Configuration.get("ITEM_FACTORY_PHYSICAL_NAME"));
        }
        return name.toString();
    }


    private static Set<Effect> getEquipmentEffects(int levelSum) {
        int power = calculateStandardPower(levelSum);
        int effectsCount = 1;
        if (power >= Configuration.getInt("ITEM_FACTORY_EQUIPMENT_TWO_EFFECTS_MIN_POWER")) {
            effectsCount = RandomUtils.getInt(1, 2);
        }
        if (power >= Configuration.getInt("ITEM_FACTORY_EQUIPMENT_THREE_EFFECTS_MIN_POWER")) {
            effectsCount = RandomUtils.getInt(1, 3);
        }
        return RandomUtils.asSum(power, effectsCount).stream()
                .map(p -> EffectFactory.getPositiveEffect(0, p))
                .collect(Collectors.toSet());
    }

    private static Set<Effect> getTargetEffects(int levelSum) {
        int power = calculateStandardPower(levelSum);
        int effectsCount = 1;
        if (power >= Configuration.getInt("ITEM_FACTORY_EQUIPMENT_TWO_EFFECTS_MIN_POWER")) {
            effectsCount = RandomUtils.getInt(1, 2);
        }
        if (power >= Configuration.getInt("ITEM_FACTORY_EQUIPMENT_THREE_EFFECTS_MIN_POWER")) {
            effectsCount = RandomUtils.getInt(1, 3);
        }
        return RandomUtils.asSum(levelSum, effectsCount).stream()
                .map(EffectFactory::getNegativeEffect)
                .collect(Collectors.toSet());
    }

    private static int calculateStandardPower(int level) {
        return EffectFactory.calculateStandardPower(level,
                Configuration.getInt("ITEM_FACTORY_EQUIPMENT_POWER_METRIC_DURATION"));
    }

    private static class InnerGenerationDescription {
        public int realLevel;
        public BodyEffect beznogim = null;
        public BurnEffect vampiring = null;
        public DisableEffect silence = null;
        public StatsEffect heavy = null;
        public Set<Effect> totalEffects = new HashSet<>();

        public static InnerGenerationDescription generate(int level) {
            InnerGenerationDescription description = new InnerGenerationDescription();
            description.realLevel = Math.max(1, RandomUtils.getInt(
                    (int)(Configuration.getDouble("ITEM_FACTORY_REAL_LEVEL_MIN") * level),
                    (int)(Configuration.getDouble("ITEM_FACTORY_REAL_LEVEL_MAX") * level)));
            if (level > Configuration.getDouble("ITEM_FACTORY_HEAVY_MIN_LEVEL") &&
                    RandomUtils.getBoolean(Configuration.getDouble("ITEM_FACTORY_HEAVY_PROBABILITY"))) {

                description.heavy = new StatsEffect(0, Configuration.get("ITEM_FACTORY_HEAVY_EFFECT_NAME"),
                        0, level / 2, 0, 0, 0, 0, level / 10);
                description.realLevel += level;
                description.totalEffects.add(description.heavy);
            }
            if (level > Configuration.getDouble("ITEM_FACTORY_PHYSICAL_MIN_LEVEL") &&
                    RandomUtils.getBoolean(Configuration.getDouble("ITEM_FACTORY_PHYSICAL_PROBABILITY"))) {

                description.silence = new DisableEffect(0,
                        Configuration.get("ITEM_FACTORY_PHYSICAL_EFFECT_NAME"), false);
                description.realLevel = (int)(description.realLevel *
                        Configuration.getDouble("ITEM_FACTORY_PHYSICAL_ADDITIONAL_LEVEL"));
                description.totalEffects.add(description.silence);
            }
            if (level > Configuration.getDouble("ITEM_FACTORY_VAMPIRING_MIN_LEVEL") &&
                    RandomUtils.getBoolean(Configuration.getDouble("ITEM_FACTORY_VAMPIRING_PROBABILITY"))) {

                double q = RandomUtils.getDouble(Configuration.getDouble("ITEM_FACTORY_VAMPIRING_POWER_MIN"),
                        Configuration.getDouble("ITEM_FACTORY_VAMPIRING_POWER_MAX"));
                int power = calculateStandardPower((int)(q * level));
                description.vampiring = new BurnEffect(0, Configuration.get("ITEM_FACTORY_VAMPIRING_EFFECT_NAME"),
                        power / Configuration.getInt("EFFECT_FACTORY_BURN_DAMAGE_CONSTANT"));
                description.realLevel = (int)(description.realLevel *
                        (1 + q * Configuration.getDouble("ITEM_FACTORY_VAMPIRING_ADDITIONAL_LEVEL")));
                description.totalEffects.add(description.vampiring);
            }
            if (level > Configuration.getDouble("ITEM_FACTORY_BEZNOGIM_MIN_LEVEL") &&
                    RandomUtils.getBoolean(Configuration.getDouble("ITEM_FACTORY_BEZNOGIM_PROBABILITY"))) {

                double q = RandomUtils.getDouble(Configuration.getDouble("ITEM_FACTORY_BEZNOGIM_POWER_MIN"),
                        Configuration.getDouble("ITEM_FACTORY_BEZNOGIM_POWER_MAX"));
                int power = calculateStandardPower((int)(q * level));
                description.beznogim = EffectFactory.getBodyEffect(0, power, false);
                description.realLevel = (int)(description.realLevel *
                        (1 + q * Configuration.getDouble("ITEM_FACTORY_BEZNOGIM_ADDITIONAL_LEVEL")));
                description.totalEffects.add(description.beznogim);
            }
            return description;
        }
    }
}
