package ru.spbau.mit.roguelike.commons.logging;

import ru.spbau.mit.roguelike.commons.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging utils. Logs everything that happens in the game
 */
public final class Logging {
    //private final static AtomicInteger ID_PROVIDER = new AtomicInteger();
    //private final static Map<Integer, Logger> id2logger = new HashMap<>();
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
