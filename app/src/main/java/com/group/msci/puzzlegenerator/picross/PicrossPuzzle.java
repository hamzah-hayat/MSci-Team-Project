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
    protected int puzzleWidth;
    protected int puzzleHeight;

    public PicrossPuzzle(boolean[][] answerArrayT, Bitmap foregroundImageT) {
        answerArray = answerArrayT;
        foregroundImage = foregroundImageT;
        puzzleWidth = answerArray.length;
        puzzleHeight = answerArray[0].length;
    }

    public int getWidth() {
        return puzzleWidth;
    }

    public int getHeight () {
        return puzzleHeight;
    }

    public boolean checkIfCorrect(int i, int j) {
        if (answerArray[i][j] == true) {
            return true;
        }
        else {
            return false;
        }
    }
}
