package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

/**
 * This Class represents a ball
 * Created by Hamzah on 21/01/2016.
 */
public class Ball extends BallSwitchObject {

    public Ball()
    {
        super();
    }

    public Ball(int startX,int startY)
    {
        super(startX,startY);
    }

    public void setPosX(int PosX)
    {
        posX = PosX;
    }
    public void setPosY(int PosY)
    {
        posY = PosY;
    }

}
