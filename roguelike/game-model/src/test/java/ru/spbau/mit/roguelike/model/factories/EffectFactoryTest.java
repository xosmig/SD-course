package ru.spbau.mit.roguelike.model.factories;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.units.effect.*;

import java.util.Collection;

import static org.junit.Assert.*;

public class EffectFactoryTest {
    @Before
    public void before(){
        EffectFactory.getNegativeEffect(1); //Just to load configuration
    }

    @Test
    public void getPositiveEffect() throws Exception {
        EffectFactory.getPositiveEffect(1, 1);
        EffectFactory.getPositiveEffect(1, 100000);
        EffectFactory.getPositiveEffect(100000, 1);
    }

    @Test
    public void getNegativeEffect() throws Exception {
        EffectFactory.getNegativeEffect(1, 1);
        EffectFactory.getNegativeEffect(1, 100000);
        EffectFactory.getNegativeEffect(100000, 1);
    }

    @Test
    public void getDisableEffect() throws Exception {
        assertFalse(EffectFactory.getDisableEffect(100, false).isFreeze());
        assertTrue(EffectFactory.getDisableEffect(100, true).isFreeze());
    }

    @Test
    public void getBurnEffect() throws Exception {
        int power = RandomUtils.getInt(Configuration.getInt("EFFECT_FACTORY_BURN_DAMAGE_CONSTANT"), 1000);
        int duration = RandomUtils.getInt(1, 1000);
        BurnEffect effect = EffectFactory.getBurnEffect(duration, power);
        assertEquals(power / Configuration.getInt("EFFECT_FACTORY_BURN_DAMAGE_CONSTANT"), effect.getDamage());
        assertEquals(duration, effect.getDuration());
    }

    @Test
    public void getManaBurnEffect() throws Exception {
        int power = RandomUtils.getInt(Configuration.getInt("EFFECT_FACTORY_MBURN_DAMAGE_CONSTANT"), 1000);
        int duration = RandomUtils.getInt(1, 1000);
        ManaBurnEffect effect = EffectFactory.getManaBurnEffect(duration, power);
        assertEquals(power / Configuration.getInt("EFFECT_FACTORY_MBURN_DAMAGE_CONSTANT"), effect.getManaDamage());
        assertEquals(duration, effect.getDuration());
    }

    @Test
    public void getDiseaseEffect() throws Exception {
        int power = RandomUtils.getInt(Configuration.getInt("EFFECT_FACTORY_DISEASE_MIN_POWER"), 1000);
        int duration = RandomUtils.getInt(1, 1000);
        DiseaseEffect effect = EffectFactory.getDiseaseEffect(duration, power);
        assertTrue(effect.getMagicalDamageDelta() != 0 || effect.getPhysicalDamageDelta() != 0 ||
                effect.getRegenerationReduce() != 0 || effect.getManaRegenerationReduce() != 0);
        assertEquals(duration, effect.getDuration());
    }

    @Test
    public void getRegenerationEffect() throws Exception {
        int power = RandomUtils.getInt(Configuration.getInt("EFFECT_FACTORY_REGENERATION_MIN_POWER"), 1000);
        int duration = RandomUtils.getInt(1, 1000);
        RegenerationEffect effect = EffectFactory.getRegenerationEffect(duration, power);
        assertTrue(effect.getHealthRegeneration() != 0 || effect.getManaRegeneration() != 0);
        assertEquals(duration, effect.getDuration());

    }

    @Test
    public void getStatsEffect() throws Exception {
        int power = RandomUtils.getInt(1, 1000);
        int duration = RandomUtils.getInt(1, 1000);
        StatsEffect effect = EffectFactory.getStatsEffect(duration, power, true);
        assertTrue(effect.getDextirityDelta() != 0 || effect.getStaminaDelta() != 0 ||
                effect.getLuckDelta() != 0 || effect.getStrengthDelta() != 0 ||
                effect.getWisdomDelta() != 0 || effect.getIntelligenceDelta() != 0 ||
                effect.getSlownessDelta() != 0);
        assertTrue(effect.getDextirityDelta() >= 0 && effect.getStaminaDelta() >= 0 &&
                effect.getLuckDelta() >= 0 && effect.getStrengthDelta() >= 0 &&
                effect.getWisdomDelta() >= 0 && effect.getIntelligenceDelta() >= 0 &&
                effect.getSlownessDelta() <= 0);
        assertEquals(duration, effect.getDuration());
        effect = EffectFactory.getStatsEffect(duration, power, false);
        assertTrue(effect.getDextirityDelta() != 0 || effect.getStaminaDelta() != 0 ||
                effect.getLuckDelta() != 0 || effect.getStrengthDelta() != 0 ||
                effect.getWisdomDelta() != 0 || effect.getIntelligenceDelta() != 0 ||
                effect.getSlownessDelta() != 0);
        assertTrue(effect.getDextirityDelta() <= 0 && effect.getStaminaDelta() <= 0 &&
                effect.getLuckDelta() <= 0 && effect.getStrengthDelta() <= 0 &&
                effect.getWisdomDelta() <= 0 && effect.getIntelligenceDelta() <= 0 &&
                effect.getSlownessDelta() >= 0);
        assertEquals(duration, effect.getDuration());
    }

    @Test
    public void getBodyEffect() throws Exception {
        int power = RandomUtils.getInt(Configuration.getInt("EFFECT_FACTORY_BODY_MIN_POWER"), 1000);
        int duration = RandomUtils.getInt(1, 1000);
        BodyEffect effect = EffectFactory.getBodyEffect(duration, power, true);
        assertTrue(effect.getHandDelta() != 0 || effect.getHeadDelta() != 0 ||
                effect.getLegsDelta() != 0);
        assertTrue(effect.getHandDelta() >= 0 || effect.getHeadDelta() >= 0 ||
                effect.getLegsDelta() >= 0);
        assertEquals(duration, effect.getDuration());
        effect = EffectFactory.getBodyEffect(duration, power, false);
        assertTrue(effect.getHandDelta() != 0 || effect.getHeadDelta() != 0 ||
                effect.getLegsDelta() != 0);
        assertTrue(effect.getHandDelta() <= 0 || effect.getHeadDelta() <= 0 ||
                effect.getLegsDelta() <= 0);
        assertEquals(duration, effect.getDuration());
    }

}