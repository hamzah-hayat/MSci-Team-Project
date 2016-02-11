package com.group.msci.puzzlegenerator.BallSwitch;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzle {
    int sizeX, sizeY;
    ArrayList<BallSwitchObject> gameObjects;
    int puzzleID;
    String puzzleCreator;
    Date dateMade;

    public BallSwitchPuzzle()
    {

    }

    public BallSwitchPuzzle(int sizeXIn,int sizeYIn) { sizeX = sizeXIn;sizeY = sizeYIn; }

    public BallSwitchPuzzle(int puzzleIDIn)
    {
        puzzleID = puzzleIDIn;
        //Load puzzle from database
    }

    public BallSwitchPuzzle(int sizeXIn,int sizeYIn,ArrayList<BallSwitchObject> list,int puzzleIDIn,String puzzleCreatorIn,Date dateMadeIn)
    {
        sizeX = sizeXIn;
        sizeY = sizeYIn;
        gameObjects = list;
        puzzleID = puzzleIDIn;
        puzzleCreator = puzzleCreatorIn;
        dateMade = dateMadeIn;
    }

    public void addObject(BallSwitchObject obj)
    {
        gameObjects.add(obj);
    }

    public void removeObject(BallSwitchObject obj)
    {
        gameObjects.remove(obj);
    }

    public ArrayList<BallSwitchObject> getObjects()
    {
        return gameObjects;
    }

    public boolean checkPuzzleComplete()
    {
        for(BallSwitchObject object : gameObjects)
        {
            if(object.getClass().getName().equals("Switch"))
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
}
