package com.group.msci.puzzlegenerator.picross;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossSquare {
    private boolean shaded;
    private boolean crossed;

    public PicrossSquare () {
        shaded = false;
        crossed = false;
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
}
