package ru.spbau.mit.roguelike.consoleui;

import org.codetome.zircon.api.Size;
import org.codetome.zircon.api.builder.TerminalBuilder;
import org.codetome.zircon.api.input.InputType;
import org.codetome.zircon.api.resource.GraphicTilesetResource;
import org.codetome.zircon.api.terminal.Terminal;
import org.codetome.zircon.internal.font.impl.PickFirstMetaStrategy;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.consoleui.views.MainMenu;
import ru.spbau.mit.roguelike.uicommon.MainApp;
import sun.rmi.runtime.Log;

import java.io.IOException;

/**
 * Main that runs game in console
 */
public class Main extends MainApp {
    private static Terminal terminal = TerminalBuilder.newBuilder().initialTerminalSize(Size.of(87, 32)).build();

    public Main() {
        super(new MainMenu(terminal));
    }

    public static void main(String[] args) {
        UserInputHolder.register(terminal);
        new Main().run();
        UserInputHolder.unregister(terminal);
        try {
            terminal.close();
        } catch (IOException e) {
            Logging.log(e);
        }
    }
}
