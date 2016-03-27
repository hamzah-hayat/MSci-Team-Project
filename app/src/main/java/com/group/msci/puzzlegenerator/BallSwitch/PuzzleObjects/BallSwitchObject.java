package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

/**
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchObject {

    int posX,posY;

    public BallSwitchObject()
    {
        posX = 0;
        posY = 0;
    }

    public BallSwitchObject(int startX,int startY)
    {
        posX = startX;
        posY = startY;
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    @Override
    public String toString()
    {
        return "This is a Object with position " + posX + "," + posY;
    }
}
