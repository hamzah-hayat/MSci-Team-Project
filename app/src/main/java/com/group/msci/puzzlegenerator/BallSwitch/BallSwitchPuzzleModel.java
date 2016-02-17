package com.group.msci.puzzlegenerator.BallSwitch;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleModel {

    BallSwitchPuzzleController controller;
    User user;
    BallSwitchPuzzle currentPuzzle;

    public BallSwitchPuzzleModel()
    {

    }

    public void setController(BallSwitchPuzzleController controllerIn){controller = controllerIn;}

    public void setUser(User userIn)
    {
        user = userIn;
    }

    public void setPuzzle(BallSwitchPuzzle puzzleIn)
    {
        currentPuzzle = puzzleIn;
    }
}
