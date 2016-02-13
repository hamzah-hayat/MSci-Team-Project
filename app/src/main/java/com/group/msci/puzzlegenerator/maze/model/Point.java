package com.group.msci.puzzlegenerator.maze.model;

/**
 * Created by Filipt on 11/01/2016.
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("[%d,%d]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof  Point) &&
                (((Point) o).y == this.y) &&
                (((Point) o).x == this.x);
    }
}