package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.consoleui.UserInputHolder;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.uicommon.views.View;
import ru.spbau.mit.roguelike.uicommon.views.entity.CreepEntityView;

import java.util.List;

/**
 * View creep on console
 */
public class ConsoleCreepView extends CreepEntityView {
    private final Terminal terminal;
    private final int bottom;
    private final int right;
    private boolean exit = false;

    public ConsoleCreepView(View parentView, CreepEntity entity, Terminal terminal) {
        super(parentView, entity);
        this.terminal = terminal;
        bottom = terminal.getBoundableSize().getRows() - 4;
        right = terminal.getBoundableSize().getColumns();
    }

    @Override
    protected void draw() {
        TerminalUtils.fill(terminal, Position.of(0, 0), Position.of(right - 1, bottom - 1), TextColorFactory.DEFAULT_BACKGROUND_COLOR);
        super.draw();
        terminal.flush();
    }

    @Override
    protected void drawNameAngLevel(String name, int level) {
        TerminalUtils.printNameAndLevel(terminal, 0, name, level);
    }

    @Override
    protected void drawStat(StatDescriptor current, StatDescriptor base) {
        TerminalUtils.printStat(terminal, 0, current, base);
    }

    @Override
    protected void drawState(StateDescriptor current, StateDescriptor currentStatDefault) {
        TerminalUtils.printState(terminal, 0, current, currentStatDefault);
    }

    @Override
    protected void drawEffects(List<EffectInstance> effects) {
        int p = 0;
        for (EffectInstance instance : effects) {
            p += TerminalUtils.printEffectWithDuration(terminal, Position.of(45, 7 + p), instance.getEffect(), instance.getDuration());
            if (p > bottom - 9) {
                break;
            }
        }
    }

    @Override
    protected void drawEquipment(List<Equipment> equipment) {
        if (!equipment.isEmpty()) {
            TerminalUtils.printItem(terminal, Position.of(0, 7), equipment.get(0));
        }
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                char key = UserInputHolder.poll(terminal);
                switch (key) {
                    case 'w': {
                        scrollUp();
                        break;
                    }
                    case 'a': {
                        swapScrolling();
                        break;
                    }
                    case 'd': {
                        swapScrolling();
                        break;
                    }
                    case 's': {
                        scrollDown();
                        break;
                    }
                    case 'q': {
                        exit = true;
                    }
                }
            } catch (InterruptedException e) {
                //TODO log
            }
        }
    }
}
