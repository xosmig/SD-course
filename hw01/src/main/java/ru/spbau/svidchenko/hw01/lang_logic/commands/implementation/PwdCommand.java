package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.Collections;
import java.util.List;

/**
 * Pwd command implementation
 * Prints current directory structure
 * @author ArgentumWalker
 */
public class PwdCommand extends Command {
    public PwdCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) {
        String output = SystemInteractionApi.getCurrentDirectory().toString();
        return CommandOutput.output(Collections.singletonList(output));
    }
}
