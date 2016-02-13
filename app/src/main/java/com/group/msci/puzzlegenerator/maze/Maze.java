package com.group.msci.puzzlegenerator.maze;

import com.group.msci.puzzlegenerator.maze.model.Point;

/**
 * Created by Filipt on 19/01/2016.
 */
public interface Maze {
    void solve();
    void log();
    void regenerate();
    byte at(Point coords);
    byte at(int x, int y);
    Point playerPos();
    boolean movePlayer(int direction);
    boolean solved();
    int height();
    int width();
}