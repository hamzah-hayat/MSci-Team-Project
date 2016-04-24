package com.group.msci.puzzlegenerator.BallSwitch;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.RectF;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;

import java.util.ArrayList;

/**
 * Created by Hamzah on 24/04/2016.
 */
public class BallSwitchPuzzleMoveBall {
    //This class will be used to move the ball, it will use a thread to do that
    BallSwitchPuzzle puzzle;
    BallSwitchPuzzleMoveThread thread;
    BallSwitchPuzzleGame gameActivity;
    BallSwitchPuzzleView gameView;

    //Frame speed
    public long timeNow;
    public long timePrev = 0;
    public long timePrevFrame = 0;
    public long timeDelta;

    //An ArrayList to store a set of directions which i use to move the ball
    ArrayList<Integer> ballMoveDirections = new ArrayList<>();
    public boolean movingBall = false;
    public float ballXPosition=0;
    public float ballYPosition=0;
    float ballXPositionToMoveTo=0;
    float ballYPositionToMoveTo=0;

    public BallSwitchPuzzleMoveBall(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
        puzzle = gameActivity.getPuzzle();
        gameView = gameActivity.getView();

        thread = new BallSwitchPuzzleMoveThread(this);
        thread.setRunning(true);
        thread.start();
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

    public void addMovementBall(int direction)
    {
        if(ballMoveDirections.isEmpty())
        {
            ballMoveDirections.add(direction);
            switch (ballMoveDirections.get(0))
            {
                //direction 1-north, 2-east,3-south, 4-west
                case 1:
                    ballXPositionToMoveTo=ballXPosition;
                    ballYPositionToMoveTo=ballYPosition-1;
                    break;
                case 2:
                    ballXPositionToMoveTo=ballXPosition+1;
                    ballYPositionToMoveTo=ballYPosition;
                    break;
                case 3:
                    ballXPositionToMoveTo=ballXPosition;
                    ballYPositionToMoveTo=ballYPosition+1;
                    break;
                case 4:
                    ballXPositionToMoveTo=ballXPosition-1;
                    ballYPositionToMoveTo=ballYPosition;
                    break;
            }
        }
        else
        {
            ballMoveDirections.add(direction);
        }
        movingBall=true;
    }

    public void moveBall()
    {
        ArrayList<BallSwitchObject> objects = gameActivity.puzzle.getObjects();
        Ball ball = gameActivity.getPuzzle().getBall();
        boolean hitObject;

        if(!ballMoveDirections.isEmpty())
        {
            if(movingBall)
            {
                //Time to move the ball using the direction
                if((ballXPosition>=ballXPositionToMoveTo && ballYPosition>=ballYPositionToMoveTo && (ballMoveDirections.get(0)==2 || ballMoveDirections.get(0)==3)
                        ||(ballXPosition<=ballXPositionToMoveTo && ballYPosition<=ballYPositionToMoveTo && (ballMoveDirections.get(0)==1 || ballMoveDirections.get(0)==4))))
                {
                    //We've reached the end of movement
                    //Now reset ballX and Y position and remove move from arraylist

                    ballXPosition=ballXPositionToMoveTo;
                    ballYPosition=ballYPositionToMoveTo;
                    ball.setPosX((int)ballXPosition);
                    ball.setPosY((int)ballYPosition);
                    //Interact with whatever is on this space
                    hitObject=false;
                    for(BallSwitchObject object : objects)
                    {
                        if(object.getPosY()==ball.getPosY() && object.getPosX()==ball.getPosX() && ball!=object)
                        {
                            //Use the object
                            object.use(ball, gameActivity);
                            ballMoveDirections.remove(0);
                            hitObject=true;
                            break;  //No point checking anything else
                        }
                    }
                    if(!hitObject)
                    {
                        switch (ballMoveDirections.get(0)) {
                            case 1:
                                if (ball.getPosY() == 0) {
                                    ballMoveDirections.remove(0);
                                }
                                break;
                            case 2:
                                if (ball.getPosX() == gameActivity.puzzle.getSizeX()-1) {
                                    ballMoveDirections.remove(0);
                                }
                                break;
                            case 3:
                                if (ball.getPosY() == gameActivity.puzzle.getSizeY()-1) {
                                    ballMoveDirections.remove(0);
                                }
                                break;
                            case 4:
                                if (ball.getPosX() == 0) {
                                    ballMoveDirections.remove(0);
                                }
                                break;
                        }
                    }

                    //Now set up the next positions we need to go
                    if(!ballMoveDirections.isEmpty())
                    {
                        switch (ballMoveDirections.get(0))
                        {
                            //direction 1-north, 2-east,3-south, 4-west
                            case 1:
                                ballXPositionToMoveTo=ballXPosition;
                                ballYPositionToMoveTo=ballYPosition-1;
                                break;
                            case 2:
                                ballXPositionToMoveTo=ballXPosition+1;
                                ballYPositionToMoveTo=ballYPosition;
                                break;
                            case 3:
                                ballXPositionToMoveTo=ballXPosition;
                                ballYPositionToMoveTo=ballYPosition+1;
                                break;
                            case 4:
                                ballXPositionToMoveTo=ballXPosition-1;
                                ballYPositionToMoveTo=ballYPosition;
                                break;
                        }
                    }
                    else
                    {
                        movingBall=false;
                    }
                }
                else
                {
                    //Slowly move the ball in the correct direction
                    switch (ballMoveDirections.get(0))
                    {
                        //direction 1-north, 2-east,3-south, 4-west
                        case 1:
                            ballYPosition-=0.1f;
                            break;
                        case 2:
                            ballXPosition+=0.1f;
                            break;
                        case 3:
                            ballYPosition+=0.1f;
                            break;
                        case 4:
                            ballXPosition-=0.1f;
                            break;
                    }

                }
            }
        }
    }
    public void resetBallPosition()
    {
        ballXPosition = puzzle.getBall().getPosX();
        ballYPosition = puzzle.getBall().getPosY();
    }
}
