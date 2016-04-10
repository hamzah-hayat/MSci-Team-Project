package com.group.msci.puzzlegenerator.maze.model;

import android.util.Log;

import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.utils.Seed;

import java.util.Random;

/*
 * Composition of BaseMazes.
 */

public class PortalMaze implements Maze {

    private BaseMaze[] planes;
    private int nplanes;
    private int width;
    private int height;
    private int currentPlane;
    private Point3D entry;
    private Point3D exit;

    public PortalMaze(int width, int height, int nplanes, Seed seed) {
        this(width, height, nplanes,
                new Point3D(1, 0, 0),
                new Point3D(width - 2, height - 1, nplanes - 1), seed);
    }

    public PortalMaze(int width, int height, int nplanes, Point3D entry,
                      Point3D exit, Seed seed) {
        Random random = new Random();
        planes = new BaseMaze[nplanes];
        this.currentPlane = entry.z;
        this.width = width;
        this.height = height;
        this.nplanes = nplanes;
        this.entry = entry;
        this.exit = exit;

        for (int i = 0; i < nplanes; ++i) {
            planes[i] = new BaseMaze(width, height, seed);
        }

        /**Random horizontal entry/exit point for the intermediate planes.
         * These will be the 'portals'.
         */
        int lastXpos = entry.x;

        /**Ensure that adjacent planes have horizontally aligned
         * entry and exit points, so that it is possible to move
         * between them.
         */
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
        int currentExit = planes[currentPlane].at(xpos, height - 1);
        int nextEntrance = planes[currentPlane + 1].at(xpos, 0);

        while (currentExit == BaseMaze.WALL_JUNCTION || nextEntrance == BaseMaze.WALL_JUNCTION) {
            xpos = 1 + randomizer.nextInt(width - 2);
            currentExit = planes[currentPlane].at(xpos, height - 1);
            nextEntrance = planes[currentPlane + 1].at(xpos, 0);
        }

        return xpos;
    }

    //Interface methods
    @Override
    public void solve() {
        for (BaseMaze plane : planes) {
            plane.solve();
        }
    }

    @Override
    public boolean solved() {
        for (BaseMaze plane : planes) {
            if (!plane.solved()) return false;
        }
        return true;
    }

    @Override
    public void log() {
        for (int i = 0; i < nplanes; ++i) {
            System.out.println("Plane " + i);
            planes[i].log();
            System.out.println();
        }
    }

    /*No need to retain the player position
     * in the mazes that haven't been visited yet
     * by the player.
     */
    @Override
    public void regenerate() {
        for (int i = 0; i < nplanes; ++i) {
            if (i == currentPlane) {
                planes[i].regenerate(true);
            }
            else {
                planes[i].regenerate(false);
            }
        }
    }

    @Override
    public Point3D playerPos() {
        Point pos = planes[currentPlane].playerPos();
        return new Point3D(pos.x, pos.y, currentPlane);
    }

    @Override
    public boolean movePlayer(int direction) {
        BaseMaze current = planes[currentPlane];
        Point next = BaseMaze.neighbour_at(direction, current.playerPos());
        if (current.withinBounds(next)) {
            planes[currentPlane].movePlayer(direction);
            if (next.equals(current.exit())) {
                ++currentPlane;
            }
            return true;
        }

        return false;
    }

    @Override
    public byte at(Point point) {
        return planes[currentPlane].at(point);
    }

    @Override
    public byte at(int x, int y) {
        return planes[currentPlane].at(x, y);
    }

    @Override
    public int width() {
        return planes[currentPlane].width();
    }

    public Point entryGate() {
        return planes[currentPlane].entryGate();
    }

    public Point exitGate() {
        return planes[currentPlane].exitGate();
    }

    @Override public int height() {
        return planes[currentPlane].height();
    }

    public void logMat() {
        for (int i = 0; i < planes.length; ++i) {
            System.out.println("Plane " + i);
            planes[i].logMat();
            System.out.println();
        }
    }

    @Override
    public Point3D entry() {return entry;}

    @Override
    public Point3D exit() {return exit;}

    @Override
    public boolean isJunction(Point point) {
        return planes[currentPlane].isJunction(point);
    }

    public int getNumberOfPlanes() {
        return nplanes;
    }

    public int getCurrentPlane() {
        return currentPlane;
    }

    public boolean atGate(Point point) {
        return planes[currentPlane].atGate(point);
    }

    @Override
    public boolean isWall(Point point) {
        //Anything outside the existing planes is considered a wall.
        return (currentPlane >= nplanes) || planes[getCurrentPlane()].isWall(point);
    }
}