package ru.spbau.mit.roguelike.uicommon.views;

import ru.spbau.mit.roguelike.uicommon.views.elements.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple list menu view
 */
public abstract class ListMenu<T extends Button> extends View {
    private final List<T> buttons = new ArrayList<T>();
    private int currentButton = 0;

    public ListMenu(View parentView) {
        super(parentView);
    }

    @Override
    public final void draw() {
        draw(buttons.get(currentButton));
    }

    public ListMenu<T> addButton(T button) {
        buttons.add(button);
        return this;
    }

    public void click() {
        buttons.get(currentButton).onClick();
        draw();
    }

    public void left() {
        currentButton = Math.max(0, currentButton - 1);
        draw();
    }

    public void right() {
        currentButton = Math.min(buttons.size() - 1, currentButton + 1);
        draw();
    }

    protected abstract void draw(T b);
}
