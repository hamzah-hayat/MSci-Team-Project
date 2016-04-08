package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;

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

    //This method is used to draw for each object
    public void draw(int posXDraw,int posYDraw,Canvas canvas,Paint paint)
    {
        //This method should be overridden by child classes, but this is the default
        //Just a Circle
        canvas.drawCircle(posXDraw,posYDraw,100,paint);
    }

    public void use(Ball ball,BallSwitchPuzzleGame gameActivity)
    {
        //This method does whatever the object wants to do, can use the ball if necessary
        //Default does nothing
    }
}
