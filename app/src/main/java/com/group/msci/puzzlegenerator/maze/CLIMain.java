package com.group.msci.puzzlegenerator.maze;

import java.util.Arrays;

/**
 * For testing on the command line
 */
public class CLIMain {


    public static void main(String[] args) throws InterruptedException{
        //BaseMaze m = new BaseMaze(21, 21);
        //MazeTimer timer = new MazeTimer(3000, 1000, m);
        //timer.start();
        //m.log();
        //Thread.sleep(3001);
        PortalMaze pm = new PortalMaze(21, 21, 4);
        //pm.log();
        //pm.logMat();
        test(new BaseMaze(21, 21));
        test(new PortalMaze(21, 21, 4));
        test(new Maze3D(21));
    }

    public static void test(Maze maze) {
        maze.solve();
        maze.log();
    }

}
