package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Echo command implementation
 * @author ArgentumWalker
 */
public class EchoCommand extends Command {
    public EchoCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) {
        return CommandOutput.output(new ArrayList<>(getArguments()));
    }
}
