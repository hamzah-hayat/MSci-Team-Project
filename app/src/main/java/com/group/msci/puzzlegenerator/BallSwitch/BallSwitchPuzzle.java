package com.group.msci.puzzlegenerator.BallSwitch;

import java.util.Date;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzle {
    int[][] gridData;
    int puzzleID;
    String puzzleCreator;
    Date dateMade;

    public BallSwitchPuzzle()
    {

    }

    public BallSwitchPuzzle(int[][] gridDataIn)
    {
        gridData = gridDataIn;
    }

    public BallSwitchPuzzle(int puzzleIDIn)
    {
        puzzleID = puzzleIDIn;
        //Load puzzle from database
    }

    public BallSwitchPuzzle(int[][] gridDataIn,int puzzleIDIn,String puzzleCreatorIn,Date dateMadeIn)
    {
        gridData = gridDataIn;
        puzzleID = puzzleIDIn;
        puzzleCreator = puzzleCreatorIn;
        dateMade = dateMadeIn;
    }
}
