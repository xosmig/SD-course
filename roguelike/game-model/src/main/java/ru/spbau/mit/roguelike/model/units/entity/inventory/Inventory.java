package ru.spbau.mit.roguelike.model.units.entity.inventory;

import ru.spbau.mit.roguelike.model.units.item.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds items that player keep
 */
public class Inventory {
    private final Set<Item> content = new HashSet<>();

    public Inventory addItem(Item item) {
        content.add(item);
        return this;
    }

    public Set<Item> getItems() {
        return content;
    }

    public Inventory removeItem(Item item) {
        content.remove(item);
        return this;
    }
}
