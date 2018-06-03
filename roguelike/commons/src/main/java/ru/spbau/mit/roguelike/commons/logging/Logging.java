package ru.spbau.mit.roguelike.commons.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging utils. Logs everything that happens in the game
 */
public final class Logging {
    //TODO logger by id
    private final static Logger logger = Logger.getAnonymousLogger();
    static {
        logger.setUseParentHandlers(false);
    }

    public static void log(Event event) {
        logger.info(event.where() + ": " + event.getMessage());
    }

    public static void log(String text) {
        logger.info(text);
    }

    public static void log(Exception e) {
        logger.log(Level.SEVERE, e, () -> "");
    }

    public static void addHandler(Handler handler) {
        logger.addHandler(handler);
    }
}
