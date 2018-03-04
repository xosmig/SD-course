package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PipelineCommandTest {

    @Test
    public void pipeline_echoAndWc_success() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        Command echo = new EchoCommand(Arrays.asList(testString1, testString2));
        Command wc = new WcCommand(Collections.emptyList());
        List<String> result = new PipelineCommand(Collections.emptyList(), echo, wc)
                .execute(Collections.emptyList()).getOutput();
        assertEquals(3, result.size());
        assertEquals("Word count: 4", result.get(0));
        assertEquals("Line count: 2", result.get(1));
        assertEquals("Byte count: " + Character.BYTES * (testString1.length() + testString2.length()), result.get(2));
    }

    @Test
    public void pipeline_echoWcWc_success() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        Command echo = new EchoCommand(Arrays.asList(testString1, testString2));
        Command wc1 = new WcCommand(Collections.emptyList());
        Command wc2 = new WcCommand(Collections.emptyList());
        List<String> result = new PipelineCommand(Collections.emptyList(),
                new PipelineCommand(Collections.emptyList(), echo, wc1),
                wc2).execute(Collections.emptyList()).getOutput();
        assertEquals(3, result.size());
        assertEquals("Word count: 9", result.get(0));
        assertEquals("Line count: 3", result.get(1));
        assertEquals("Byte count: " + Character.BYTES * 40, result.get(2));
    }
}