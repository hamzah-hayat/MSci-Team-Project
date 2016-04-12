package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;
import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchObject {

    int posX,posY;
    public Bitmap image;

    public BallSwitchObject()
    {
        posX = 0;
        posY = 0;
    }

    public BallSwitchObject(int startX,int startY)
    {
        //Use this one if the subclass has more then one image it needs, then it will handle it on its own
        posX = startX;
        posY = startY;
    }

    public BallSwitchObject(int startX,int startY,Resources r,int imageName)
    {
        //Use this if we just need one image
        posX = startX;
        posY = startY;
        image = BitmapFactory.decodeResource(r, imageName);
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
    public void draw(RectF box,Canvas canvas,Paint paint)
    {
        //This method should be overridden by child classes, but this is the default
        //Just a Circle
        //canvas.drawCircle(posXDraw,posYDraw,100,paint);
    }

    public void use(Ball ball,BallSwitchPuzzleGame gameActivity)
    {
        //This method does whatever the object wants to do, can use the ball if necessary
        //Default does nothing
    }
}
