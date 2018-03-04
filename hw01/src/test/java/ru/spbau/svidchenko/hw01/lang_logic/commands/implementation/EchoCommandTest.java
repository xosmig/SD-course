package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class EchoCommandTest {

    @Test
    public void echo_empty_empty() throws Exception {
        List<String> result = new EchoCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertTrue(result.isEmpty());
    }

    @Test
    public void echo_oneLine_sameLine() throws Exception {
        String testString = "TestTestStringTest";
        List<String> result = new EchoCommand(Collections.singletonList(testString)).execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertEquals(testString, result.get(0));
    }

    @Test
    public void echo_manyLines_sameLines() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        List<String> result = new EchoCommand(Arrays.asList(testString1, testString2)).execute(Collections.emptyList()).getOutput();
        assertEquals(2, result.size());
        assertEquals(testString1, result.get(0));
        assertEquals(testString2, result.get(1));
    }
}