package ru.spbau.mit.roguelike.uicommon.views.inventory;

import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.inventory.Inventory;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.*;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;
import ru.spbau.mit.roguelike.uicommon.views.View;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract inventory view
 */
public abstract class InventoryView extends View {
    private final Inventory inventory;
    private final CharacterEntity entity;
    private final List<Item> inventoryItems;
    private final List<Equipment> playerEquipment;
    private final UseItemVisitor useItemVisitor = new UseItemVisitor();
    private final Runnable onEntityEquipmentChange;
    private int inventoryPosition = 0;
    private int equipmentPosition = 0;
    private boolean scrollingEquipment = true;

    public InventoryView(View parentView, CharacterEntity entity, Runnable onEntityEquipmentChange) {
        super(parentView);
        this.entity = entity;
        inventory = entity.getInventory();
        this.onEntityEquipmentChange = onEntityEquipmentChange;
        this.inventoryItems = inventory.getItems().stream().collect(Collectors.toList());
        this.playerEquipment = entity.getEquipment().stream().collect(Collectors.toList());
    }

    @Override
    protected void draw() {
        drawStat(entity.getCurrentStatDescriptor(), entity.getBaseStatDescriptor());
        drawState(entity.getStateDescriptor(), entity.getCurrentStatDescriptor().generateStateDescriptor());
        if (!inventoryItems.isEmpty()) {
            drawInventory(inventoryItems.subList(inventoryPosition, inventoryItems.size()));
        }
        if (!playerEquipment.isEmpty()) {
            drawEquipment(playerEquipment.subList(equipmentPosition, playerEquipment.size()));
        }
    }

    protected final void useSelectedItem() {
        if (!inventoryItems.isEmpty()) {
            inventoryItems.get(inventoryPosition).accept(useItemVisitor);
        }
        onEntityEquipmentChange.run();
        Set<Equipment> dropedEquipment = playerEquipment.stream().filter(eq -> !entity.getEquipment().contains(eq))
                .collect(Collectors.toSet());
        inventoryItems.addAll(dropedEquipment);
        playerEquipment.removeAll(dropedEquipment);
        equipmentPosition = Math.min(Math.max(0, playerEquipment.size() - 1), equipmentPosition);
        draw();
    }

    protected final void unequipSelectedEquipment() {
        if (playerEquipment.isEmpty()) {
            return;
        }
        Equipment source = playerEquipment.remove(equipmentPosition);
        entity.removeEquipment(source);
        inventoryItems.add(source);
        inventory.addItem(source);
        equipmentPosition = Math.min(equipmentPosition, Math.max(0, playerEquipment.size() - 1));
        onEntityEquipmentChange.run();
        draw();
    }

    protected final void deleteSelectedItem() {
        if (!inventoryItems.isEmpty()) {
            inventory.removeItem(inventoryItems.remove(inventoryPosition));
            inventoryPosition = Math.min(inventoryPosition, Math.max(0, inventoryItems.size() - 1));
        }
        draw();
    }

    protected final void swapScrolling() {
        scrollingEquipment = !scrollingEquipment;
        draw();
    }

    public boolean isScrollingEquipment() {
        return scrollingEquipment;
    }

    protected final void scrollDown() {
        if (scrollingEquipment) {
            if (playerEquipment.size() >= 1) {
                equipmentPosition = Math.min(equipmentPosition + 1, playerEquipment.size() - 1);
                draw();
            }
        } else {
            if (inventoryItems.size() >= 1) {
                inventoryPosition = Math.min(inventoryPosition + 1, inventoryItems.size() - 1);
                draw();
            }
        }
    }

    protected final void scrollUp() {
        if (scrollingEquipment) {
            if (playerEquipment.size() >= 1) {
                equipmentPosition = Math.max(equipmentPosition - 1, 0);
                draw();
            }
        } else {
            if (inventoryItems.size() >= 1) {
                inventoryPosition = Math.max(inventoryPosition - 1, 0);
                draw();
            }
        }
    }

    protected abstract void drawStat(StatDescriptor current, StatDescriptor base);
    protected abstract void drawState(StateDescriptor current, StateDescriptor currentStatDefault);
    protected abstract void drawEquipment(List<Equipment> equipment);
    protected abstract void drawInventory(List<Item> items);

    /**
     * Visitor that performs use action in InventoryView
     */
    private class UseItemVisitor implements ItemVisitor {
        private void processEquipment(Equipment e) {
            entity.addEquipment(e);
            playerEquipment.add(e);
            inventoryItems.remove(e);
            inventory.removeItem(e);
            inventoryPosition = Math.min(inventoryPosition, Math.max(0, inventoryItems.size() - 1));
        }

        @Override
        public void visit(BodyArmor item) {
            processEquipment(item);
        }

        @Override
        public void visit(HandArmor item) {
            processEquipment(item);
        }

        @Override
        public void visit(HeadArmor item) {
            processEquipment(item);
        }

        @Override
        public void visit(LegArmor item) {
            processEquipment(item);
        }

        @Override
        public void visit(Weapon item) {
            processEquipment(item);
        }
    }
}
