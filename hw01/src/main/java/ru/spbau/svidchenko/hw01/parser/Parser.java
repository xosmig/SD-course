package ru.spbau.svidchenko.hw01.parser;

import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.exceptions.NoSuchVariableException;
import ru.spbau.svidchenko.hw01.exceptions.ParseException;
import ru.spbau.svidchenko.hw01.lang_logic.EvaluationTree;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;

/**
 * Abstract parser for CLI. Firstly process variables and then parse command.
 */
public abstract class Parser {
    public EvaluationTree parseStatement(String statement) throws CliException {
        return new EvaluationTree(parse(processVariables(statement)));
    }

    /**
     * Politics of variable processing could be different
     */
    protected abstract String processVariables(String statement) throws ParseException, NoSuchVariableException;

    /**
     * Parse command with processed variables
     */
    protected abstract Command parse(String statement) throws CliException;
}
