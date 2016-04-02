package com.group.msci.puzzlegenerator.maze;

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
    Point entryGate();
    Point exitGate();
    boolean isJunction(Point point);
    boolean isWall(Point point);
    boolean atGate(Point point);

    int getCurrentPlane();
    int getNumberOfPlanes();


    //Timer
}