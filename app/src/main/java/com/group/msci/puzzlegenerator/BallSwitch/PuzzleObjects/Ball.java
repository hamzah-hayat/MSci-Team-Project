package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

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

    @Override
    public void draw(int posXDraw,int posYDraw,Canvas canvas,Paint paint)
    {
        //Just a circle for now
        canvas.drawCircle(posXDraw,posYDraw,100,paint);
    }
}
