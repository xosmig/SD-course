package ru.spbau.mit.roguelike.commons;

/**
 * Simple point with decimal coordinates
 */
public class Point implements Savable {
    private final int x;
    private final int y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return super.equals(obj);
        }
        return ((Point) obj).getX() == getX() && ((Point) obj).getY() == getY();
    }

    @Override
    public int hashCode() {
        return 16127 * x + y;
    }
}
