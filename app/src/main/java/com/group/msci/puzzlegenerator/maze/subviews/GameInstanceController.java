package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.json.UploadPuzzleJSON;
import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.MazeTimer;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Maze3D;
import com.group.msci.puzzlegenerator.maze.model.PortalMaze;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
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

    private Button left;
    private Button right;
    private Button up;
    private Button down;
    private Button solveBtn;
    private Button shareBtn;

    private TextView timeNotif;
    private TextView timeField;

    private MazeTimer timer;

    private SolvedDialog solvedDialog;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_game);


        final MazeParams params = getIntent().getParcelableExtra("maze_params");
        String mazeType = params.getType();

        if (mazeType.equals("Portal")) {
            maze = new PortalMaze(params.getWidth(), params.getHeight(), params.getNplanes(), params.getSeed());
        }
        else if (mazeType.equals("Cube")) {
            maze = new Maze3D(params.getWidth());
        }
        else if (mazeType.equals("Simple")) {
            maze = new BaseMaze(params.getWidth(), params.getHeight(), true, params.getSeed());
        }
        else {
            throw new IllegalArgumentException("Wrong Maze type selected in parcel: " + mazeType);
        }


        left = (Button) findViewById(R.id.button_left);
        right = (Button) findViewById(R.id.button_right);
        up = (Button) findViewById(R.id.button_up);
        down = (Button) findViewById(R.id.button_down);
        solveBtn = (Button) findViewById(R.id.solve_btn);
        shareBtn = (Button) findViewById(R.id.share_button);

        timeNotif = (TextView) findViewById(R.id.time_notification);
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
            timeField.setVisibility(View.GONE);
            timeNotif.setVisibility(View.GONE);
        }


        animation = new MoveAnimation(board, this);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPuzzleJSON uploader = new UploadPuzzleJSON('m', params.toString(), "maze");
                Thread uploadThread = new Thread(uploader);
                uploadThread.start();
            }
        });


    }

    public void setAndStartTimer(MazeTimer timer) {
        this.timer = timer;
        this.timer.start();
    }

    public void showSolvedDialog() {
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

    private void setMoveListener(final int direction, Button button) {

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
}
