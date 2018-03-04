package ru.spbau.svidchenko.hw01.exceptions;

public class ParseException extends CliException {
    @Override
    public String getMessage() {
        return "Parse exception";
    }
}
