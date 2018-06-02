package ru.spbau.mit.roguelike.uicommon.views;

/**
 * Any kind of view
 * Method run should receive and resolve user's input until user want to exit
 */
public abstract class View implements Runnable {
    private final View parentView;

    public View(View parentView) {
        this.parentView = parentView;
    }

    public final void open() {
        draw();
        run();
        close();
    }

    protected abstract void draw();

    public final void close() {
        if (parentView != null) {
            parentView.open();
        }
    }
}
