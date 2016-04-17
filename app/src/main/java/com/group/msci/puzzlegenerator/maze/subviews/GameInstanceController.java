package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;
import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.MazeTimer;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.PortalMaze;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
import com.group.msci.puzzlegenerator.maze.utils.MazeScoreUploader;
import com.group.msci.puzzlegenerator.maze.utils.SolvedDialog;

/**
 * Created by filipt on 11/02/2016.
 * Activity that acts as a controller class
 * for a single game designed by a user.
 */
public class GameInstanceController extends Activity {

    private Maze maze;
    private MazeBoard board;
    private MoveAnimation animation;

    private ImageButton left;
    private ImageButton right;
    private ImageButton up;
    private ImageButton down;
    private Button solveBtn;
    private Button shareBtn;

    private int width;
    private int height;
    private int score;

    private TextView timeField;

    private MazeTimer timer;

    private SolvedDialog solvedDialog;
    private long remainingTime;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_play);


        final MazeParams params = getIntent().getParcelableExtra("maze_params");
        String mazeType = params.getType();

        if (mazeType.equals("Portal")) {
            maze = new PortalMaze(params.getWidth(), params.getHeight(), params.getNplanes(), params.getSeed());
        }
        else if (mazeType.equals("Simple")) {

            maze = new BaseMaze(params.getWidth(), params.getHeight(), true, params.getSeed());
        }
        else {
            throw new IllegalArgumentException("Wrong Maze type selected in parcel: " + mazeType);
        }

        width = maze.width();
        height = maze.height();

        left = (ImageButton) findViewById(R.id.left);
        right = (ImageButton) findViewById(R.id.right);
        up = (ImageButton) findViewById(R.id.up);
        down = (ImageButton) findViewById(R.id.down);
        solveBtn = (Button) findViewById(R.id.solve_btn);
        shareBtn = (Button) findViewById(R.id.share_btn);

        timeField = (TextView) findViewById(R.id.time_field);

        board = (MazeBoard) findViewById(R.id.mazeSurfaceView);

        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maze.solve();
                GameInstanceController.this.reDrawMaze();
            }
        });

        setMoveListener(BaseMaze.EAST, right);
        setMoveListener(BaseMaze.WEST, left);
        setMoveListener(BaseMaze.NORTH, up);
        setMoveListener(BaseMaze.SOUTH, down);

        solvedDialog = new SolvedDialog(this);
        solvedDialog.setContentView(R.layout.maze_solved_alert);
        board.setMaze(maze);

        if (params.getTime() > 0) {
            setAndStartTimer(new MazeTimer(params.getTime() * 1000, maze, timeField, board, this));
        }
        else {
            timeField.setText("".toCharArray(), 0,0);
        }


        animation = new MoveAnimation(board, this);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.getSeed().resetPos();
                Gson gson = new Gson();
                UploadPuzzleJSON uploader = new UploadPuzzleJSON('m', gson.toJson(params), "maze");
                Thread uploadThread = new Thread(uploader);
                uploadThread.start();
            }
        });


    }

    public void setAndStartTimer(MazeTimer timer) {
        this.timer = timer;
        this.timer.start();
    }

    public void uploadScore() {
        score = calculateScore();
        (new Thread(new MazeScoreUploader(score, getApplicationContext()))).start();
    }

    public void showSolvedDialog() {
        solvedDialog.setScore(score);
        solvedDialog.show();
    }

    public void reDrawMaze() {
        runOnUiThread(new Runnable() {
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

    private void setMoveListener(final int direction, ImageButton button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((animation != null) && (!animation.isRunning())) {
                    animation.setDirection(direction);
                    (new Thread(animation)).start();
                }
            }
        });
    }

    private int calculateScore() {
        //10 for solving maze
        int score = 10;

        //add width and height
        score += width + height - 2;

        //10 points for every extra maze plane
        score += ((maze.getNumberOfPlanes() - 1) * 10);

        if (timer != null) {
            //percent of time remaining
            score += (int) ((double) remainingTime / (double) timer.getTimeSeconds()) * 100;

            //give score for short times
            score += (60 * 10) - timer.getTimeSeconds();
        }
        return score;
    }

    public void updateRemainingTime(long millisUntilFinished) {
        remainingTime = millisUntilFinished / (long) 10e6;
    }
}
