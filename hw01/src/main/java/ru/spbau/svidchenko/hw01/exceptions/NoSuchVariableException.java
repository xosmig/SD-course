package ru.spbau.svidchenko.hw01.exceptions;

/**
 * This exception means, that user trying to call variable that do not exist
 */
public class NoSuchVariableException extends CliException {
    private final String name;

    public NoSuchVariableException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Variable " + name + " do not exist";
    }
}
