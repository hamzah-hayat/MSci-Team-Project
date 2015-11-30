package com.group.msci.puzzlegenerator.BallSwitch;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class User {
    String name;
    String email;
    int userID;

    public User()
    {

    }

    public User(String nameIn,String emailIn,int userIDIn)
    {
        name = nameIn;
        email = emailIn;
        userID = userIDIn;
    }

    //Set the score for this user on a puzzle
    public void setPuzzleScore(int score,int puzzleIDIn)
    {
        //Set User Score
    }
}
