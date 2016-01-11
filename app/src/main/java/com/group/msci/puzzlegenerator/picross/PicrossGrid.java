package com.group.msci.puzzlegenerator.picross;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossGrid {
    protected PicrossSquare[][] grid;

    public PicrossGrid (int height, int width) {
        grid = new PicrossSquare[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                grid[x][y] = new PicrossSquare();
            }
        }
    }
}
