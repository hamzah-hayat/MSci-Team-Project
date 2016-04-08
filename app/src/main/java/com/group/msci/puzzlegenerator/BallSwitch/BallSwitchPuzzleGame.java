package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.R;

import java.util.Scanner;

/**
 * This is a class that creates everything necessary for the BallSwitchPuzzleModule
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchPuzzleGame extends AppCompatActivity {

    BallSwitchPuzzleModel model;
    BallSwitchPuzzleController controller;
    BallSwitchPuzzleView view;
    BallSwitchPuzzle puzzle;
    boolean gameStarted;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        model = new BallSwitchPuzzleModel();
        controller = new BallSwitchPuzzleController(this);
        view = new BallSwitchPuzzleView(this);
        //Now link them together

        model.setController(controller);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        gameStarted=false;
        //Fully set up

        view.showMainMenu();
    }

    public Button findButtonById(int id)
    {
        //format for id is R.id.ButtonId
        //Button id is name of button eg R.id.startGame
        return  (Button) findViewById(id);
    }

    //This method starts a game
    public void startGame()
    {
        /*
        puzzle = new BallSwitchPuzzle(5,5);
        puzzle.setBall(2,3);
        puzzle.addObject(new Switch(1, 1));
        puzzle.addObject(new Switch(0,3));
        puzzle.addObject(new Switch(1,3));
        */
        BallSwitchPuzzleCreator creator = new BallSwitchPuzzleCreator(0,new boolean[]{true,false});
        puzzle = creator.generatePuzzle();
        gameStarted = true;
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
        if (puzzle.checkPuzzleComplete())
        {
            //Game is won
            view.showScoreBoard();
            gameStarted = false;
        }
        return super.onTouchEvent(event);
    }

    public BallSwitchPuzzleController getController()
    {
        return controller;
    }
}
