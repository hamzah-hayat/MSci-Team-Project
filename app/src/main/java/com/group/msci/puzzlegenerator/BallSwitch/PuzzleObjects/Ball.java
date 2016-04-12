package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.group.msci.puzzlegenerator.R;

/**
 * This Class represents a ball
 * Created by Hamzah on 21/01/2016.
 */
public class Ball extends BallSwitchObject {

    public Ball(int startX,int startY,Resources c)
    {
        super(startX, startY,c,R.drawable.ball);
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
    public void draw(RectF box,Canvas canvas,Paint paint)
    {
        //Just a ball bitmap
        System.out.print("stop");
        canvas.drawBitmap(image,null,box,paint);
    }
}
