package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;
import com.group.msci.puzzlegenerator.R;

/**
 * This class represents a switch on a puzzle
 * Created by Hamzah on 21/01/2016.
 */
public class Switch extends BallSwitchObject {
    boolean switched;

    public Switch()
    {
        super();
        switched = false;
    }

    public Switch(int startX,int startY,Resources c)
    {
        super(startX,startY,c,R.drawable.switchimage);
        switched = false;
    }

    public Switch(int startX,int startY,Resources c, boolean isSwitched)
    {
        super(startX,startY,c,R.drawable.switchimage);
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
    public void draw(RectF box,Canvas canvas,Paint paint)
    {
        if (switched)
        {
            ColorFilter filter = new LightingColorFilter(Color.GREEN, 1);
            paint.setColorFilter(filter);
        }
        else
        {
            ColorFilter filter = new LightingColorFilter(Color.RED, 1);
            paint.setColorFilter(filter);
            //paint.setColor(Color.RED);
        }


        canvas.drawBitmap(image, null, box, paint);
    }

    @Override
    public void use(Ball ball,BallSwitchPuzzleGame gameActivity)
    {
        //"Uses" this switch
        switched=!switched;
    }

    public void reset()
    {
        switched=false;
    }
}
