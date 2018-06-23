package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;

import java.util.Collections;

import static org.junit.Assert.*;

public class AssignmentCommandTest {
    @Test
    public void assign_oneVariable_success() throws Exception {
        new AssignmentCommand(Collections.singletonList("aaa"), "a").execute(Collections.emptyList());
        assertEquals("aaa", SystemInteractionApi.getEnvVariable("a"));
    }

    @Test
    public void reassign_oneVariable_success() throws Exception {
        new AssignmentCommand(Collections.singletonList("aaa"), "a").execute(Collections.emptyList());
        new AssignmentCommand(Collections.singletonList("aba"), "a").execute(Collections.emptyList());
        assertEquals("aba", SystemInteractionApi.getEnvVariable("a"));
    }
}
