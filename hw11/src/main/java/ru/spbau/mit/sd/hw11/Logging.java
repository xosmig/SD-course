package ru.spbau.mit.sd.hw11;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Simple Logger handler
 */
public final class Logging {
    private final static Logger logger = Logger.getAnonymousLogger();

    static {
        logger.setUseParentHandlers(false);
        try {
            logger.addHandler(new FileHandler("logs"));
        } catch (IOException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }
    }


    public static Logger logger() {
        return logger;
    }
}
