package com.group.msci.puzzlegenerator.picross;

import android.graphics.Bitmap;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossPuzzle {
    protected boolean[][] answerArray;
    protected Bitmap foregroundImage;
    protected PicrossGrid grid;

    public PicrossPuzzle(boolean[][] answerArrayT, Bitmap foregroundImageT) {
        answerArray = answerArrayT;
        foregroundImage = foregroundImageT;
        grid = new PicrossGrid(answerArray.length, answerArray[0].length);
    }
}
