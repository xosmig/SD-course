package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.consoleui.UserInputHolder;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.uicommon.views.View;
import ru.spbau.mit.roguelike.uicommon.views.inventory.InventoryView;

import java.util.List;

/**
 * Console inventory viewer
 */
public class ConsoleInventory extends InventoryView {
    private final Terminal terminal;
    private final int bottom;
    private final int right;
    private final int level;
    private final String name;
    private boolean exit = false;

    public ConsoleInventory(View parentView, CharacterEntity entity, Runnable onEntityEquipmentChange, Terminal terminal) {
        super(parentView, entity, onEntityEquipmentChange);
        this.terminal = terminal;
        level = entity.getLevel();
        name = entity.getName();
        bottom = terminal.getBoundableSize().getRows();
        right = terminal.getBoundableSize().getColumns();
    }


    @Override
    protected void draw() {
        TerminalUtils.fill(terminal, Position.of(0, 0), Position.of(right - 1, bottom - 1), TextColorFactory.DEFAULT_BACKGROUND_COLOR);
        drawGUIBorders();
        drawNameAngLevel(name, level);
        super.draw();
        terminal.flush();
    }

    protected void drawGUIBorders() {
        TerminalUtils.printDesignations(terminal, 0);
        TerminalUtils.verticalLine(terminal,
                Position.of(right - TerminalUtils.STAT_START_FROM_RIGHT - 1, 5),
                bottom - 4);
        TerminalUtils.horizontalLine(terminal, Position.of(0, 4), right);
        if (!isScrollingEquipment()) {
            terminal.setForegroundColor(TextColorFactory.fromRGB(200, 200, 0));
        }
        TerminalUtils.printText("Inventory", terminal, Position.of(1, 5));
        if (!isScrollingEquipment()) {
            terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        } else {
            terminal.setForegroundColor(TextColorFactory.fromRGB(200, 200, 0));
        }
        TerminalUtils.printText("Equipment", terminal, Position.of(right - TerminalUtils.STAT_START_FROM_RIGHT + 1, 5));
        terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
    }

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
    protected void drawEquipment(List<Equipment> equipment) {
        if (!equipment.isEmpty()) {
            TerminalUtils.printItem(terminal, Position.of(45, 7), equipment.get(0));
        }
    }

    @Override
    protected void drawInventory(List<Item> items) {
        if (!items.isEmpty()) {
            TerminalUtils.printItem(terminal, Position.of(0, 7), items.get(0));
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
                    case ' ': {
                        if (!isScrollingEquipment()) {
                            useSelectedItem();
                        }
                        break;
                    }
                    case 'e': {
                        if (isScrollingEquipment()) {
                            unequipSelectedEquipment();
                        } else {
                            deleteSelectedItem();
                        }
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
