package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List directory contents
 */
public class LsCommand extends Command {

    public LsCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) throws CliException {
        final List<String> args = getArguments();
        final List<String> output = new ArrayList<>();

        if (args.isEmpty()) {
            printDirectories("", output);
        } else if (args.size() == 1) {
            printDirectories(args.get(0), output);
        } else {
            for (Iterator<String> it = args.iterator(); it.hasNext();) {
                String dir = it.next();
                output.add(dir + ":");
                printDirectories(dir, output);

                if (it.hasNext()) {
                    output.add("");
                }
            }
        }

        return CommandOutput.output(output);
    }

    private void printDirectories(String path, List<String> output) throws CliException {
        output.addAll(SystemInteractionApi.getFilesList(path));
    }
}
