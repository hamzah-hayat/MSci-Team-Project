package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;

/**
 * Created by Hamzah on 08/04/2016.
 */
public class Fan extends BallSwitchObject {

    int posX,posY;
    int direction;
    //Direction this fan pushes, 1 is north,2 is east,3 is south, 4 is west

    public Fan(int startX,int startY,int directionIn)
    {
        super(startX,startY);
        direction = directionIn;
    }

    @Override
    public void draw(int posXDraw,int posYDraw,Canvas canvas,Paint paint)
    {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(posXDraw,posYDraw,10,paint);
    }

    @Override
    public void use(Ball ball,BallSwitchPuzzleGame gameActivity)
    {
        //This fan pushes the ball in the direction it is facing
        gameActivity.getController().moveball(direction);
    }

}
