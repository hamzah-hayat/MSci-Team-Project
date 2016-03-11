package com.group.msci.puzzlegenerator.maze.model;

import android.util.SparseArray;

import com.group.msci.puzzlegenerator.maze.Maze;

import java.util.*;

/**
 * Created by filipt on 15/01/2016.
 */
public class BaseMaze implements Maze {

    public static final int EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3;
    public static final byte PATH = 2, SPACE = 1, WALL = 0, WALL_JUNCTION = -1, HORIZONTAL_WALL = -2, VERTICAL_WALL = -3;

    public static final SparseArray<Integer> opposite = new SparseArray<>(4);
    private static final SparseArray<Point> neighbours = new SparseArray<>(4);

    static {
        opposite.put(EAST, WEST);
        opposite.put(SOUTH, NORTH);
        opposite.put(WEST, EAST);
        opposite.put(NORTH, SOUTH);

        neighbours.put(EAST, new Point(1,0));
        neighbours.put(SOUTH, new Point(1,0));
        neighbours.put(WEST, new Point(1,0));
        neighbours.put(NORTH, new Point(1,0));
    }

    private static final List<Integer>
            DIRECTIONS = Arrays.asList(EAST, SOUTH, WEST, NORTH);

    private int height;
    private int width;
    private byte[][] grid;
    private boolean solved;
    private Point entry;
    private Point exit;
    private Point playerPos;

    public BaseMaze(int width, int height, Point entry, Point exit) {
        this(width, height);
        setOpenings(entry, exit);
    }

    public BaseMaze(int width, int height) {
        this.height = height;
        this.width = width;
        solved = false;

        grid = new byte[height][width];
        int carveStart = (this instanceof Maze3D) ? width / 3 : 1;
        carve(carveStart, 1);
        specifyWalls();
        setDefaultOpenings();
    }

    protected void setOpenings(Point entry, Point exit) {
        this.entry = entry;
        this.exit = exit;
        playerPos = entry;
        writeAt(entry, SPACE);
        writeAt(exit, SPACE);
    }

    private void setDefaultOpenings() {
        int xEnter = (this instanceof Maze3D) ? width / 3 : 1;
        int xExit = (this instanceof Maze3D) ? 2 * width / 3 : width - 2;
        setOpenings(new Point(xEnter, 0), new Point(xExit, height - 1));
    }
    //Interface methods

