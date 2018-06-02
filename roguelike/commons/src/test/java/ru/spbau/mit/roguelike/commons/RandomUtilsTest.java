package ru.spbau.mit.roguelike.commons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RandomUtilsTest {
    @Test
    public void chooseRandom() throws Exception {
        List<Integer> variants = Arrays.asList(1, 2, 3, 4, 5, 15);
        assertTrue(variants.contains(RandomUtils.chooseRandom(variants)));
    }

    @Test
    public void asSumWithoutZeros() throws Exception {
        int source = RandomUtils.getInt(10, 10000);
        List<Integer> terms = RandomUtils.asSumWithoutZeros(source, RandomUtils.getInt(1, source));
        int sum = 0;
        for (int term : terms) {
            sum += term;
            assertTrue(term > 0);
        }
        assertEquals(sum, source);
    }

    @Test
    public void asSum() throws Exception {
        int source = RandomUtils.getInt(10, 10000);
        List<Integer> terms = RandomUtils.asSum(source, RandomUtils.getInt(1, 2 * source));
        int sum = 0;
        for (int term : terms) {
            sum += term;
            assertTrue(term >= 0);
        }
        assertEquals(sum, source);
    }

}