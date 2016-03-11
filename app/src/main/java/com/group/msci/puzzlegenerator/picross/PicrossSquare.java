package com.group.msci.puzzlegenerator.picross;

import android.content.Context;
import android.widget.ImageButton;

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
    }

    public void cross() {
        crossed = true;
    }

    public void unshade() {
        shaded = false;
    }

    public void uncross() {
        crossed = false;
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

    public int getyPosition() {
        return yPosition;
    }
}
