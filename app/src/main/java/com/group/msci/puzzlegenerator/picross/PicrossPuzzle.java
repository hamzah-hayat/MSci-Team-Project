package com.group.msci.puzzlegenerator.picross;

import android.graphics.Bitmap;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

import java.util.ArrayList;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossPuzzle {
    protected boolean[][] answerArray;
    protected Bitmap foregroundImage;
    protected PicrossGrid grid;
    protected ArrayList<ArrayList<ImageButton>> buttons;

    public PicrossPuzzle(boolean[][] answerArrayT, Bitmap foregroundImageT) {
        answerArray = answerArrayT;
        foregroundImage = foregroundImageT;
        grid = new PicrossGrid(answerArray.length, answerArray[0].length);
    }

    public void createButtons () {

    }
}
