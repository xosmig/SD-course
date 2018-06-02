package ru.spbau.mit.roguelike.commons.logging;

import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.Savable;

/**
 * Any event that can be logged
 */
public class Event implements Savable {
    private final Point where;
    private final String message;

    public Event(Point where, String message) {
        this.where = where;
        this.message = message;
    }

    public Point where() {
        return where;
    }

    public String getMessage() {
        return message;
    }
}
