package com.group.msci.puzzlegenerator.maze.model;

/**
 * Created by Filipt on 19/01/2016.
 */
public class Point3D extends Point {

    public int z;

    public Point3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

}