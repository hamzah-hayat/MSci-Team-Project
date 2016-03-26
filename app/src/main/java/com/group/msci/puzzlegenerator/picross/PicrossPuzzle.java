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
    protected int largestClueRow;
    protected int largestClueColumn;
    protected ArrayList<ArrayList<Integer>> puzzleCluesRows = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> puzzleCluesColumns = new ArrayList<>();
    protected boolean[][] currentAnswers;

    public PicrossPuzzle(boolean[][] answerArrayT, Bitmap foregroundImageT) {
        answerArray = answerArrayT;
        foregroundImage = foregroundImageT;
        puzzleWidth = answerArray[0].length;
        puzzleHeight = answerArray.length;
        currentAnswers = new boolean[puzzleHeight][puzzleWidth];
        generateClues();
        findLargestClues();
    }

    public int getWidth() {
        return puzzleWidth;
    }

    public int getHeight () {
        return puzzleHeight;
    }

    public boolean checkIfCorrect(int i, int j) {
        if (answerArray[i][j] == true) {
            currentAnswers[i][j] = true;
            return true;
        }
        else {
            currentAnswers[i][j] = true;
            return false;
        }
    }

    public void generateClues() {
        //need to build these by row then by column, so:
        //ROW CLUES
        int countUntilBlank = 0;
        ArrayList<Integer> currentRow;
        ArrayList<Integer> currentColumn;
        for (int i = 0; i < answerArray.length; i++) {
            currentRow = new ArrayList<>();
            for (int j = 0; j < answerArray[i].length; j++) {
                if (answerArray[i][j]) { //if cell's been shaded
                    countUntilBlank++; //increase the count
                }
                else { //if cell hasn't been shaded
                    if (countUntilBlank == 0) { //if the previous cell was also blank
                        //do nothing
                    }
                    else { //if this is the first unshaded cell
                        currentRow.add(countUntilBlank); //add current shaded count
                        countUntilBlank = 0;
                    }
                }
            }
            puzzleCluesRows.add(currentRow);
        }
        countUntilBlank = 0;
        for (int i = 0; i < answerArray[0].length; i++) {
            currentColumn = new ArrayList<>();
            for (int j = 0; j < answerArray.length; j++) {
                if (answerArray[j][i]) { //if cell's been shaded
                    countUntilBlank++; //increase the count
                }
                else { //if cell hasn't been shaded
                    if (countUntilBlank == 0) { //if the previous cell was also blank
                        //do nothing
                    }
                    else { //if this is the first unshaded cell
                        currentColumn.add(countUntilBlank); //add current shaded count
                        countUntilBlank = 0;
                    }
                }
            }
            puzzleCluesColumns.add(currentColumn);
        }
        // and that generates all the clues! :D
    }

    public void findLargestClues() {
        for (int i = 0; i < puzzleCluesRows.size(); i++) {
            if (puzzleCluesRows.get(i).size() > largestClueRow) {
                largestClueRow = puzzleCluesRows.get(i).size();
            }
        }
        for (int i = 0; i < puzzleCluesColumns.size(); i++) {
            if (puzzleCluesColumns.get(i).size() > largestClueColumn) {
                largestClueColumn = puzzleCluesColumns.get(i).size();
            }
        }
    }

    public int getLargestClueRow() {
        return largestClueRow;
    }

    public int getLargestClueColumn() {
        return largestClueColumn;
    }

    public ArrayList<ArrayList<Integer>> getPuzzleCluesRows() {
        return puzzleCluesRows;
    }

    public ArrayList<ArrayList<Integer>> getPuzzleCluesColumns() {
        return puzzleCluesColumns;
    }
}
