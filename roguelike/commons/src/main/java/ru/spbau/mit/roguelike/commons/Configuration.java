package ru.spbau.mit.roguelike.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration holder. Holds all changeable information about the world.
 * Symbol % used for split set and list elements
 */
public final class Configuration implements Savable {
    private static final HashMap<String, String> key2entry = new HashMap<>();

    public static void clear() {
        key2entry.clear();
    }

    public static void add(String key, String value) {
        key2entry.put(key, value);
    }

    public static String get(String key) {
        return key2entry.get(key);
    }

    public static int getInt(String key) {
        return Integer.valueOf(key2entry.get(key));
    }

    public static double getDouble(String key) {
        return Double.valueOf(key2entry.get(key));
    }

    public static Set<String> getSet(String key) {
        return Stream.of(key2entry.get(key).split("%")).collect(Collectors.toSet());
    }

    public static List<String> getList(String key) {
        return Stream.of(key2entry.get(key).split("%")).collect(Collectors.toList());
    }

    public static void addFromStream(InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);
        for (String key : properties.stringPropertyNames()) {
            add(key, properties.getProperty(key));
        }
    }
}
