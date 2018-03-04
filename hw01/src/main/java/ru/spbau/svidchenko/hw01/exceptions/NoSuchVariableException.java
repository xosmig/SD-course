package ru.spbau.svidchenko.hw01.exceptions;

/**
 * This exception means, that user trying to call variable that do not exist
 */
public class NoSuchVariableException extends CliException {
    @Override
    public String getMessage() {
        return "Variable do not exist";
    }
}
