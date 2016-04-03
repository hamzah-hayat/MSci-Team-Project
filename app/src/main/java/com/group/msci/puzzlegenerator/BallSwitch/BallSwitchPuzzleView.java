package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.group.msci.puzzlegenerator.R;

/**
 * This class is an "extenstion" of the main activity BallSwitchGame, and is used to show everything
 * needed for the module
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleView{

    BallSwitchPuzzleController controller;
    BallSwitchPuzzleGame gameActivity;

    BallSwitchPuzzleGameCanvas gameCanvas;

    public BallSwitchPuzzleView(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
    }

    public void createGUI()
    {
        //Create the screens and windows needed for GUI

    }

    public void setController(BallSwitchPuzzleController controllerIn){controller=controllerIn;}

    public void showMainMenu()
    {
        gameActivity.setContentView(R.layout.ballswitch_activity);
    }

    //Show the gameScreen here
    public void showGameScreen()
    {
        //gameActivity.setContentView(R.layout.ballswitch_game);
        //Make sure the gameCanvas isnt null, create it if it is
        if(gameCanvas==null)
        {
            gameCanvas = new BallSwitchPuzzleGameCanvas(gameActivity,gameActivity.getPuzzle());
        }
        gameActivity.setContentView(gameCanvas);
    }

    public void showUser(User user)
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
        //Game is won
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameActivity);
        dlgAlert.setMessage("Puzle is complete!");
        dlgAlert.setTitle("BallSwitch Puzzle Game");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showMainMenu();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    public void showPuzzleCreationMenu()
    {

    }

    public void redrawGame()
    {
        gameCanvas.invalidate();
    }
}
