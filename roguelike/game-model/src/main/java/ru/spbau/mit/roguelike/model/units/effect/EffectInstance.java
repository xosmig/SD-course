package ru.spbau.mit.roguelike.model.units.effect;

/**
 * Instance of effect
 */
public class EffectInstance {
    private int duration;
    private final Effect effect;

    public EffectInstance(Effect effect) {
        this.effect = effect;
        duration = effect.getDuration();
    }

    public void decreaseDuration() {
        duration--;
    }

    public boolean isEnded() {
        return duration <= 0;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }
}
