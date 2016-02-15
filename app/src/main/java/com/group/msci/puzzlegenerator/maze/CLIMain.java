package com.group.msci.puzzlegenerator.maze;

import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Maze3D;
import com.group.msci.puzzlegenerator.maze.model.PortalMaze;

/**
 * For testing on the command line
 */
public class CLIMain {


    public static void main(String[] args) {
        BaseMaze m1 = new BaseMaze(21, 21);
        PortalMaze m2 = new PortalMaze(21, 21, 4);
        Maze3D m3 = new Maze3D(21);
        System.out.println("maze init");
        //test(m1);
        //test(m2);
        //test(m3);
        BaseMaze m = new BaseMaze(21, 21);
        m.log();
        m.logMat();

    }

    public static void test(Maze maze) {
        maze.solve();
        maze.log();
        System.out.println();
    }

}
