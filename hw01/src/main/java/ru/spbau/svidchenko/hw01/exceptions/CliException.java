package ru.spbau.svidchenko.hw01.exceptions;

/**
 * Exception that contains all other system exceptions
 */
public class CliException extends Exception {
    protected CliException() {}

    public CliException(Exception e) {
        initCause(e);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }
}
