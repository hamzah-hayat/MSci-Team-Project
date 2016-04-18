package com.group.msci.puzzlegenerator.maze.models;

/**
 * Created by Filipt on 19/01/2016.
 */
public class Point3D extends Point {

    public int z;

    public Point3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public Point3D(Point p, int z) {
        super(p.x, p.y);
        this.z = z;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Point3D) {
            Point3D otherp = (Point3D) other;
            return (otherp.x == this.x) &&
                    (otherp.y == this.y) &&
                    (otherp.z == this.z);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d]", x, y, z);
    }

}