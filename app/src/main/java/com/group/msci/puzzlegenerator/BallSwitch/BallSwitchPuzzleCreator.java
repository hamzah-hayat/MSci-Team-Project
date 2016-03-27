package com.group.msci.puzzlegenerator.BallSwitch;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleCreator {

    int difficulty;
    boolean[] useableObstacles;

    public BallSwitchPuzzleCreator()
    {

    }

    public BallSwitchPuzzleCreator(int difficultyIn,boolean[] useableObstaclesIn)
    {
        difficulty = difficultyIn;
        useableObstacles = useableObstaclesIn;
    }

    public BallSwitchPuzzle generatePuzzle()
    {
        BallSwitchPuzzle puzzle = new BallSwitchPuzzle(5,5);
        //Create a puzzle
        return puzzle;
    }
}
