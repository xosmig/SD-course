package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class ExitCommandTest {

    @Test
    public void execute() throws Exception {
        assertTrue(new ExitCommand(Collections.emptyList()).execute(Collections.emptyList()).isExit());
    }
}