package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This class represents a switch on a puzzle
 * Created by Hamzah on 21/01/2016.
 */
public class Switch extends BallSwitchObject {
    int posX,posY;
    boolean switched;

    public Switch()
    {
        super();
        switched = false;
    }

    public Switch(int startX,int startY)
    {
        super(startX,startY);
        switched = false;
    }

    public Switch(int startX,int startY, boolean isSwitched)
    {
        super(startX,startY);
        switched = isSwitched;
    }

    public boolean getSwitched()
    {
        return switched;
    }
    public void changeSwitch()
    {
        switched = !switched;
    }

    @Override
    public void draw(int posXDraw,int posYDraw,Canvas canvas,Paint paint)
    {
        if (switched)
        {
            paint.setColor(Color.GREEN);
        }
        else
        {
            paint.setColor(Color.RED);
        }


        canvas.drawCircle(posXDraw,posYDraw,50,paint);
    }
}
