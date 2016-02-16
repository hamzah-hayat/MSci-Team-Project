package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;

import java.util.logging.Handler;

/**
 * Created by filipt on 11/02/2016.
 */
public class GameView extends Activity {

    private BaseMaze maze;
    private MazeBoard board;

    //Buttons
    private Button left;
    private Button right;
    private Button up;
    private Button down;

    private Dialog solvedDialog;

    private Handler solvedHandle;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_game);

        maze = new BaseMaze(31, 31);
        left = (Button) findViewById(R.id.button_left);
        right = (Button) findViewById(R.id.button_right);
        up = (Button) findViewById(R.id.button_up);
        down = (Button) findViewById(R.id.button_down);

        board = (MazeBoard) findViewById(R.id.mazeSurfaceView);
        setMoveListener(BaseMaze.EAST, right);
        setMoveListener(BaseMaze.WEST, left);
        setMoveListener(BaseMaze.NORTH, up);
        setMoveListener(BaseMaze.SOUTH, down);

        solvedDialog = new Dialog(this);
        solvedDialog.setContentView(R.layout.maze_solved_alert);
        solvedDialog.setTitle("Solved!");

        drawMaze();
    }

    public void showSolvedDialog() {
        solvedDialog.show();
        /*
        Button dialogButton = (Button) findViewById(R.id.dialog_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solvedDialog.dismiss();
            }
        });
        */
    }

    private void setMoveListener(final int direction, Button button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveAnimation animation = new MoveAnimation(direction, board, GameView.this);
                animation.start();
            }
        });
    }

    private void drawMaze() {
        board.setMaze(maze);
    }
}
