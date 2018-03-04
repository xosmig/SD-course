package ru.spbau.svidchenko.hw01.common;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void readFrom_emptyStream_emptyList() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        List<String> result = IOUtils.readFrom(input);
        assertTrue(result.isEmpty());
    }

    @Test
    public void readFrom_onelineStream_listWithOneLine() throws Exception {
        String testString = "TestTestStringTest";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        List<String> result = IOUtils.readFrom(input);
        assertEquals(1, result.size());
        assertEquals(testString, result.get(0));
    }

    @Test
    public void readFrom_manylineStream_listWithMAnyLines() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        InputStream input = new ByteArrayInputStream((testString1 + "\n" + testString2).getBytes());
        List<String> result = IOUtils.readFrom(input);
        assertEquals(2, result.size());
        assertEquals(testString1, result.get(0));
        assertEquals(testString2, result.get(1));
    }

    @Test
    public void writeTo_emptyStream_emptyList() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.writeTo(Collections.emptyList(), output);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        List<String> result = IOUtils.readFrom(input);
        assertTrue(result.isEmpty());
    }

    @Test
    public void writeTo_onelineStream_listWithOneLine() throws Exception {
        String testString = "TestTestStringTest";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.writeTo(Collections.singletonList(testString), output);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        List<String> result = IOUtils.readFrom(input);
        assertEquals(1, result.size());
        assertEquals(testString, result.get(0));
    }

    @Test
    public void writeTo_manylineStream_listWithMAnyLines() throws Exception {
        String testString1 = "TestTestStringTest";
        String testString2 = "Test string 2";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.writeTo(Arrays.asList(testString1, testString2), output);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        List<String> result = IOUtils.readFrom(input);
        assertEquals(2, result.size());
        assertEquals(testString1, result.get(0));
        assertEquals(testString2, result.get(1));
    }

}