    @Override
    public void log() {

        for (byte[] row : grid) {
            for (byte cell : row) {
                if (cell <= WALL) {
                    System.out.print("[]");
                } else if (cell == PATH) {
                    System.out.print(" *");
                } else {
                    /**Space (dead ends or unvisited).
                     * So a number thats lower than space if unvisited
                     * and higher if it's a filled out path to a dead end.
                     */
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    public void logMat() {
        for (byte[] row : grid) {
            System.out.println();
            for (byte cell : row) {
                System.out.print(cell + " ");
            }
        }
    }

    @Override
    public Point playerPos() {
        return playerPos;
    }

    @Override
    public boolean solved() {
        return solved;
    }

    @Override
    public void solve() {
        if (entry == null || exit == null) setDefaultOpenings();
        fill(entry, exit, SPACE);
        writeAt(exit, PATH);
        solved = true;
    }

    @Override
    public void regenerate() {
        regenerate(true);
    }

    @Override
    public byte at(Point p) {
        return grid[p.y][p.x];
    }

    @Override
    public boolean movePlayer(int direction) {
        playerPos = neighbour_at(direction, playerPos);
        if (withinBounds(playerPos)) {
            return true;
        }

        return false;
    }

    @Override
    public Point entry() {
        return entry;
    }

    @Override
    public Point exit() {
        return exit;
    }


    //Convenience methods

    protected void regenerate(boolean keepPlayerPos) {
        for (int i = 0; i < width; ++i) {
            Arrays.fill(grid[i], (byte)0);
        }

        //Make sure the previous player position is carved
        if (keepPlayerPos) {
            writeAt(playerPos, PATH);
        }
        else {
            playerPos = entry;
        }

        writeAt(entry, SPACE);
        writeAt(exit, SPACE);
        int carveStart = (this instanceof Maze3D) ? width / 3 : 1;
        carve(carveStart, 1);
        specifyWalls();
    }


    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public byte at(int x, int y) {
        return grid[y][x];
    }

    public void writeAt(int i, int j, byte value) {
        grid[i][j] = value;
    }

    public void writeAt(Point p, byte value) {
        grid[p.y][p.x] = value;
    }

    public boolean isWall(Point p) {
        return grid[p.y][p.x] <= WALL;
    }

    public boolean withinBounds(int x, int y) {
        return x < width && x > 0 && y > 0 && y < height;
    }

    protected boolean withinBounds(Point p) {
        return withinBounds(p.x, p.y);
    }

    private boolean withinGrid(int x, int y) {
        return x < width && x > -1 && y > -1 && y < height;
    }

    public void inc(Point p) {
        grid[p.y][p.x] += 1;
    }

    public void dec(Point p) {
        grid[p.y][p.x] -= 1;
    }

    public List<Integer> randomizedDirections() {
        List<Integer> directions = new ArrayList<Integer>(DIRECTIONS);
        Collections.shuffle(directions);
        return directions;
    }

    public static Point neighbour_at(int direction, int x, int y) {
        switch (direction) {
            case EAST:
                return new Point(x + 1, y);
            case SOUTH:
                return new Point(x, y + 1);
            case WEST:
                return new Point(x - 1, y);
            case NORTH:
                return new Point(x, y - 1);
        }

        //return new Point();
        return null;
    }

    public static SparseArray<Point> all_neighbours(Point p) {
        SparseArray<Point> all = new SparseArray<Point>(4);
        all.put(NORTH, new Point(p.x, p.y - 1));
        all.put(EAST, new Point(p.x + 1, p.y));
        all.put(SOUTH, new Point(p.x, p.y + 1));
        all.put(WEST, new Point(p.x - 1, p.y));

        return all;
    }


    /*
     * This method will be used during the onDraw method
     * of a surface view. For performance reasons no objects
     * will be initialised by this method
     */
    public static SparseArray<Point> __all_neighbours(Point p) {

        Point north = neighbours.get(NORTH);
        north.x = p.x;
        north.y = p.y - 1;
        Point south = neighbours.get(SOUTH);
        south.x = p.x;
        south.y = p.y + 1;
        Point west = neighbours.get(WEST);
        west.x = p.x - 1;
        west.y = p.y;
        Point east = neighbours.get(EAST);
        east.x = p.x + 1;
        east.y = p.y;

        return neighbours;
    }

    public static Point neighbour_at(int direction, Point p) {
        return neighbour_at(direction, p.x, p.y);
    }

    public boolean isJunction(Point p) {
       SparseArray<Point> all = all_neighbours(p);
        int nwalls = 0;

        for (int i = 0; i < all.size(); ++i) {
            //Log.i("WTF", p.toll.s);
            if ((!withinBounds(all.get(i))) ||  isWall(all.get(i))) {
                ++nwalls;
            }
        }

        return nwalls < 2;
    }

    private boolean carvable(Point wall, Point neighbour) {
        return withinBounds(neighbour.x, neighbour.y) &&
                isWall(wall) && isWall(neighbour);

    }

    //Algorithms
    private static final int DEAD_END = -1, SUCCESS = 1;

    /**The dead-end filler algorithm. As the name suggests.
     * it goes through the maze marking a path as visited, and returning
     * if it reaches a dead-end. The parts that are only visited once, are
     * the solution since a dead-end was never encountered there.
     */


    private int fill(Point curPos, Point exitPoint, int minVisit) {

        while (!curPos.equals(exitPoint)) {
            inc(curPos);
            List<Point> cells = getAdjCells(curPos, minVisit);

            if (cells.size() > 1) {

                for (Point cell : cells) {
                    /*Do the process on all the possible pathways
                      one of them will lead to the exit the others
                      will be identified as dead ends.
                     */
                    int rc = fill(cell, exitPoint, minVisit);

                    if (rc == DEAD_END) {
                        inc(curPos);
                        /*
                         Redo the process to fill up the dead end, so it can
                         be ignored later. Increment junction for the moment
                         so that filling up doesn't go in the wrong direction.
                         */
                        fill(cell, exitPoint, minVisit + 1);
                        grid[curPos.y][curPos.x] -= 1;
                    }
                    else {
                        return SUCCESS;
                    }
                }
            }
            else if (cells.size() == 0) {
                return DEAD_END;
            }
            else {
                //Mark as visited next iteration.
                curPos = cells.get(0);
            }
        }

        return SUCCESS;
    }

    private List<Point> getAdjCells(Point curPos, int minVisit) {
        List<Point> cells = new ArrayList<Point>();

        //Always start with a random direction.
        for (int direction : randomizedDirections()) {
            Point neighbour = BaseMaze.neighbour_at(direction, curPos);

            if (withinBounds(neighbour.x, neighbour.y) &&
                    !isWall(neighbour) &&
                    (at(neighbour) == minVisit)) {
                cells.add(neighbour);
            }
        }
        return cells;
    }

    //MAZE-GENERATION METHODS
    /**The Recursive Backtracker Algorithm, for maze generation.
     *The standard recursive solution tends to run out of
     * stack space quickly as the maze gets larger. This could especially
     * be a problem on small devices. Therefore the iterative
     * version of the algorithm will be used.
     */
    private void carve(int x, int y) {
        Deque<Point> stack = new ArrayDeque<Point>();
        Point curPos = new Point(x, y);
        boolean allNeighboursCarved = false;

        //Execute first on empty stack
        do {

            if (allNeighboursCarved) {
                curPos = stack.pop();
            }
            if (curPos.x != (width - 1))writeAt(curPos, SPACE);
            allNeighboursCarved = true;
            List<Integer> directions = randomizedDirections();

            for (int i = 0; allNeighboursCarved && i < directions.size(); ++i) {
                Point wall = neighbour_at(directions.get(i), curPos);
                Point neighbour = neighbour_at(directions.get(i), wall);

                //System.out.println(carveable(maze, wall, neighbour));
                if (carvable(wall, neighbour)) {
                    if (curPos.x != (width - 1))writeAt(curPos, SPACE);
                    //writeAt(curPos, SPACE);
                    writeAt(wall, SPACE);
                    writeAt(neighbour, SPACE);

                    stack.push(curPos);
                    curPos = neighbour;
                    allNeighboursCarved = false;
                }
            }
        } while (!stack.isEmpty());
    }


    /**Give different values to walls depending on their kind, e.g
     * walls that are connedted to more than two others should have a
     * different value.
     */
    private void specifyWalls() {

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {

                int nextWalls = 0;
                Point current = new Point(x, y);
                SparseArray<Point> neighbours = all_neighbours(current);

                for (int i = 0; i < neighbours.size(); ++i) {
                    Point neighbour = neighbours.get(i);
                    if (withinGrid(neighbour.x, neighbour.y) &&
                            isWall(neighbour)) {
                        ++nextWalls;
                    }
                }

                if ((nextWalls > 2) && isWall(current)) {
                    writeAt(current, WALL_JUNCTION);
                }
            }
        }
    }
}