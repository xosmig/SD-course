package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.io.IOException;
import java.util.List;

/**
 * Request of executing outter command
 * @author ArgentumWalker
 */
public class OutterCommand extends Command {
    private String command;

    public OutterCommand(List<String> arguments, String command) {
        super(arguments);
        this.command = command;
    }

    @Override
    public CommandOutput execute(List<String> input) throws CliException {
        try {
            return CommandOutput.output(SystemInteractionApi.executeCommand(command, getArguments(), input));
        } catch (IOException e) {
            return CommandOutput.interrupt();
        }
    }
}
