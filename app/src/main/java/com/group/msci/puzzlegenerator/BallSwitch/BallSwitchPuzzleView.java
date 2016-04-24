package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.group.msci.puzzlegenerator.R;

/**
 * This class is an "extenstion" of the main activity BallSwitchGame, and is used to show everything
 * needed for the module
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleView {

    BallSwitchPuzzleController controller;
    BallSwitchPuzzleGame gameActivity;

    BallSwitchPuzzleGameSurface gameCanvas;

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
        gameActivity.getController().setUpMainMenuButtons();
    }

    //Show the gameScreen here
    public void showGameScreen()
    {
        gameActivity.setContentView(R.layout.balls_play);
        System.out.println("View set");
        //gameActivity.setContentView(R.layout.ballswitch_game);
        //Make sure the gameCanvas isnt null, create it if it is
        if(gameCanvas==null)
        {
            gameCanvas = new BallSwitchPuzzleGameSurface(gameActivity,gameActivity.getPuzzle());
            LinearLayout surface = (LinearLayout)gameActivity.findViewById(R.id.surfaceView);
            surface.addView(gameCanvas);
        }
        else
        {
            gameCanvas.setPuzzle(gameActivity.getPuzzle());
        }
    }

    public void showHelpScreen()
    {
        gameActivity.setContentView(R.layout.ballswitch_help);
    }

    public void showUser(User user)
    {

    }


    public void showSolution(String moveList)
    {

    }

    public void showScoreBoard()
    {
        //Game is won
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameActivity);
        dlgAlert.setMessage("Puzzle is complete!");
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

    public boolean getAnimatingBall() { return gameCanvas.animatingBall; }

    public void resetSurface()
    {
        gameCanvas.resetBallPosition();
    }
}
