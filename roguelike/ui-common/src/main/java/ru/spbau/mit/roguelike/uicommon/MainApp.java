package ru.spbau.mit.roguelike.uicommon;

import ru.spbau.mit.roguelike.uicommon.views.MainMenuView;

/**
 * Abstract main class for UI standalone app
 */
public class MainApp implements Runnable {
    private MainMenuView mainView;

    public MainApp(MainMenuView mainView) {
        this.mainView = mainView;
    }

    public void run() {
        mainView.open();
    }
}
