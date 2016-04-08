package com.group.msci.puzzlegenerator.BallSwitch;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

import java.util.Random;

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
    public BallSwitchPuzzle createPuzzleWithVars(int sizeX,int sizeY,int difficulty,boolean[] obstaclesUsable)
    {
        BallSwitchPuzzle createdPuzzle = new BallSwitchPuzzle(sizeX,sizeY);
        //By using our obstacles, we create a puzzle and make a movelist for how it works.
        int obstaclesNum = ((sizeX*sizeY)*(difficulty+1)) / 5;

        //Randomly make our starting position
        Random rand = new Random();
        int startX = rand.nextInt(sizeX);
        int startY = rand.nextInt(sizeY);
        createdPuzzle.setBall(startX, startY);
        //Now our ball is set up

        for(int i=0;i<obstaclesNum;i++)
        {
            //Create each obstacle and add it to map
            boolean positionOK = false;
            int XPos = 0;
            int YPos = 0;
            while(!positionOK)
            {
                XPos = rand.nextInt(sizeX);
                YPos = rand.nextInt(sizeY);

                for(BallSwitchObject object : createdPuzzle.getObjects())
                {
                    if(object.getPosX()!=XPos || object.getPosY()!=YPos)
                    {
                        positionOK = true;
                        break;
                    }
                }
            }


            switch (rand.nextInt(obstaclesUsable.length+2))
            {
                case 1:
                    createdPuzzle.addObject(new Switch(XPos,YPos));
                    break;
                case 2:
                    createdPuzzle.addObject(new Switch(XPos,YPos));
                    break;
                case 3:
                    createdPuzzle.addObject(new Switch(XPos,YPos));
                    break;
                case 4:
                    createdPuzzle.addObject(new Fan(XPos,YPos,rand.nextInt(4)));
                    break;
                default:
                    createdPuzzle.addObject(new Switch(XPos,YPos));
                    break;
                }
        }
        return createdPuzzle;
    }


}
