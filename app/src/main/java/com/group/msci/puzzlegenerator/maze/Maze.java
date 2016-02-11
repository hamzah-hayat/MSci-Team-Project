package com.group.msci.puzzlegenerator.maze;

/**
 * Created by Filipt on 19/01/2016.
 */
public interface Maze {
    void solve();
    void log();
    void regenerate();
    byte at(Point coords);
    Point playerPos();
    boolean movePlayer(int direction);
    boolean solved();
}