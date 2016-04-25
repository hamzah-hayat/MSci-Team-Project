package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 25/04/2016.
 */
public class BallSwitchPuzzleMainMenu extends Activity {
    //This activity is used for the main Menu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballswitch_activity);
        setUpMainMenuButtons();
    }
    public void setUpMainMenuButtons()
    {
        //Setup main menu buttons so they work
        ImageButton ballSwitchStartButton = (ImageButton)findViewById(R.id.playButton);
        ballSwitchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BallSwitchPuzzleMainMenu.this,
                        BallSwitchPuzzleGeneratorMenu.class);
                startActivity(intent);
            }
        });

        ImageButton ballSwitchHelpButton = (ImageButton)findViewById(R.id.helpButton);
        ballSwitchHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BallSwitchPuzzleMainMenu.this,
                        BallSwitchPuzzleHelpPage.class);
                startActivity(intent);
            }
        });

        ImageButton ballSwitchLeaderBoardButton = (ImageButton)findViewById(R.id.scoreboardButton);
        ballSwitchLeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BallSwitchPuzzleMainMenu.this,
                        BallSwitchPuzzleScoreboard.class);
                startActivity(intent);
            }
        });


    }
}
