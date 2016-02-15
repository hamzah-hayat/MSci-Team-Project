package com.group.msci.puzzlegenerator.maze;

import android.graphics.Canvas;

import com.group.msci.puzzlegenerator.maze.model.Point;

/**
 * Created by Filipt on 19/01/2016.
 */
public interface Maze {

    //Maze methods
    void solve();
    void log();
    void regenerate();
    boolean movePlayer(int direction);

    //Accessors
    byte at(Point coords);
    byte at(int x, int y);
    Point playerPos();
    boolean solved();
    int height();
    int width();
    Point entry();
    Point exit();
    boolean isJunction(Point point);
}