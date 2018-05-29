package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.List;

/**
 * Pipeline command implementation
 * @author ArgentumWalker
 */
public class PipelineCommand extends Command {
    private final Command left;
    private final Command right;

    public PipelineCommand(List<String> arguments, Command leftCommand, Command rightCommand) {
        super(arguments);
        left = leftCommand;
        right = rightCommand;
    }

    @Override
    public CommandOutput execute(List<String> input) throws CliException {
        CommandOutput first = left.execute(input);
        if (first.isInterrupted() || first.isExit()) {
            return first;
        }
        return right.execute(first.getOutput());
    }

    public Command getLeft() {
        return left;
    }

    public Command getRight() {
        return right;
    }
}
