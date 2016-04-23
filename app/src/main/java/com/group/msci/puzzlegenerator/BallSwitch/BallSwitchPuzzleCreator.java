package com.group.msci.puzzlegenerator.BallSwitch;

import android.content.Context;
import android.content.res.Resources;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleCreator {

    int difficulty;
    boolean[] useableObstacles;
    Resources c;
    //Need the context to load images

    public BallSwitchPuzzleCreator()
    {

    }

    public BallSwitchPuzzleCreator(int difficultyIn,boolean[] useableObstaclesIn,Resources cIn)
    {
        difficulty = difficultyIn;
        useableObstacles = useableObstaclesIn;
        c = cIn;
    }

    public BallSwitchPuzzle generatePuzzle()
    {
        BallSwitchPuzzle puzzle;
        //Need to create a puzzle and movelist for solving that puzzle

        //difficulty is how hard, currently 0,1,2 is easy,medium and hard
        int sizeX,sizeY;

        switch(difficulty)
        {
            case 0:
                sizeX = 5;
                sizeY = 5;
                useableObstacles = new boolean[]{true,false};
                break;
            case 1:
                sizeX = 10;
                sizeY = 10;
                useableObstacles = new boolean[]{true,true};
                break;
            case 2:
                sizeX = 15;
                sizeY = 15;
                useableObstacles = new boolean[]{true,true};
                break;
            default:
                sizeX = 5;
                sizeY = 5;
                useableObstacles = new boolean[]{true,true};
                break;
        }
        puzzle = createPuzzleWithVars(sizeX,sizeY,difficulty,useableObstacles);


        //Create a puzzle
        return puzzle;
    }

    //Use the vars we have to create a puzzle
    public BallSwitchPuzzle createPuzzleWithVars(int sizeX,int sizeY,int difficulty,boolean[] obstaclesUsable) {
        BallSwitchPuzzle createdPuzzle = new BallSwitchPuzzle(sizeX, sizeY);
        ArrayList<Integer> moveList = new ArrayList<>();
        //By using our obstacles, we create a puzzle and make a movelist for how it works.
        int obstaclesNum = ((sizeX * sizeY) * (difficulty + 2)) / 5;

        //Randomly make our starting position
        Random rand = new Random();
        int startX = rand.nextInt(sizeX);
        int startY = rand.nextInt(sizeY);
        createdPuzzle.createBall(startX, startY, c);
        //Now our ball is set up

        //Now using our ball we need to "simulate" moving it around the grid creating objects as we go
        for(int i=0;i<obstaclesNum;i++)
        {
            ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(1,2,3,4));
            boolean positionOK = false;
            //Move the ball around the grid and create objects that interact with it
            //In order we need
            /*
                1.Choose a direction and make sure theres at least one space
             */


            int ballDirection = rand.nextInt(directions.size()) + 1;    //Random value using our directions

        }


        /*
        for(int i=0;i<obstaclesNum;i++)
        {
            //Create each obstacle and add it to map
            boolean positionOK = false;
            int XPos = 0;
            int YPos = 0;
            while(!positionOK)
            {
                positionOK = true;
                //We assume this new position is ok until proven wrong
                XPos = rand.nextInt(sizeX);
                YPos = rand.nextInt(sizeY);

                for(BallSwitchObject object : createdPuzzle.getObjects())
                {
                    if(object.getPosX()==XPos && object.getPosY()==YPos)
                    {
                        positionOK = false;
                        break;
                    }
                }
            }


            switch (rand.nextInt(obstaclesUsable.length+3))
            {
                case 1:
                    createdPuzzle.addObject(new Switch(XPos,YPos,c));
                    break;
                case 2:
                    createdPuzzle.addObject(new Switch(XPos,YPos,c));
                    break;
                case 3:
                    createdPuzzle.addObject(new Switch(XPos,YPos,c));
                    break;
                case 4:
                    createdPuzzle.addObject(new Fan(XPos,YPos,c,rand.nextInt(4)));
                    break;
                default:
                    createdPuzzle.addObject(new Switch(XPos,YPos,c));
                    break;
                }
        }
        */
        return createdPuzzle;
    }

}
