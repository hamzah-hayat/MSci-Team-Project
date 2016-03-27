package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Maze3D;
import com.group.msci.puzzlegenerator.maze.model.PortalMaze;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
import com.group.msci.puzzlegenerator.maze.utils.SolvedDialog;

import java.util.logging.Handler;

/**
 * Created by filipt on 11/02/2016.
 */
public class GameView extends Activity {

    private Maze maze;
    private MazeBoard board;
    private MoveAnimation animation;

    private Button left;
    private Button right;
    private Button up;
    private Button down;

    private SolvedDialog solvedDialog;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_game);


        MazeParams params = (MazeParams) getIntent().getParcelableExtra("maze_params");
        String mazeType = params.getType();

        if (mazeType.equals("Portal")) {
            maze = new PortalMaze(params.getWidth(), params.getHeight(), params.getNplanes());
        }
        else if (mazeType.equals("Cube")) {
            maze = new Maze3D(params.getWidth());
        }
        else if (mazeType.equals("Simple")) {
            maze = new BaseMaze(params.getWidth(), params.getHeight());
        }
        else {
            throw new IllegalArgumentException("Wrong Maze type selected in parcel: " + mazeType);
        }

        left = (Button) findViewById(R.id.button_left);
        right = (Button) findViewById(R.id.button_right);
        up = (Button) findViewById(R.id.button_up);
        down = (Button) findViewById(R.id.button_down);

        board = (MazeBoard) findViewById(R.id.mazeSurfaceView);

        setMoveListener(BaseMaze.EAST, right);
        setMoveListener(BaseMaze.WEST, left);
        setMoveListener(BaseMaze.NORTH, up);
        setMoveListener(BaseMaze.SOUTH, down);

        solvedDialog = new SolvedDialog(this);
        solvedDialog.setContentView(R.layout.maze_solved_alert);
        solvedDialog.setTitle("Solved!");

        drawMaze();
        animation = new MoveAnimation(board, this);
    }

    public void showSolvedDialog() {
        solvedDialog.show();
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

    private void drawMaze() {
        board.setMaze(maze);
    }
}
