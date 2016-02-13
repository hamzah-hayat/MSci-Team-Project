package com.group.msci.puzzlegenerator.maze.subviews;

import android.app.Activity;
import android.os.Bundle;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;

/**
 * Created by filipt on 11/02/2016.
 */
public class GameView extends Activity {

    private BaseMaze maze;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_game);

        maze = new BaseMaze(41, 41);
        drawMaze();
    }

    private void drawMaze() {
        MazeBoard sv = (MazeBoard) findViewById(R.id.mazeSurfaceView);
        sv.setMaze(maze);
    }





}
