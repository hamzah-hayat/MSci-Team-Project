package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.R;

/**
 * This class is an "extenstion" of the main activity BallSwitchGame, and is used to show everything
 * needed for the module
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleView {

    BallSwitchPuzzleController controller;
    BallSwitchPuzzleGame gameActivity;
    protected TextView timer;
    private long startTime = 0L;
    Handler timeHandler = new Handler();
    long timeInMS = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;


    BallSwitchPuzzleGameSurface gameCanvas;

    public BallSwitchPuzzleView(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
    }


    public void setController(BallSwitchPuzzleController controllerIn){controller=controllerIn;}

    /*
    public void showMainMenu()
    {
        gameActivity.setContentView(R.layout.ballswitch_activity);
        gameActivity.getController().setUpMainMenuButtons();
    }
*/

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

        timer = (TextView) gameActivity.findViewById(R.id.timerField);
        startTime = SystemClock.uptimeMillis();
        timeHandler.postDelayed(updateTimerThread, 0);

    }

    public Runnable updateTimerThread = new Runnable() {
        boolean update = true;
        @Override
        public void run() {
            if(update) {
                timeInMS = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMS;
                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                if (secs % 60 < 10) {
                    timer.setText(("" + mins + ":0" + Integer.toString(secs % 60)));
                } else {
                    timer.setText(("" + mins + ":" + Integer.toString(secs % 60)));
                }
                timeHandler.postDelayed(this, 0);
            }
        }
    };

    /*
    public void showHelpScreen()
    {
        gameActivity.setContentView(R.layout.ballswitch_help);
    }
    */

    /*
    public void showGameWinScreen(int time)
    {
        gameActivity.setContentView(R.layout.ballswitch_complete);
        TextView timeText = (TextView)gameActivity.findViewById(R.id.ballSwitchTime);
        timeText.setText(Integer.toString(time));
    }
    */
    /*
    public void showGeneratorScreen()
    {
        gameActivity.setContentView(R.layout.balls_create);
    }
    */


    public void showScoreBoard(int timeIn)
    {
        //Start new activity
        Intent intent = new Intent(gameActivity,
                BallSwitchPuzzleWinPage.class);
        //Need to pass my puzzle into this
        String[] puzzleData = new String[4 + gameActivity.getPuzzle().getObjects().size()];
        puzzleData[0] = Integer.toString(gameActivity.puzzle.getSizeX());
        puzzleData[1] = Integer.toString(gameActivity.puzzle.getSizeY());
        puzzleData[2] = Integer.toString(gameActivity.puzzle.getBall().getPosX()) + "," + Integer.toString(gameActivity.puzzle.getBall().getPosY());
        for (Integer move : gameActivity.puzzle.winningMoves) {
            puzzleData[3] += Integer.toString(move) + ",";
        }
        puzzleData[3] = puzzleData[3].substring(4, puzzleData[3].length() - 1);    //Get rid of last comma

        int counter = 4;
        for (BallSwitchObject object : gameActivity.puzzle.getObjects()) {
            if (!(object instanceof Ball)) {
                if (object instanceof Fan) {
                    puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY()) + "," + Integer.toString(((Fan) object).getDirection());
                } else if (object instanceof Switch) {
                    puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY());
                }
                counter++;
            }
        }
        intent.putExtra("time",(int) (gameActivity.view.updatedTime / 1000));
        intent.putExtra("puzzleData",puzzleData);
        gameActivity.startActivity(intent);
        /*
        //Game is won
        final int time = timeIn;
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameActivity);
        dlgAlert.setMessage("Puzzle is complete!");
        dlgAlert.setTitle("BallSwitch Puzzle Game");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Start new activity
                Intent intent = new Intent(gameActivity,
                        BallSwitchPuzzleWinPage.class);
                //Need to pass my puzzle into this
                String[] puzzleData = new String[4 + gameActivity.getPuzzle().getObjects().size()];
                puzzleData[0] = Integer.toString(gameActivity.puzzle.getSizeX());
                puzzleData[1] = Integer.toString(gameActivity.puzzle.getSizeY());
                puzzleData[2] = Integer.toString(gameActivity.puzzle.getBall().getPosX()) + "," + Integer.toString(gameActivity.puzzle.getBall().getPosY());
                for (Integer move : gameActivity.puzzle.winningMoves) {
                    puzzleData[3] += Integer.toString(move) + ",";
                }
                puzzleData[3] = puzzleData[3].substring(4, puzzleData[3].length() - 1);    //Get rid of last comma

                int counter = 4;
                for (BallSwitchObject object : gameActivity.puzzle.getObjects()) {
                    if (!(object instanceof Ball)) {
                        if (object instanceof Fan) {
                            puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY()) + "," + Integer.toString(((Fan) object).getDirection());
                        } else if (object instanceof Switch) {
                            puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY());
                        }
                        counter++;
                    }
                }
                intent.putExtra("time",(int) (gameActivity.view.updatedTime / 1000));
                intent.putExtra("puzzleData",puzzleData);
                gameActivity.startActivity(intent);
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        */
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
