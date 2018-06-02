package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.input.KeyStroke;
import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.consoleui.LoggingHolder;
import ru.spbau.mit.roguelike.consoleui.UserInputHolder;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.uicommon.views.MainMenuView;
import ru.spbau.mit.roguelike.uicommon.views.elements.TextButton;

/**
 * Console main menu
 */
public class MainMenu extends MainMenuView {
    private final Terminal terminal;
    private boolean exit = false;

    public MainMenu(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                char key = UserInputHolder.poll(terminal);
                switch (key) {
                    case 'w': {
                        left();
                        break;
                    }
                    case 's': {
                        right();
                        break;
                    }
                    case ' ': {
                        click();
                        break;
                    }
                }
            } catch (InterruptedException dontCare) {}
        }
    }

    @Override
    public void startGame(CharacterEntity player) {
        new ConsoleGame(this, player, terminal).open();
    }

    @Override
    public void exit() {
        exit = true;
    }

    @Override
    protected void draw(TextButton b) {
        terminal.clear();
        TerminalUtils.printText(b.getText(), terminal, Position.of(
                (terminal.getBoundableSize().getColumns() - b.getText().length()) / 2,
                terminal.getBoundableSize().getRows() / 2));
        terminal.flush();
    }
}
