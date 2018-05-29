package ru.spbau.svidchenko.hw01.exceptions;

/**
 * Statement not parsed by any reason
 */
public class ParseException extends CliException {
    @Override
    public String getMessage() {
        return "Parse exception";
    }
}
