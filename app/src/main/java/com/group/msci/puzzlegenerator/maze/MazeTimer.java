package com.group.msci.puzzlegenerator.maze;

import android.os.CountDownTimer;

/**
 * Created by Filipt on 29/01/2016.
 */
public class MazeTimer extends CountDownTimer {
    private Maze maze;

    public MazeTimer(long time, long tickInterval, Maze maze) {
        super(time, tickInterval);
        this.maze = maze;
    }

    @Override
    public void onTick(long millisUntilEnd) {
        //Display some kind of notification maybe, or update UI clock.
    }

    @Override
    public void onFinish() {
        maze.regenerate();
    }


}
