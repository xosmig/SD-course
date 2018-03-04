package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Assignment command implementation
 * @author ArgentumWalker
 */
public class AssignmentCommand extends Command {
    private String name;

    public AssignmentCommand(List<String> arguments, String name) {
        super(arguments);
        this.name = name;
    }

    @Override
    public CommandOutput execute(List<String> input) {
        if (getArguments().size() != 1) {
            return CommandOutput.interrupt();
        }
        SystemInteractionApi.setEnvVariable(name, getArguments().get(0));
        return CommandOutput.output(Collections.emptyList());
    }
}
