package ru.spbau.svidchenko.hw01.lang_logic;

import ru.spbau.svidchenko.hw01.common.IOUtils;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

/**
 * Evaluation tree for each language statement
 * @author ArgentumWalker
 */
public class EvaluationTree {
    private final Command rootNode;

    public EvaluationTree(Command root) {
        rootNode = root;
    }

    /**
     * Executes command with specified input-output streams
     * @return is CLI interrupted
     */
    public boolean execute(InputStream input, OutputStream output) throws CliException {
        CommandOutput result = rootNode.execute(Collections.emptyList());
        if (result.isExit()) {
            return true;
        }
        if (result.isInterrupted()) {
            return false;
        }
        try {
            IOUtils.writeTo(result.getOutput(), output);
        } catch (IOException e) {
            throw new CliException(e);
        }
        return false;
    }
}
