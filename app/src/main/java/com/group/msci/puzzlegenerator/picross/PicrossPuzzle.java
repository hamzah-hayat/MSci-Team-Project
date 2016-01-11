package com.group.msci.puzzlegenerator.picross;

import android.graphics.Bitmap;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossPuzzle {
    protected boolean[][] answerArray;
    protected Bitmap originalImage;
    protected Bitmap foregroundImage;

    public PicrossPuzzle(boolean[][] answerArrayT, Bitmap originalImageT, Bitmap foregroundImageT) {
        answerArray = answerArrayT;
        originalImage = originalImageT;
        foregroundImage = foregroundImageT;
    }
}
