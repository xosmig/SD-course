package ru.spbau.svidchenko.hw01.exceptions;

import java.io.IOException;

public class IOUtilsException extends RuntimeException {
    private final IOException cause;

    public IOUtilsException(IOException cause) {
        this.cause = cause;
    }

    @Override
    public IOException getCause() {
        return cause;
    }
}
