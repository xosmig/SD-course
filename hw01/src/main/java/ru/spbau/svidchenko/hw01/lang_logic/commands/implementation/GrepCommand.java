package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParser;
import com.google.devtools.common.options.OptionsParsingException;
import ru.spbau.svidchenko.hw01.common.IOUtils;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of Grep command for CLI
 * Supports -i, -w and -A keys.
 * Usage: grep KEYS PATTERN [FILE]
 * @author ArgentumWalker
 */
public class GrepCommand extends Command {
    public GrepCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) throws CliException {
        OptionsParser parser = OptionsParser.newOptionsParser(GrepOptions.class);
        try {
            parser.parse(getArguments());
        } catch (OptionsParsingException e) {
            throw new CliException(e);
        }
        List<String> residue = parser.getResidue();
        GrepOptions options = parser.getOptions(GrepOptions.class);
        if (options == null || residue.size() > 2 || residue.size() == 0) {
            throw new CliException(new NullPointerException());
        }
        if (residue.size() == 2) {
            input = IOUtils.readFrom(SystemInteractionApi.getFile(residue.get(1)));
        }
        List<String> result = new ArrayList<>();
        int forcePrint = 0;
        Pattern pattern = Pattern.compile(residue.get(0), options.i ? Pattern.CASE_INSENSITIVE : 0);
        for (String s : input) {
            boolean matched = false;
            if (options.w) {
                String[] splited = s.split("\\W+");
                for (String ss : splited) {
                    matched = matched || pattern.matcher(ss).matches();
                }
            } else {
                matched = pattern.matcher(s).find();
            }
            if (matched) {
                forcePrint = options.A;
                result.add(s);
            } else {
                if (forcePrint > 0) {
                    forcePrint--;
                    result.add(s);
                }
            }
        }
        return CommandOutput.output(result);
    }

    public static class GrepOptions extends OptionsBase {
        @Option(
                name = "i",
                abbrev = 'i',
                defaultValue = "false"
        )
        public boolean i;

        @Option(
                name="words",
                abbrev = 'w',
                defaultValue = "false"
        )
        public boolean w;

        @Option(
                name="A",
                abbrev = 'A',
                defaultValue = "0"
        )
        public int A;
    }
}
