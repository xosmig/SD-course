package ru.spbau.mit.roguelike.uicommon.views.entity;

import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.uicommon.views.View;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract entity viewer
 */
public abstract class EntityView extends View {
    protected final WorldEntity entity;
    private final List<EffectInstance> effects;
    private final List<Equipment> equipment;
    private boolean scrollingEffects = true;
    private int effectsPosition = 0;
    private int equipmentPosition = 0;

    public EntityView(View parentView, WorldEntity entity) {
        super(parentView);
        this.entity = entity;
        this.effects = this.entity.getEffectInstances().stream()
                .sorted(Comparator.comparing(EffectInstance::getDuration)).collect(Collectors.toList());
        this.equipment = this.entity.getEquipment().stream()
                .sorted(Comparator.comparing(Equipment::getLevel)).collect(Collectors.toList());
    }

    @Override
    protected void draw() {
        drawStat(entity.getCurrentStatDescriptor(), entity.getBaseStatDescriptor());
        drawState(entity.getStateDescriptor(), entity.getCurrentStatDescriptor().generateStateDescriptor());
        if (effects.size() >= 1) {
            drawEffects(effects.subList(effectsPosition, effects.size()));
        }
        if (equipment.size() >= 1) {
            drawEquipment(equipment.subList(equipmentPosition, equipment.size()));
        }
    }

    protected final void swapScrolling() {
        scrollingEffects = !scrollingEffects;
        draw();
    }

    public boolean isScrollingEffects() {
        return scrollingEffects;
    }

    protected final void scrollDown() {
        if (scrollingEffects) {
            if (effects.size() >= 1) {
                effectsPosition = Math.min(effectsPosition + 1, effects.size() - 1);
                draw();
            }
        } else {
            if (equipment.size() >= 1) {
                equipmentPosition = Math.min(equipmentPosition + 1, equipment.size() - 1);
                draw();
            }
        }
    }

    protected final void scrollUp() {
        if (scrollingEffects) {
            if (effects.size() >= 1) {
                effectsPosition = Math.max(effectsPosition - 1, 0);
                draw();
            }
        } else {
            if (equipment.size() >= 1) {
                equipmentPosition = Math.max(equipmentPosition - 1, 0);
                draw();
            }
        }
    }

    protected abstract void drawStat(StatDescriptor current, StatDescriptor base);
    protected abstract void drawState(StateDescriptor current, StateDescriptor currentStatDefault);
    protected abstract void drawEffects(List<EffectInstance> effects);
    protected abstract void drawEquipment(List<Equipment> equipment);
}
