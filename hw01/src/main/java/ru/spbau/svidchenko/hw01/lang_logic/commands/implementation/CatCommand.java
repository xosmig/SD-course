package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.IOUtils;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Cat file command
 * Returns output with data from file
 * @author ArgentumWalker
 */
public class CatCommand extends Command {
    public CatCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) {
        List<String> args = getArguments();
        if (args.size() > 1) {
            return CommandOutput.interrupt();
        }
        if (args.size() == 1) {
            try (InputStream file = SystemInteractionApi.getFile(args.get(0))) {
                return CommandOutput.output(IOUtils.readFrom(file));
            } catch (CliException | IOException e) {
                return CommandOutput.interrupt();
            }
        }
        return CommandOutput.output(input);
    }
}
