package ru.spbau.mit.roguelike.model.factories;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.model.units.effect.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory for effects random generation
 */
public final class EffectFactory {
    static {
        try {
            Configuration.addFromStream(EffectFactory.class.getResourceAsStream("/EffectFactory.properties"));
        } catch (IOException e) {
            Logging.log(e);
        }
    }


    public static Effect getPositiveEffect(int duration, int power) {
        List<Effect> possibleEffects = new ArrayList<>();
        if (Configuration.getInt("EFFECT_FACTORY_REGENERATION_MIN_POWER") <= power) {
            possibleEffects.add(getRegenerationEffect(duration, power));
        }
        possibleEffects.add(getStatsEffect(duration, power, true));
        if (Configuration.getInt("EFFECT_FACTORY_BODY_MIN_POWER") <= power) {
            possibleEffects.add(getBodyEffect(duration, power, true));
        }
        return RandomUtils.chooseRandom(possibleEffects);
    }

    public static Effect getNegativeEffect(int duration, int power) {
        List<Effect> possibleEffects = new ArrayList<>();
        possibleEffects.add(getBurnEffect(duration, power));
        possibleEffects.add(getManaBurnEffect(duration, power));
        if (Configuration.getInt("EFFECT_FACTORY_DISEASE_MIN_POWER") <= power) {
            possibleEffects.add(getDiseaseEffect(duration, power));
        }
        possibleEffects.add(getStatsEffect(duration, power, false));
        if (Configuration.getInt("EFFECT_FACTORY_BODY_MIN_POWER") <= power) {
            possibleEffects.add(getBodyEffect(duration, power, false));
        }
        return RandomUtils.chooseRandom(possibleEffects);
    }

    public static int calculateStandardPower(int level, int duration) {
        return (int)(5 * level / Math.pow(duration,
                Configuration.getDouble("EFFECT_FACTORY_DURATION_POWER")));
    }

    public static Effect getNegativeEffect(int level) {
        int duration = RandomUtils.getInt(1, 1 + (int)Math.pow(level,
                Configuration.getDouble("EFFECT_FACTORY_MAX_LEVEL_DURATION_POWER")));
        int power = calculateStandardPower(level, duration);
        return getNegativeEffect(duration, power);
    }

    public static DisableEffect getDisableEffect(int level, boolean freeze) {
        int power = 1 + (freeze ? Configuration.getInt("EFFECT_FACTORY_DISABLE_FREEZE_CONSTANT") : 0);
        int durationConstant = Configuration.getInt("EFFECT_FACTORY_DISABLE_LEVEL_CONSTANT");
        int duration = (int)((durationConstant - (durationConstant - 1) * 1. / level) / power);
        return new DisableEffect(duration,
                freeze ? Configuration.get("EFFECT_FACTORY_DISABLE_FREEZE_NAME") :
                        Configuration.get("EFFECT_FACTORY_DISABLE_SILENCE_NAME"),
                freeze);
    }

    public static BurnEffect getBurnEffect(int duration, int power) {
        return new BurnEffect(duration, Configuration.get("EFFECT_FACTORY_BURN_NAME"),
                power / Configuration.getInt("EFFECT_FACTORY_BURN_DAMAGE_CONSTANT"));
    }

    public static ManaBurnEffect getManaBurnEffect(int duration, int power) {
        return new ManaBurnEffect(duration, Configuration.get("EFFECT_FACTORY_MBURN_NAME"),
                power / Configuration.getInt("EFFECT_FACTORY_MBURN_DAMAGE_CONSTANT"));
    }

    public static DiseaseEffect getDiseaseEffect(int duration, int power) {
        List<Integer> weights = Arrays.asList(
                Configuration.getInt("EFFECT_FACTORY_DISEASE_PHYSICAL_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_DISEASE_MAGICAL_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_DISEASE_REG_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_DISEASE_MREG_CONSTANT"));
        List<Integer> deltas = RandomUtils.asWeightedSum(power, weights);
        return new DiseaseEffect(duration, Configuration.get("EFFECT_FACTORY_DISEASE_NAME"),
                deltas.get(0), deltas.get(1), deltas.get(2), deltas.get(3));
    }

    public static RegenerationEffect getRegenerationEffect(int duration, int power) {
        List<Integer> weights = Arrays.asList(
                Configuration.getInt("EFFECT_FACTORY_REGENERATION_HEALTH_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_REGENERATION_MANA_CONSTANT"));
        List<Integer> deltas = RandomUtils.asWeightedSum(power, weights);
        return new RegenerationEffect(duration, Configuration.get("EFFECT_FACTORY_REGENERATION_NAME"),
                deltas.get(0), deltas.get(1));
    }

    public static StatsEffect getStatsEffect(int duration, int power, boolean positive) {
        List<Integer> deltas;
        if (power > Configuration.getInt("EFFECT_FACTORY_STATS_MIN_SLOWNESS_POWER")) {
            List<Integer> weights = Arrays.asList(
                    1, 1, 1, 1, 1, 1,
                    Configuration.getInt("EFFECT_FACTORY_STATS_SLOWNESS_CONSTANT"));
            deltas = RandomUtils.asWeightedSum(power, weights);
        } else {
            deltas = RandomUtils.asSum(power, 6);
            deltas.add(0);
        }
        return new StatsEffect(duration, positive ?
                Configuration.get("EFFECT_FACTORY_STATS_NAME_POSITIVE") :
                Configuration.get("EFFECT_FACTORY_STATS_NAME_NEGATIVE"),
                positive ? deltas.get(0) : -deltas.get(0),
                positive ? deltas.get(1) : -deltas.get(1),
                positive ? deltas.get(2) : -deltas.get(2),
                positive ? deltas.get(3) : -deltas.get(3),
                positive ? deltas.get(4) : -deltas.get(4),
                positive ? deltas.get(5) : -deltas.get(5),
                positive ? -deltas.get(6) : deltas.get(6));
    }

    public static BodyEffect getBodyEffect(int duration, int power, boolean positive) {
        List<Integer> weights = Arrays.asList(
                Configuration.getInt("EFFECT_FACTORY_BODY_LEG_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_BODY_HEAD_CONSTANT"),
                Configuration.getInt("EFFECT_FACTORY_BODY_HAND_CONSTANT"));
        List<Integer> deltas = RandomUtils.asWeightedSum(power, weights);
        return new BodyEffect(duration, positive ?
                Configuration.get("EFFECT_FACTORY_BODY_NAME_POSITIVE") :
                Configuration.get("EFFECT_FACTORY_BODY_NAME_NEGATIVE"),
                positive ? deltas.get(0) : -deltas.get(0),
                positive ? deltas.get(1) : -deltas.get(1),
                positive ? deltas.get(2) : -deltas.get(2));
    }
}
