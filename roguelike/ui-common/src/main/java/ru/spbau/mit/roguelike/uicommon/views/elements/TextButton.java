package ru.spbau.mit.roguelike.uicommon.views.elements;

/**
 * Button with text on it
 */
public abstract class TextButton implements Button {
    private final String text;

    protected TextButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
