package com.group.msci.puzzlegenerator.BallSwitch;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

import java.util.ArrayList;

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
        ImageButton ballSwitchStartButton = gameActivity.findButtonById(R.id.playButton);
        ballSwitchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                gameActivity.startGame();
                gameView.showGameScreen();
            }
        });

        ImageButton ballSwitchHelpButton = gameActivity.findButtonById(R.id.helpButton);
        ballSwitchHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.showHelpScreen();
            }
        });

        ImageButton ballSwitchLeaderBoardButton = gameActivity.findButtonById(R.id.scoreboardButton);
        ballSwitchLeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gameActivity,
                        BallSwitchPuzzleScoreboard.class);
                gameActivity.startActivity(intent);
            }
        });


    }

    public void moveball(int direction)
    {
        //direction 1-north, 2-east,3-south, 4-west
        switch (direction)
        {
            case 1:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),0);
                moveballCheckCollision(0,-1);
                break;
            case 2:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getSizeX(),gameActivity.puzzle.getBall().getPosY());
                moveballCheckCollision(1, 0);
                break;
            case 3:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),gameActivity.puzzle.getSizeY());
                moveballCheckCollision(0, 1);
                break;
            case 4:
                //gameActivity.puzzle.setBall(0,gameActivity.puzzle.getBall().getPosY());
                moveballCheckCollision(-1, 0);
                break;
            default:
                break;
        }
    }

    public void moveballCheckCollision(int directionX,int directionY)
    {
        ArrayList<BallSwitchObject> objects = gameActivity.puzzle.getObjects();
        Ball ball = gameActivity.getPuzzle().getBall();

        boolean hitObject = false; // need to stop moving ball and break;
        while(ball.getPosY()+directionY<gameActivity.puzzle.getSizeY() && ball.getPosX()+directionX<gameActivity.puzzle.getSizeX() && ball.getPosY()+directionY>-1 && ball.getPosX()+directionX>-1)
        {
            //First we move so that we dont hit anything we are currently on
            ball.setPosX(ball.getPosX() + directionX);
            ball.setPosY(ball.getPosY() + directionY);

            //Animate movement
            if(directionX==1)
            {
                //East
                gameView.gameCanvas.addAnimationBall(2);
            }
            else if(directionX==-1)
            {
                //West
                gameView.gameCanvas.addAnimationBall(4);
            }
            if(directionY==1)
            {
                //South
                gameView.gameCanvas.addAnimationBall(3);
            }
            else if(directionY==-1)
            {
                //North
                gameView.gameCanvas.addAnimationBall(1);
            }

            for(BallSwitchObject object : objects)
            {
                if(object.getPosY()==ball.getPosY() && object.getPosX()==ball.getPosX() && ball!=object)
                {
                    //Use the object
                    object.use(ball,gameActivity);
                    hitObject=true;
                    break;  //No point checking anything else
                }
            }
            if(hitObject)
            {
                //If we hit the object we need to break out asap (so the ball stops moving)
                break;
            }
        }
    }
}
