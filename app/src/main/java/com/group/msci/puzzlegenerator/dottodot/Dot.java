package com.group.msci.puzzlegenerator.dottodot;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class Dot {
    private int xPos;
    private int yPos;

    public Dot(int x, int y) {
        xPos = x;
        yPos = y;
    }
    
    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
