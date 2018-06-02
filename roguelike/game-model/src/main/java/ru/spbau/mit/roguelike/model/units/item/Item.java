package ru.spbau.mit.roguelike.model.units.item;

import ru.spbau.mit.roguelike.commons.Savable;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

/**
 * Abstract item that entity can keep
 */
public abstract class Item implements Savable {
    private final String name;
    private int level;

    public Item(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public abstract void accept(ItemVisitor visitor);
}
