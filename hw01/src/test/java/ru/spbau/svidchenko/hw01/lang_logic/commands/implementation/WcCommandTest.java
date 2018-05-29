package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WcCommandTest {

    @Test
    public void wc_empty_correctCounts() throws Exception {
        List<String> result = new WcCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertEquals(3, result.size());
        assertEquals("Word count: 0", result.get(0));
        assertEquals("Line count: 0", result.get(1));
        assertEquals("Byte count: 0", result.get(2));
    }

    @Test
    public void wc_oneLine_correctCounts() throws Exception {
        String testString = "TestTestStringTest";
        List<String> result = new WcCommand(Collections.emptyList()).execute(Collections.singletonList(testString)).getOutput();
        assertEquals(3, result.size());
        assertEquals("Word count: 1", result.get(0));
        assertEquals("Line count: 1", result.get(1));
        assertEquals("Byte count: " + testString.length(), result.get(2));
    }

    @Test
    public void wc_manyLines_correctCounts() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        List<String> result = new WcCommand(Collections.emptyList()).execute(Arrays.asList(testString1, testString2)).getOutput();
        assertEquals(3, result.size());
        assertEquals("Word count: 4", result.get(0));
        assertEquals("Line count: 2", result.get(1));
        assertEquals("Byte count: " + (testString1.length() + testString2.length()), result.get(2));
    }
}