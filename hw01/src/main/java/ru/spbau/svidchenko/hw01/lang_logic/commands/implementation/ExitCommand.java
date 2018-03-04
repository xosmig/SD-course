package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.List;

/**
 * Exit from CLI
 * @author ArgentumWalker
 */
public class ExitCommand extends Command {
    public ExitCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) {
        return CommandOutput.exit();
    }
}
