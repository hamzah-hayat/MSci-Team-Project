package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;

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

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_game);

        maze = new BaseMaze(61, 61);
        left = (Button) findViewById(R.id.button_left);
        right = (Button) findViewById(R.id.button_right);
        up = (Button) findViewById(R.id.button_up);
        down = (Button) findViewById(R.id.button_down);

        board = (MazeBoard) findViewById(R.id.mazeSurfaceView);
        setMoveListener(BaseMaze.EAST, right);
        setMoveListener(BaseMaze.WEST, left);
        setMoveListener(BaseMaze.NORTH, up);
        setMoveListener(BaseMaze.SOUTH, down);
        drawMaze();
    }

    private void setMoveListener(final int direction, Button button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveAnimation animation = new MoveAnimation(direction, board);
                animation.start();
                //(new Thread(animation)).start();
            }
        });
        //button.setOnClickListener(new __MoveClickListener(direction, board));
    }

    private void drawMaze() {
        board.setMaze(maze);
    }





}
