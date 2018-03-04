package ru.spbau.svidchenko.hw01.lang_logic.commands;

import ru.spbau.svidchenko.hw01.exceptions.CliException;

import java.util.List;

/**
 * Interface for all commands in CLI
 * @author ArgentumWalker
 */
public abstract class Command {
    private final List<String> arguments;

    public Command(List<String> arguments) {
        this.arguments = arguments;
    }

    public abstract CommandOutput execute(List<String> input) throws CliException;

    protected List<String> getArguments() {
        return arguments;
    }
}
