package com.group.msci.puzzlegenerator.maze;

import java.util.Random;

/*
 * Composition of 2D Mazes.
 */

public class PortalMaze implements Maze {

    private BaseMaze[] planes;
    private int nplanes;
    private int width;
    private int height;

    public PortalMaze(int width, int height, int nplanes) {
        this(width, height, nplanes,
                new Point3D(1, 1, 0),
                new Point3D(width - 2, height - 1, nplanes - 1));
    }

    public PortalMaze(int width, int height, int nplanes, Point3D entry,
                      Point3D exit) {
        Random random = new Random();
        planes = new BaseMaze[nplanes];
        this.width = width;
        this.height = height;
        this.nplanes = nplanes;

        for (int i = 0; i < nplanes; ++i) {
            planes[i] = new BaseMaze(width, height);
        }

        /**Random horizontal entry/exit point for the intermediate planes.
         * These will be the 'portals'.
         */
        int lastXpos = entry.x;

        /**Ensure that adjacent planes have horizontally aligned
         * entry and exit points, so that it is possible to move
         * between them.
         */
        //TODO: Make sure this works in all cases.
        for (int i = 0; i < nplanes; ++i) {
            /**Make sure no entry/exit point is in the middle of a wall
             * junction which would make it unreachable.
             */
            int xpos = (i != nplanes - 1) ? genXpos(random, i) : exit.x;
            planes[i].setOpenings(new Point(lastXpos, 0), new Point(xpos, height - 1));
            lastXpos = xpos;
        }
    }

    private int genXpos(Random randomizer, int currentPlane) {
        int xpos = 1 + randomizer.nextInt(width - 2);
        while (planes[currentPlane].at(xpos, height - 1 ) == BaseMaze.WALL_JUNCTION ||
                planes[currentPlane + 1].at(xpos, 0) == BaseMaze.WALL_JUNCTION) {
            xpos = 1 + randomizer.nextInt(width - 2);
        }
        return xpos;
    }

    @Override
    public void solve() {
        for (BaseMaze plane : planes) {
            plane.solve();
        }
    }

    @Override
    public void log() {
        for (int i = 0; i < planes.length; ++i) {
            System.out.println("Plane " + i);
            planes[i].log();
            System.out.println();
        }
    }

}