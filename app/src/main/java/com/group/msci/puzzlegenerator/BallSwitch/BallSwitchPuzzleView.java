package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.os.Bundle;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleView extends Activity {

    BallSwitchPuzzleController controller;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.ballswitch_activity);

    }

    public BallSwitchPuzzleView()
    {

    }

    public void createGUI()
    {
        //Create the screens and windows needed for GUI

    }

    public void showMainMenu()
    {
        setContentView(R.layout.ballswitch_activity);
    }

    public void showUser(User user)
    {

    }

    public void showPuzzle()
    {

    }

    public void showSolution(String moveList)
    {

    }

    public void showLeaderBoard(LeaderBoard leaderBoard)
    {

    }

    public void showScoreBoard()
    {

    }

    public void showPuzzleCreationMenu()
    {

    }
}
