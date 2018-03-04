package ru.spbau.svidchenko.hw01.lang_logic.commands;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Common output for all commands
 * @author ArgentumWalker
 */
public class CommandOutput {
    private boolean interrupted;
    private boolean exit;
    private List<String> output;
    private String interruptionCause;

    private CommandOutput(boolean interrupted, boolean exit, List<String> output) {
        this.interrupted = interrupted;
        this.output = output;
        this.exit = exit;
    }

    /**
     * Returns output with interruption signal
     */
    public static CommandOutput interrupt() {
        return new CommandOutput(true, false, Collections.emptyList());
    }

    /**
     * Returns output with exit signal
     */
    public static CommandOutput exit() {
        return new CommandOutput(false, true, Collections.emptyList());
    }

    /**
     * Returns output with contained data stream
     */
    public static CommandOutput output(List<String> output) {
        return new CommandOutput(false, false, output);
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public List<String> getOutput() {
        return output;
    }

    public boolean isExit() {
        return exit;
    }
}
