package ru.spbau.mit.roguelike.consoleui;

import org.codetome.zircon.api.input.InputType;
import org.codetome.zircon.api.terminal.Terminal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holds user's key input. Can hold char input only
 */
public final class UserInputHolder {
    private static final Map<Terminal, BlockingQueue<Character>> terminal2input = new HashMap<>();

    public static void register(Terminal terminal) {
        terminal2input.put(terminal, new LinkedBlockingQueue<>());
        terminal.onInput(input -> {
            if (input.isKeyStroke() && input.asKeyStroke().inputTypeIs(InputType.Character)) {
                UserInputHolder.put(terminal, input.asKeyStroke().getCharacter());
            }
        });
    }

    public static char poll(Terminal terminal) throws InterruptedException {
        return terminal2input.get(terminal).take();
    }

    public static void put(Terminal terminal, char input) {
        terminal2input.get(terminal).add(input);
    }

    public static void clear(Terminal terminal) {
        terminal2input.get(terminal).clear();
    }

    public static void unregister(Terminal terminal) {
        terminal.onInput(input -> {});
        terminal2input.remove(terminal);
    }
}
