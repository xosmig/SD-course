package ru.spbau.svidchenko.hw01.common;

import ru.spbau.svidchenko.hw01.exceptions.IOUtilsException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains methods to write Stream of string to OutputStream and to read stream of strings from InputStream
 * @author ArgentumWalker
 */
public final class IOUtils {
    public static List<String> readFrom(InputStream input) {
        return new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.toList());
    }

    public static void writeTo(List<String> stringStream, OutputStream output) {
        Writer writer = new OutputStreamWriter(output);
        stringStream.forEach(
                str -> {
                    try {
                        writer.write(str);
                        writer.write("\n");
                        writer.flush();
                    } catch (IOException e) {
                        throw new IOUtilsException(e);
                    }
                });
    }
}
