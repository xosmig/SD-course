package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.uicommon.views.View;
import ru.spbau.mit.roguelike.uicommon.views.entity.CharacterEntityView;

import java.util.List;

/**
 * Console player's character view
 */
public class ConsoleCharacterView extends CharacterEntityView {
    private final Terminal terminal;

    public ConsoleCharacterView(View parentView, CharacterEntity entity, Terminal terminal) {
        super(parentView, entity);
        this.terminal = terminal;
    }

    @Override
    protected void drawName(String name) {

    }

    @Override
    protected void drawStat(StatDescriptor current, StatDescriptor base) {

    }

    @Override
    protected void drawState(StateDescriptor current, StateDescriptor currentStatDefault) {

    }

    @Override
    protected void drawEffects(List<EffectInstance> effects) {

    }

    @Override
    protected void drawEquipment(List<Equipment> equipment) {

    }

    @Override
    public void run() {

    }
}
