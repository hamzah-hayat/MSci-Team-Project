package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is a class that creates everything necessary for the BallSwitchPuzzleModule
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchPuzzleGame extends Activity {

    BallSwitchPuzzleController controller;
    BallSwitchPuzzleView view;
    BallSwitchPuzzle puzzle;
    boolean gameStarted;
    BallSwitchPuzzleCreator creator;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        if(getIntent().hasExtra("ANSWER_ARRAY"))
        {
            //WE have a puzzle
            final String puzzleString = getIntent().getStringExtra("ANSWER_ARRAY");
            Gson gson = new Gson();
            String[] output = gson.fromJson(puzzleString , String[].class);
            //We have our puzzle now
            //Just set it up correctly and start the game
            puzzle = new BallSwitchPuzzle(Integer.parseInt(output[0]),Integer.parseInt(output[1]));
            //Need the following
            //1.Size X
            //2.Size Y
            //3.The Ball
            //4.Winning moves
            //5.All other objects
            String[] ball = output[2].split(",");
            puzzle.createBall(Integer.parseInt(ball[0]),Integer.parseInt(ball[1]),getResources());
            String[] moves = output[3].split(",");
            for(String move : moves)
            {
                puzzle.winningMoves.add(Integer.parseInt(move));
            }

            for(int i=4;i<output.length-1;i++)
            {
                //Add objects using the rest of the puzzledata
                String[] object = output[i].split(",");
                if(object[0].equals("com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch"))
                {
                    //Its a switch
                    puzzle.addObject(new Switch(Integer.parseInt(object[1]),Integer.parseInt(object[2]),getResources()));
                }
                else if(object[0].equals("com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan"))
                {
                    //Its a fan
                    puzzle.addObject(new Fan(Integer.parseInt(object[1]),Integer.parseInt(object[2]),getResources(),Integer.parseInt(object[3])));
                }
            }

            controller = new BallSwitchPuzzleController(this);
            view = new BallSwitchPuzzleView(this);

            controller.setView(view);
            view.setController(controller);

            gameStarted=true;
            view.showGameScreen();
            controller.setUpGameButtons();
        }
        else
        {
            controller = new BallSwitchPuzzleController(this);
            view = new BallSwitchPuzzleView(this);
            //Now link them together

            controller.setView(view);
            view.setController(controller);
            gameStarted=false;
            //Fully set up
            view.showMainMenu();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        boolean retry = true;
        controller.ballMover.thread.setRunning(false);
        while (retry) {
            try {
                controller.ballMover.thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public ImageButton findButtonById(int id)
    {
        //format for id is R.id.ButtonId
        //Button id is name of button eg R.id.startGame
        return  (ImageButton) findViewById(id);
    }

    public void setCreator(BallSwitchPuzzleCreator creatorIn)
    {
        creator = creatorIn;
    }

    //This method starts a game
    public void startGame()
    {
        puzzle = creator.generatePuzzle();
        view.gameCanvas =null;
        gameStarted = true;
        controller.ballMover = new BallSwitchPuzzleMoveBall(this);
        controller.ballMover.resetBallPosition();
    }

    public BallSwitchPuzzle getPuzzle()
    {
        return puzzle;
    }

    //Input
    float initialX, initialY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!gameStarted)
        {
            return super.onTouchEvent(event);
        }

        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                System.out.println( "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                //System.out.println("Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                System.out.println("Action was UP");

                if (initialX < finalX && finalX-initialX>100) {
                    System.out.println("Left to Right swipe performed");
                    controller.moveball(2);
                }
                else if (initialX > finalX && initialX-finalX>100) {
                    System.out.println("Right to Left swipe performed");
                    controller.moveball(4);
                }
                else if (initialY< finalY && finalY-initialY>100) {
                    System.out.println("Up to Down swipe performed");
                    controller.moveball(3);
                }
                else if (initialY > finalY && initialY-finalY>100) {
                    System.out.println("Down to Up swipe performed");
                    controller.moveball(1);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                System.out.println("Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                System.out.println("Movement occurred outside bounds of current screen element");
                break;
        }
        view.redrawGame();
        checkWin();
        return super.onTouchEvent(event);
    }

    public void checkWin()
    {
        if (puzzle.checkPuzzleComplete())
        {
            //Game is won
            view.showScoreBoard(10);
            gameStarted = false;
        }
    }

    public BallSwitchPuzzleController getController()
    {
        return controller;
    }
    public BallSwitchPuzzleView getView()
    {
        return view;
    }
}
