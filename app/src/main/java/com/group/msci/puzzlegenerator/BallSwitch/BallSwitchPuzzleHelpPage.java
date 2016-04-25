package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 25/04/2016.
 */
public class BallSwitchPuzzleHelpPage extends Activity {
    //This activity is used for the Help page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballswitch_help);
        setUpHelpButtons();

    }

    public void setUpHelpButtons()
    {
        ImageButton ballSwitchReturnButton = (ImageButton)findViewById(R.id.ballSwitchReturnButton);
        ballSwitchReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create main menu activity here
                Intent intent = new Intent(BallSwitchPuzzleHelpPage.this,
                        BallSwitchPuzzleMainMenu.class);
                startActivity(intent);
            }
        });
    }

}
