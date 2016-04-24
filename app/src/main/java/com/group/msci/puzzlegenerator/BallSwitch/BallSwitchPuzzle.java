package com.group.msci.puzzlegenerator.BallSwitch;

import android.content.res.Resources;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzle {
    int sizeX, sizeY;
    ArrayList<BallSwitchObject> gameObjects = new ArrayList<>();
    Ball ball;
    int puzzleID;
    String puzzleCreator;
    Date dateMade;
    ArrayList<Integer> winningMoves;


    public BallSwitchPuzzle(int sizeXIn,int sizeYIn) { sizeX = sizeXIn;sizeY = sizeYIn; }

    public BallSwitchPuzzle(int puzzleIDIn)
    {
        puzzleID = puzzleIDIn;
        //Load puzzle from database
    }

    public BallSwitchPuzzle(int sizeXIn,int sizeYIn,ArrayList<BallSwitchObject> list,int puzzleIDIn,String puzzleCreatorIn,Date dateMadeIn,ArrayList<Integer> winningMovesIn)
    {
        sizeX = sizeXIn;
        sizeY = sizeYIn;
        gameObjects = list;
        puzzleID = puzzleIDIn;
        puzzleCreator = puzzleCreatorIn;
        dateMade = dateMadeIn;
        winningMoves = winningMovesIn;
    }

    public void addObject(BallSwitchObject obj)
    {
        gameObjects.add(obj);
    }

    public void removeObject(BallSwitchObject obj)
    {
        gameObjects.remove(obj);
    }

    public Ball getBall()
    {
        return ball;
    }

    public boolean checkSpaceEmpty(int posX,int posY)
    {
        for(BallSwitchObject object : getObjects())
        {
            if(object.getPosX()==posX && object.getPosY()==posY)
            {
                return true;
            }
        }
        return false;
    }

    public void createBall(int posX,int posY,Resources c)
    {
        //Makes the ball
        ball = new Ball(posX,posY,c);
        addObject(ball);
    }

    public ArrayList<BallSwitchObject> getObjects()
    {
        return gameObjects;
    }

    public boolean checkPuzzleComplete()
    {
        for(BallSwitchObject object : gameObjects)
        {
            if(object.getClass().getName().equals("com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch"))
            {
                Switch switchObject = (Switch) object;
                if(switchObject.getSwitched()==false)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public int getSizeX()
    {
        return sizeX;
    }
    public int getSizeY()
    {
        return sizeY;
    }

    public void resetPuzzle()
    {
        for(BallSwitchObject object : getObjects())
        {
            object.reset();
        }
    }
}
