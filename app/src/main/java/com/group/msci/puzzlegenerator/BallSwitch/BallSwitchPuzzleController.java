package com.group.msci.puzzlegenerator.BallSwitch;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleController {

    BallSwitchPuzzleGame gameActivity;
    BallSwitchPuzzleView gameView;
    BallSwitchPuzzleModel gameModel;

    public BallSwitchPuzzleController(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
    }

    public void setView(BallSwitchPuzzleView viewIn){gameView=viewIn;}

    public void setModel(BallSwitchPuzzleModel modelIn){gameModel=modelIn;}

    public void setUpMainMenuButtons()
    {
        //Setup main menu buttons so they work
        Button ballSwitchStartButton = gameActivity.findButtonById(R.id.startGame);
        ballSwitchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                gameActivity.startGame();
                gameView.showGameScreen();
            }
        });

    }


    public void startPuzzle()
    {
        //Start the current Puzzle (from model)
    }

}
