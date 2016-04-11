package com.group.msci.puzzlegenerator.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by filipt on 11/04/2016.
 */
public class MazeWallController extends Activity {

    private ImageButton play;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_main);

        play = (ImageButton) findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MazeWallController.this, MazeController.class);
                MazeWallController.this.startActivity(intent);
            }
        });
    }
}
