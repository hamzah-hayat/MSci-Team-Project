package com.group.msci.puzzlegenerator.maze;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.maze.subviews.GameInstanceController;
import com.group.msci.puzzlegenerator.maze.subviews.MazeBoard;

/**
 * Created by Filipt on 29/01/2016.
 */
public class MazeTimer extends CountDownTimer {
    private Maze maze;
    private TextView timeField;
    private MazeBoard board;
    private GameInstanceController parentActivity;
    private long time;

    private final static long TICK_INTERVAL_MILIS = 1000;
    private final static String TIMER_FMT = "%02d:%02d";
    private final static int FMT_START = 0;
    private final static int FMT_LEN = 5;


    public MazeTimer(long time, Maze maze, TextView timeField, MazeBoard board,
                     GameInstanceController parentActivity) {
        super(time, TICK_INTERVAL_MILIS);
        this.time = time;
        this.maze = maze;
        this.timeField = timeField;
        this.board = board;
        this.parentActivity = parentActivity;
        timeField.setText(formatMilis(time), FMT_START, FMT_LEN);
    }

    public long getTimeSeconds() {
        return time / (long) 10e6;
    }

    //Copy constructor to be used when timer runs out to set a new one.
    private MazeTimer(MazeTimer other) {
       this(other.time, other.maze, other.timeField, other.board, other.parentActivity);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeField.setText(formatMilis(millisUntilFinished), FMT_START, FMT_LEN);
        parentActivity.updateRemainingTime(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        maze.regenerate();
        board.setMaze(maze);
        parentActivity.reDrawMaze();
        parentActivity.setAndStartTimer(new MazeTimer(this));
    }

    private static char[] formatMilis(long milis) {
        long seconds = milis / 1000;
        long minutes = seconds / 60;
        long secondsLeft = seconds % 60;
        return String.format(TIMER_FMT, minutes, secondsLeft).toCharArray();
    }
}
