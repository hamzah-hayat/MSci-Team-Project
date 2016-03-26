package com.group.msci.puzzlegenerator.picross;

import android.content.Context;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossSquare extends ImageButton {
    private boolean shaded;
    private boolean crossed;
    private int xPosition;
    private int yPosition;

    public PicrossSquare (Context context, int xCoords, int yCoords) {
        super(context);
        shaded = false;
        crossed = false;
        xPosition = xCoords;
        yPosition = yCoords;
    }

    public void shade() {
        shaded = true;
        setBackgroundResource(R.drawable.shaded);
    }

    public void cross() {
        crossed = true;
        setBackgroundResource(R.drawable.crossed);
    }

    public void unshade() {
        shaded = false;
        setBackgroundResource(R.drawable.unshaded);
    }

    public void uncross() {
        crossed = false;
        setBackgroundResource(R.drawable.unshaded);
    }

    public boolean getShadeStatus() {
        return shaded;
    }

    public boolean getCrossStatus() {
        return crossed;
    }

    public int getXPosition () {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }
}
