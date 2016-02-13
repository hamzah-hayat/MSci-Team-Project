package com.group.msci.puzzlegenerator.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.subviews.GameView;

/**
 * Created by filipt on 11/28/15.
 */
public class MazeView extends Activity {

    private MazeController controller;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_activity);
        Button startBtn = (Button) findViewById(R.id.startButton);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MazeView.this, GameView.class);
                MazeView.this.startActivity(intent);
            }
        });


    }

    public void startGame() {

    }

    public void displayMaze(Maze maze) {

    }

    public void showSolution() {

    }

    public void showScore() {

    }

    public void showCreationMenu() {

    }

    public void showPauseMenu() {

    }
}
