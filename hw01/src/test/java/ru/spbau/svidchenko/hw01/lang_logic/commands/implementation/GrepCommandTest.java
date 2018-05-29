package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GrepCommandTest {
    @Test
    public void grep_simpleTest_oneString() throws Exception {
        assertArrayEquals(
                Arrays.asList("kek").toArray(),
                new GrepCommand(Arrays.asList("kek"))
                        .execute(Arrays.asList("kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kek").toArray(),
                new GrepCommand(Arrays.asList("k.k"))
                        .execute(Arrays.asList("kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kek").toArray(),
                new GrepCommand(Arrays.asList("k\\wk"))
                        .execute(Arrays.asList("kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
    }

    @Test
    public void grep_caseSensitivityTest_oneString() throws Exception {
        assertArrayEquals(
                Arrays.asList("kEk").toArray(),
                new GrepCommand(Arrays.asList("kEk"))
                        .execute(Arrays.asList("kEk", "kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kEk", "kek").toArray(),
                new GrepCommand(Arrays.asList("k.k"))
                        .execute(Arrays.asList("kEk", "kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kEk", "kek").toArray(),
                new GrepCommand(Arrays.asList("-i", "kEk"))
                        .execute(Arrays.asList("kEk", "kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
    }

    @Test
    public void grep_wholeWordTest_oneString() throws Exception {
        assertArrayEquals(
                Arrays.asList("kek", "kekek").toArray(),
                new GrepCommand(Arrays.asList("kek"))
                        .execute(Arrays.asList("kek", "kekek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kek").toArray(),
                new GrepCommand(Arrays.asList("-w", "kek"))
                        .execute(Arrays.asList("kek", "kekek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
        assertArrayEquals(
                Arrays.asList("kek", "kekek").toArray(),
                new GrepCommand(Arrays.asList("-w", "k(ek)+"))
                        .execute(Arrays.asList("kek", "kekek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
    }

    @Test
    public void grep_multilinePrintTest_oneString() throws Exception {
        assertArrayEquals(
                Arrays.asList("kek", "rofl", "lol", "nerf").toArray(),
                new GrepCommand(Arrays.asList("-A", "3", "kek"))
                        .execute(Arrays.asList("kek", "rofl", "lol", "nerf", ""))
                        .getOutput().toArray()
        );
    }
}