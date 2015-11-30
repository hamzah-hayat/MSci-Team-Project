package com.group.msci.puzzlegenerator.BallSwitch;

import java.util.ArrayList;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class LeaderBoard {

    String databaseName;
    String databasePassword;

    ArrayList<ScoreTuple> records;
    int puzzleID;

    public LeaderBoard()
    {

    }

    public LeaderBoard(int puzzleIDIn)
    {
        //Create a leaderboard for this puzzle
        puzzleID = puzzleIDIn;
        //Fill up ArrayList with ScoreTuples
        getScoresForPuzzle();
    }

    private void getScoresForPuzzle()
    {
        //Get Scores using puzzleID
    }

    public ArrayList<ScoreTuple> getLeaderBoards()
    {
        return records;
    }
}
