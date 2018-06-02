package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
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

    public ConsoleInventory(View parentView, CharacterEntity entity, Terminal terminal) {
        super(parentView, entity);
        this.terminal = terminal;
    }

    @Override
    protected void drawEquipment(List<Equipment> equipment) {

    }

    @Override
    protected void drawInventory(List<Item> items) {

    }

    @Override
    public void run() {

    }
}
