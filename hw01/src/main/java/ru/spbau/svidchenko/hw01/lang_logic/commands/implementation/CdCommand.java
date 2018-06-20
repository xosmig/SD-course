package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.Collections;
import java.util.List;


public class CdCommand extends Command {

    public CdCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) throws CliException {
        List<String> args = getArguments();
        SystemInteractionApi.changeCurrentDir(args.isEmpty() ? "/" : args.get(0));
        return CommandOutput.output(Collections.emptyList());
    }
}
