package com.group.msci.puzzlegenerator.maze;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.maze.subviews.GameView;
import com.group.msci.puzzlegenerator.maze.subviews.MazeBoard;

import java.text.SimpleDateFormat;

/**
 * Created by Filipt on 29/01/2016.
 */
public class MazeTimer extends CountDownTimer {
    private Maze maze;
    private TextView timeField;
    private MazeBoard board;
    private GameView parentActivity;


    private final static long TICK_INTERVAL_MILIS = 1000;
    private final static String TIMER_FMT = "%02d:%02d";
    private final static int FMT_START = 0;
    private final static int FMT_LEN = 5;


    public MazeTimer(long time, Maze maze, TextView timeField, MazeBoard board, GameView parentActivity) {
        super(time, TICK_INTERVAL_MILIS);
        this.maze = maze;
        this.timeField = timeField;
        this.board = board;
        this.parentActivity = parentActivity;
        timeField.setText(formatMilis(time), FMT_START, FMT_LEN);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeField.setText(formatMilis(millisUntilFinished), FMT_START, FMT_LEN);
    }

    @Override
    public void onFinish() {
        maze.regenerate();
        board.setMaze(maze);
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = null;
                try {
                    canvas = board.getHolder().lockCanvas();
                    synchronized (board.getHolder()) {
                        board.draw(canvas);
                        board.postInvalidate();
                    }
                } finally {
                    if (canvas != null) {
                        board.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        });

    }

    private static char[] formatMilis(long milis) {
        long seconds = milis / 1000;
        long minutes = seconds / 60;
        long secondsLeft = seconds % 60;
        return String.format(TIMER_FMT, minutes, secondsLeft).toCharArray();
    }


}
