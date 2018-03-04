package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class OutterCommandTest {

    @Test
    public void execute() throws Exception {
        List<String> result = new OutterCommand(Collections.singletonList("test"), "echo")
                .execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }
}