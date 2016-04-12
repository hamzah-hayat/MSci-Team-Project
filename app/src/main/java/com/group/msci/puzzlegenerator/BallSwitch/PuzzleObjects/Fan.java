package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;
import com.group.msci.puzzlegenerator.R;

/**
 * Created by Hamzah on 08/04/2016.
 */
public class Fan extends BallSwitchObject {

    int posX,posY;
    int direction;
    //Direction this fan pushes, 1 is north,2 is east,3 is south, 4 is west

    public Fan(int startX,int startY,Resources c,int directionIn)
    {
        super(startX,startY,c, R.drawable.ball);
        direction = directionIn;
    }

    @Override
    public void draw(RectF box,Canvas canvas,Paint paint)
    {
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(image,null,box,paint);
    }

    @Override
    public void use(Ball ball,BallSwitchPuzzleGame gameActivity)
    {
        //This fan pushes the ball in the direction it is facing
        gameActivity.getController().moveball(direction);
    }

}
