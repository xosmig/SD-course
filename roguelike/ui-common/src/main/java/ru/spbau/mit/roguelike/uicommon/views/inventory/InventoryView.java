package ru.spbau.mit.roguelike.uicommon.views.inventory;

import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.inventory.Inventory;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.*;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;
import ru.spbau.mit.roguelike.uicommon.views.View;

import java.util.List;
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
    private int inventoryPosition = 0;
    private int equipmentPosition = 0;
    private boolean scrollingEquipment = true;

    public InventoryView(View parentView, CharacterEntity entity) {
        super(parentView);
        this.entity = entity;
        inventory = entity.getInventory();
        this.inventoryItems = inventory.getItems().stream().collect(Collectors.toList());
        this.playerEquipment = entity.getEquipment().stream().collect(Collectors.toList());
    }

    @Override
    protected void draw() {
        drawInventory(inventoryItems);
        drawEquipment(playerEquipment);
    }

    protected final void useSelectedItem() {
        if (!inventoryItems.isEmpty()) {
            inventoryItems.get(inventoryPosition).accept(useItemVisitor);
        }
    }

    protected final void unequipSelectedEquipment() {
        if (playerEquipment.isEmpty()) {
            return;
        }
        Equipment source = playerEquipment.remove(equipmentPosition);
        entity.removeEquipment(source);
        inventoryItems.add(source);
        inventory.addItem(source);
        equipmentPosition = Math.max(equipmentPosition, Math.min(0, playerEquipment.size() - 1));
        draw();
    }

    protected final void deleteSelectedItem() {
        if (!inventoryItems.isEmpty()) {
            inventory.removeItem(inventoryItems.remove(inventoryPosition));
            inventoryPosition = Math.max(inventoryPosition, Math.min(0, inventoryItems.size() - 1));
        }
    }

    protected final void swapScrolling() {
        scrollingEquipment = !scrollingEquipment;
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


    protected abstract void drawEquipment(List<Equipment> equipment);
    protected abstract void drawInventory(List<Item> items);

    /**
     * Visitor that performs use action in InventoryView
     */
    private class UseItemVisitor implements ItemVisitor {
        private void processEquipment(Equipment e) {
            entity.addEquipment(e);
            inventoryItems.remove(e);
            inventory.removeItem(e);
            inventoryPosition = Math.max(inventoryPosition, Math.min(0, inventoryItems.size()));
            draw();
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
