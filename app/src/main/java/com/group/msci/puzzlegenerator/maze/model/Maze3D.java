package com.group.msci.puzzlegenerator.maze.model;

import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Point;

/**
 * Created by Filipt on 19/01/2016.
 */
public class Maze3D extends BaseMaze {

    /**The Maze 3D will be a cube, and the
     * length parameter specifies the length of
     * the side of one face of the cube. A 2D grid will
     * be used to represent the cube, where regions of the
     * grid will correspond to faces on the cube.
     */
    public Maze3D(int length) {
        super(length * 3, length * 4 + 1, new Point((length * 3) / 2, 0),
                new Point((length * 3) / 2, length * 4));
    }

    /**Override withinBounds to leave out regions
     * of the grid that don't fit onto any face
     * of the cube. So only the faces of the cube get
     * carved. The diagram below demonstrates the
     * regions to be left out.
     *
     *    |F |T| F|
     *    |T |T| T|
     *    |F |T| F|
     *    |F |T| F|
     */
    @Override
    public boolean withinBounds(int x, int y) {
        //Upper left corner or upper right corner
        int length = width() / 3 - 1;
        boolean notMiddleCol = (x < length) || (x > (2 * length));
        return !(notMiddleCol && y < length) &&
                !((y > (2 * length)) && notMiddleCol) &&
                super.withinBounds(x, y);
    }
}