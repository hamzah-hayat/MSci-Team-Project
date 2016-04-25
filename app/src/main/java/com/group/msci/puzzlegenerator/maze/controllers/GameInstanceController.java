package com.group.msci.puzzlegenerator.maze.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.PuzzleCode;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;
import com.group.msci.puzzlegenerator.maze.models.Maze;
import com.group.msci.puzzlegenerator.maze.utils.MazeTimer;
import com.group.msci.puzzlegenerator.maze.models.BaseMaze;
import com.group.msci.puzzlegenerator.maze.models.PortalMaze;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
import com.group.msci.puzzlegenerator.maze.utils.SolvedDialog;
import com.group.msci.puzzlegenerator.utils.json.UploadScoreJSON;

import org.json.JSONException;

/**
 * Created by filipt on 11/02/2016.
 * Activity that acts as a controller class
 * for a single game designed by a user.
 */
public class GameInstanceController extends Activity {

    private Maze maze;
    private MazeBoard board;
    private MoveAnimation animation;
    private Thread animationThread;

    private ImageButton left;
    private ImageButton right;
    private ImageButton up;
    private ImageButton down;
    private Button solveBtn;
    private Button shareBtn;

    private int width;
    private int height;
    private int score;
    private int genCount;
    private boolean autoSolved;

    private TextView timeField;

    private MazeTimer timer;

    private SolvedDialog solvedDialog;
    private long remainingTime;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_play);

        genCount = 1;
        final MazeParams params = getIntent().getParcelableExtra("maze_params");
        String mazeType = params.getType();

        if (mazeType.equals("Portal")) {
            maze = new PortalMaze(params.getWidth(), params.getHeight(), params.getNplanes(),
                                  params.getSeed());
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
                autoSolved = true;
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
            timeField.setText(new char[0], 0,0);
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
                String code = "";

                try {
                    uploadThread.join();
                    code = uploader.getJSON().getString("shareCode");
                    showSharedDialog(true, code);
                    PuzzleCode.getInstance().setCode("m" + code);
                } catch (InterruptedException|JSONException e) {
                    showSharedDialog(false, code);
                    e.printStackTrace();
                }

            }
        });
    }

    public void interruptAnimationIfRunning() {
        if (animation.isRunning()) {
            animationThread.interrupt();
        }
    }

    public void incRegenCount() {
        genCount++;
    }

    @Override
    public void onBackPressed() {
        interruptAnimationIfRunning();
        super.onBackPressed();
    }

    public void showSharedDialog(boolean success, String code) {
        String msg = (success) ? "Successfully shared the puzzle, the puzzle's code is " +
                                 code : "Unfortunately failed to share the puzzle";
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Puzzle Sharing");
        alertDialog.setMessage(msg);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void setAndStartTimer(MazeTimer timer) {
        this.timer = timer;
        this.timer.start();
    }

    public void stopTimer() {
        if (timer != null)
           timer.cancel();
    }

    public void uploadScoreIfShared() {
        PuzzleCode pc = PuzzleCode.getInstance();
        if (pc.isSet()) {
            (new Thread(new UploadScoreJSON(pc.getTypeCode(), pc.numericCode(),
                                            score, getApplicationContext())))
                    .start();
        }
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
                        while (canvas == null) {
                            canvas = board.getHolder().lockCanvas();
                        }
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
                    animationThread = new Thread(animation);
                    animationThread.start();
                }
            }
        });
    }

    public void calculateScore() {

        if (autoSolved) {
            score = 0;
        }
        else {
            /*10 for solving maze
             *add width and height
             *10 points for every extra plane
             */
            score = 10 + width + height - 2 + ((maze.getNumberOfPlanes() - 1) * 10);

            if (timer != null) {
                //give score for short times
                int timeBonus = (60 * 10) - (int) timer.getTimeSeconds();
                timeBonus /= genCount;
                timeBonus += (remainingTime / timer.getTimeSeconds()) * 100; //% of remaining time
                score += timeBonus;
            }
        }
    }

    public void updateRemainingTime(long millisUntilFinished) {
        remainingTime = millisUntilFinished / (long) 10e6;
    }
}
