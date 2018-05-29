package ru.spbau.svidchenko.hw01.parser;

import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.exceptions.NoSuchVariableException;
import ru.spbau.svidchenko.hw01.exceptions.ParseException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.implementation.*;

import java.util.*;
import java.util.stream.Stream;

public class ParserImpl extends Parser {
    private static final Set<Character> ALLOWED_NAME_CHARACTERS = new HashSet<>();
    static {
        for (char c = 'a'; c < 'z'; c++) {
            ALLOWED_NAME_CHARACTERS.add(c);
        }
        for (char c = 'A'; c < 'Z'; c++) {
            ALLOWED_NAME_CHARACTERS.add(c);
        }
        for (char c = '0'; c < '9'; c++) {
            ALLOWED_NAME_CHARACTERS.add(c);
        }
        ALLOWED_NAME_CHARACTERS.add('-');
        ALLOWED_NAME_CHARACTERS.add('_');
        ALLOWED_NAME_CHARACTERS.add(':');
        ALLOWED_NAME_CHARACTERS.add('/');
        ALLOWED_NAME_CHARACTERS.add('.');
    }
    private static final Character NAME_BREAK_CHARACTER = ' ';

    @Override
    protected String processVariables(String statement) throws ParseException, NoSuchVariableException {
        Queue<Character> queue = new LinkedList<>();
        for (Character c : statement.toCharArray()) {
             queue.add(c);
        }
        boolean inDoubleQuotes = false;
        StringBuilder builder = new StringBuilder();
        while (!queue.isEmpty()) {
            if (queue.element().equals('\\')) {
                queue.remove();
                if (!queue.isEmpty()) {
                    builder.append(queue.poll());
                } else {
                    builder.append('\\');
                }
            }
            if (queue.element().equals('$')) {
                queue.remove();
                builder.append(SystemInteractionApi.getEnvVariable(parseName(queue)));
                continue;
            }
            if (queue.element().equals('\'') && !inDoubleQuotes) {
                builder.append('\'').append(parseStringWithSingleQuotes(queue)).append('\'');
                continue;
            }
            if (queue.element().equals('"')) {
                inDoubleQuotes = !inDoubleQuotes;
            }
            builder.append(queue.poll());
        }
        return builder.toString();
    }

    @Override
    protected Command parse(String statement) throws CliException {
        Queue<Character> queue = new LinkedList<>();
        for (Character c : statement.toCharArray()) {
            queue.add(c);
        }
        List<Command> pipedCommands = new ArrayList<>();
        String name = null;
        List<String> arguments = new ArrayList<>();
        while (!queue.isEmpty()) {
            if (queue.element().equals(' ') || queue.element().equals('\n')) {
                queue.remove();
                continue;
            }
            if (name == null) {
                name = parseName(queue);
                if (name.isEmpty()) {
                    throw new ParseException();
                }
                continue;
            }
            if (queue.element().equals('=')) {
                if (!arguments.isEmpty()) {
                    throw new ParseException();
                }
                queue.remove();
                StringBuilder rightSide = new StringBuilder();
                queue.forEach(rightSide::append);
                pipedCommands.add(new AssignmentCommand(Collections.singletonList(rightSide.toString()), name));
                return zipPipe(pipedCommands);
            }
            if (queue.element().equals('"')) {
                arguments.add(parseStringWithDoubleQuotes(queue));
                continue;
            }
            if (queue.element().equals('\'')) {
                arguments.add(parseStringWithSingleQuotes(queue));
                continue;
            }
            if (queue.element().equals('|')) {
                pipedCommands.add(getCommand(name, arguments));
                arguments = new ArrayList<>();
                name = null;
                queue.poll();
                continue;
            }
            arguments.add(parseRawString(queue));
        }
        if (name == null) {
            throw new ParseException();
        }
        pipedCommands.add(getCommand(name, arguments));
        return zipPipe(pipedCommands);
    }

    protected Command zipPipe(List<Command> pipedCommands) {
        Command right = pipedCommands.get(pipedCommands.size() - 1);
        for (int i = pipedCommands.size() - 2; i > -1; i--) {
            right = new PipelineCommand(Collections.emptyList(), pipedCommands.get(i), right);
        }
        return right;
    }

    protected Command getCommand(String name, List<String> arguments) {
        switch (name) {
            case "echo":
                return new EchoCommand(arguments);
            case "wc":
                return new WcCommand(arguments);
            case "pwd":
                return new PwdCommand(arguments);
            case "cat":
                return new CatCommand(arguments);
            case "exit":
                return new ExitCommand(arguments);
            default:
                return new OutterCommand(arguments, name);
        }

    }

    protected String parseName(Queue<Character> queue) {
        StringBuilder builder = new StringBuilder();
        while (!queue.isEmpty() && ALLOWED_NAME_CHARACTERS.contains(queue.element())) {
            builder.append(queue.poll());
        }
        if (!queue.isEmpty() && queue.element() == NAME_BREAK_CHARACTER) {
            queue.poll();
        }
        return builder.toString();
    }

    protected String parseStringWithDoubleQuotes(Queue<Character> queue) throws ParseException {
        StringBuilder builder = new StringBuilder();
        try {
            queue.remove();
            while (!queue.isEmpty() && !queue.element().equals('"')) {
                builder.append(queue.poll());
            }
            queue.remove();
        } catch (NoSuchElementException e) {
            throw new ParseException();
        }
        return builder.toString();
    }

    protected String parseStringWithSingleQuotes(Queue<Character> queue) throws ParseException {
        StringBuilder builder = new StringBuilder();
        try {
            queue.remove();
            while (!queue.isEmpty() && !queue.element().equals('\'')) {
                builder.append(queue.poll());
            }
            queue.remove();
        } catch (NoSuchElementException e) {
            throw new ParseException();
        }
        return builder.toString();
    }

    protected String parseRawString(Queue<Character> queue) {
        StringBuilder builder = new StringBuilder();
        while (!queue.isEmpty() && !queue.element().equals(' ')
                && !queue.element().equals('\n') && !queue.element().equals('\'') && !queue.element().equals('"')) {
            builder.append(queue.poll());
        }
        return builder.toString();
    }
}
