package ru.spbau.svidchenko.hw01.common;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.exceptions.NoSuchVariableException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SystemInteractionApiTest {
    @Test
    public void setAndGetEnvVariable_anyValue_successfulGet() throws Exception {
        SystemInteractionApi.setEnvVariable("test", "test");
        assertEquals("test", SystemInteractionApi.getEnvVariable("test"));
    }

    @Test(expected = NoSuchVariableException.class)
    public void getEnvVariable_notExist_exception() throws Exception {
        SystemInteractionApi.getEnvVariable("abacada");
    }

    @Test
    public void executeCommand_echoCmd_sameText() throws Exception {
        List<String> result = SystemInteractionApi.executeCommand("ping",
                Collections.singletonList("ya.ru"), Collections.emptyList());
        assertFalse(result.isEmpty());
    }

}