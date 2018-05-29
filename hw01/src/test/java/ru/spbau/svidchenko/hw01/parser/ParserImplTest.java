package ru.spbau.svidchenko.hw01.parser;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.ParseException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.implementation.*;

import java.util.*;

import static org.junit.Assert.*;

public class ParserImplTest extends ParserImpl {
    @Test
    public void processVariables() throws Exception {
        SystemInteractionApi.setEnvVariable("1", "test");
        assertEquals("testtest", processVariables("$1$1"));
        assertEquals("testtest", processVariables("testtest"));
        assertEquals("testtest", processVariables("$1 test"));
        assertEquals("testtest", processVariables("test$1"));
        assertEquals("test\"test\"", processVariables("$1\"$1\""));
        assertEquals("test\'$1\'", processVariables("$1\'$1\'"));
        assertEquals("test$1", processVariables("$1\\$1"));
    }

    @Test
    public void parse() throws Exception {
        SystemInteractionApi.setEnvVariable("1", "test");
        assertTrue(parse("echo hello") instanceof EchoCommand);
        assertTrue(parse("echo hello | wc") instanceof PipelineCommand);
    }

    @Test
    public void zipPipe() throws Exception {
        Command result = zipPipe(Arrays.asList(
                new EchoCommand(Collections.emptyList()),
                new WcCommand(Collections.emptyList()),
                new CatCommand(Collections.emptyList()))
        );
        assertTrue(result instanceof PipelineCommand);
        assertTrue(((PipelineCommand)result).getLeft() instanceof EchoCommand);
        assertTrue(((PipelineCommand)result).getRight() instanceof PipelineCommand);
        assertTrue(((PipelineCommand)((PipelineCommand)result).getRight()).getLeft() instanceof WcCommand);
        assertTrue(((PipelineCommand)((PipelineCommand)result).getRight()).getRight() instanceof CatCommand);
    }

    @Test
    public void getCommand() throws Exception {
        assertTrue(getCommand("wc", Collections.emptyList()) instanceof WcCommand);
        assertTrue(getCommand("pwd", Collections.emptyList()) instanceof PwdCommand);
        assertTrue(getCommand("exit", Collections.emptyList()) instanceof ExitCommand);
        assertTrue(getCommand("echo", Collections.emptyList()) instanceof EchoCommand);
        assertTrue(getCommand("cat", Collections.emptyList()) instanceof CatCommand);
        assertTrue(getCommand("ls", Collections.emptyList()) instanceof OutterCommand);
    }

    @Test
    public void parseStringWithSingleQuotes() throws Exception {
        assertEquals("abacada", parseStringWithSingleQuotes(buildQueue("'abacada'")));
        assertEquals("", parseStringWithSingleQuotes(buildQueue("''")));
        try {
            parseStringWithSingleQuotes(buildQueue("'"));
            fail();
        } catch (ParseException shouldHappen) {}
        try {
            parseStringWithSingleQuotes(buildQueue(""));
            fail();
        } catch (ParseException shouldHappen) {}
    }

    @Test
    public void parseStringWithDoubleQuotes() throws Exception {
        assertEquals("abacada", parseStringWithDoubleQuotes(buildQueue("\"abacada\"")));
        assertEquals("", parseStringWithDoubleQuotes(buildQueue("\"\"")));
        try {
            parseStringWithDoubleQuotes(buildQueue("\""));
            fail();
        } catch (ParseException shouldHappen) {}
        try {
            parseStringWithDoubleQuotes(buildQueue(""));
            fail();
        } catch (ParseException shouldHappen) {}
    }

    @Test
    public void parseRawString() throws Exception {
        assertEquals("abacada", parseRawString(buildQueue("abacada")));
        assertEquals("aba", parseRawString(buildQueue("aba c ada")));
    }

    private Queue<Character> buildQueue(String s) {
        Queue<Character> queue = new LinkedList<>();
        for (Character c : s.toCharArray()) {
            queue.add(c);
        }
        return queue;
    }
}