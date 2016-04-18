package com.group.msci.puzzlegenerator.maze.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by filipt on 11/04/2016.
 */
public class MazeMainMenuController extends Activity {

    private ImageButton play;
    private ImageButton help;
    private ImageButton scoreboard;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_main);

        play = (ImageButton) findViewById(R.id.play);
        help = (ImageButton) findViewById(R.id.help);
        scoreboard = (ImageButton) findViewById(R.id.score);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MazeMainMenuController.this, MazeController.class);
                MazeMainMenuController.this.startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MazeMainMenuController.this, MazeHelpController.class);
                MazeMainMenuController.this.startActivity(intent);
            }
        });

        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MazeMainMenuController.this,
                                           MazeScoreBoardController.class);
                MazeMainMenuController.this.startActivity(intent);
            }
        });
    }
}
