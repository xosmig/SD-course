package ru.spbau.mit.roguelike.consoleui;

import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Holder for log. Reformat messages to log width-height parameters
 */
public class LoggingHolder extends Handler {
    private final int width;
    private final int height;
    private final LinkedList<String> messageRows = new LinkedList<>();

    public LoggingHolder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public List<String> getLog() {
        return messageRows;
    }

    public void add(String s) {
        process(s);
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().equals(Level.INFO)) {
            process(record.getMessage());
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}

    private void process(String s) {
        List<String> words = Arrays.asList(s.split(" +"));
        List<String> rows = new ArrayList<>();
        StringBuilder currentRow = new StringBuilder();
        for (String word : words) {
            if (currentRow.length() + 1 + word.length() > this.width) {
                rows.add(currentRow.toString());
                currentRow = new StringBuilder();
            }
            if (currentRow.length() != 0) {
                currentRow.append(' ');
            }
            currentRow.append(word);
        }
        rows.add(currentRow.toString());
        rows.add("");
        Collections.reverse(rows);
        for (String row : rows) {
            messageRows.addFirst(row);
        }
        while (messageRows.size() > height) {
            messageRows.removeLast();
        }
    }
}
