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

    public BallSwitchPuzzleCreator(int difficultyIn,Resources cIn)
    {
        difficulty = difficulty;
        c = cIn;
    }

    public BallSwitchPuzzleCreator(int difficultyIn,boolean[] useableObstaclesIn,Resources cIn)
    {
        difficulty = difficultyIn;
        useableObstacles = useableObstaclesIn;
        c = cIn;
    }

    public BallSwitchPuzzle generatePuzzle()
    {
        System.out.println("Generating Puzzle");
        BallSwitchPuzzle puzzle;
        //Need to create a puzzle and movelist for solving that puzzle

        //difficulty is how hard, currently 0,1,2 is easy,medium and hard
        int sizeX,sizeY,obstaclesNum;

        switch(difficulty)
        {
            case 0:
                sizeX = 4;
                sizeY = 4;
                useableObstacles = new boolean[]{true,false};
                obstaclesNum=8;
                break;
            case 1:
                sizeX = 5;
                sizeY = 5;
                useableObstacles = new boolean[]{true,false};
                obstaclesNum=12;
                break;
            case 2:
                sizeX = 6;
                sizeY = 6;
                useableObstacles = new boolean[]{true,true};
                obstaclesNum=16;
                break;
            default:
                sizeX = 5;
                sizeY = 5;
                useableObstacles = new boolean[]{true,true};
                obstaclesNum=12;
                break;
        }
        puzzle = createPuzzleWithVars(sizeX,sizeY,difficulty,useableObstacles,obstaclesNum);


        //Create a puzzle
        return puzzle;
    }

    //Use the vars we have to create a puzzle
    public BallSwitchPuzzle createPuzzleWithVars(int sizeX,int sizeY,int difficulty,boolean[] obstaclesUsable,int obstaclesNum) {
        BallSwitchPuzzle createdPuzzle = new BallSwitchPuzzle(sizeX, sizeY);
        ArrayList<Integer> moveList = new ArrayList<>();
        //By using our obstacles, we create a puzzle and make a movelist for how it works.
        //int obstaclesNum = ((sizeX * sizeY) * (difficulty +1)) / 10;

        //Randomly make our starting position
        Random rand = new Random();
        int startX = rand.nextInt(sizeX);
        int startY = rand.nextInt(sizeY);
        createdPuzzle.createBall(startX, startY, c);
        Ball ball = createdPuzzle.getBall();
        //Now our ball is set up

        //Now using our ball we need to "simulate" moving it around the grid creating objects as we go
        for(int i=0;i<obstaclesNum;i++)
        {
            ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(1,2,3,4));
            boolean positionOK = false;
            //Move the ball around the grid and create objects that interact with it
            //In order we need
            /*
                1.Choose a direction and make sure theres at least one space in that direction
                2.Randomly check those spaces until i find one that is empty
				3.Move the ball to that location, and create a switch there (or a fan and push the ball away)
				4.Repeat for each object needed
             */
            int ballDirection=0;
            int spacesUsable=0;
            do {
                if(directions.size()==0)
                {
                    //Move the ball somewhere as we've run out of directions
                    directions = new ArrayList<>(Arrays.asList(1,2,3,4));
                    int a = rand.nextInt(directions.size());
                    ballDirection = directions.get(a);    //Random value using our directions
                    moveballRandom(ballDirection, createdPuzzle);
                    moveList.add(ballDirection);
                }
                int a = rand.nextInt(directions.size());
                ballDirection = directions.get(a);    //Random value using our directions
                directions.remove(new Integer(ballDirection));
                spacesUsable = moveball(ballDirection, createdPuzzle);
            }while (spacesUsable==0);
            int randomSpace = rand.nextInt(spacesUsable)+1;
            moveList.add(ballDirection);
            switch(ballDirection)
            {
                case 1:
                    createObject(ball.getPosX(), ball.getPosY() - randomSpace, createdPuzzle, obstaclesUsable);
                    ball.setPosX(ball.getPosX());
                    ball.setPosY(ball.getPosY() - randomSpace);
                    break;
                case 2:
                    createObject(ball.getPosX()+randomSpace,ball.getPosY(),createdPuzzle,obstaclesUsable);
                    ball.setPosX(ball.getPosX()+randomSpace);
                    ball.setPosY(ball.getPosY());
                    break;
                case 3:
                    createObject(ball.getPosX(),ball.getPosY()+randomSpace,createdPuzzle,obstaclesUsable);
                    ball.setPosX(ball.getPosX());
                    ball.setPosY(ball.getPosY() + randomSpace);
                    break;
                case 4:
                    createObject(ball.getPosX()-randomSpace,ball.getPosY(),createdPuzzle,obstaclesUsable);
                    ball.setPosX(ball.getPosX() - randomSpace);
                    ball.setPosY(ball.getPosY());
                    break;
            }
        }
        createdPuzzle.winningMoves = moveList;
        createdPuzzle.resetPuzzle();
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

    private void createObject(int XPos,int YPos,BallSwitchPuzzle puzzle,boolean[] obstaclesUsable)
    {
        Random rand = new Random();
        switch (rand.nextInt(obstaclesUsable.length+3))
        {
            case 1:
                puzzle.addObject(new Switch(XPos,YPos,c));
                break;
            case 2:
                puzzle.addObject(new Switch(XPos,YPos,c));
                break;
            case 3:
                puzzle.addObject(new Switch(XPos,YPos,c));
                break;
            case 4:
                puzzle.addObject(new Fan(XPos,YPos,c,rand.nextInt(4)));
                break;
            default:
                puzzle.addObject(new Switch(XPos,YPos,c));
                break;
        }
    }

	private ArrayList<Boolean> getSpaces(int direction,BallSwitchPuzzle puzzle)
	{
		//Given a direction and position, calculate how many spaces there are between the starting space
		//And end of the grid
		int startX = puzzle.getBall().getPosX();
		int startY = puzzle.getBall().getPosY();
		int freeSpaces = 0;
        ArrayList<Boolean> spaces = new ArrayList<>();
		switch(direction)
		{
			case 1:
				freeSpaces = startY-1;
                for(int i=0;i<startY-1;i++)
                {
                    spaces.add(puzzle.checkSpaceEmpty(startX, startY - i));
                }
				break;
			case 2:
				freeSpaces = puzzle.getSizeX()-startX;
                for(int i=0;i<puzzle.getSizeX()-startX;i++)
                {
                    spaces.add(puzzle.checkSpaceEmpty(startX + i, startY));
                }
				break;
			case 3:
				freeSpaces = puzzle.getSizeY() - startY;
                for(int i=0;i<puzzle.getSizeY() - startY;i++)
                {
                    spaces.add(puzzle.checkSpaceEmpty(startX, startY + i));
                }
				break;
			case 4:
				freeSpaces = startX-1;
                for(int i=0;i<startX-1;i++)
                {
                    spaces.add(puzzle.checkSpaceEmpty(startX - i, startY));
                }
				break;
			default:
				break;
		}
		return spaces;
	}

    public int moveball(int direction,BallSwitchPuzzle puzzle)
    {
        //direction 1-north, 2-east,3-south, 4-west
        switch (direction)
        {
            case 1:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),0);
                return moveballCheckCollision(0,-1,puzzle);
            case 2:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getSizeX(),gameActivity.puzzle.getBall().getPosY());
                return moveballCheckCollision(1, 0,puzzle);
            case 3:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),gameActivity.puzzle.getSizeY());
                return moveballCheckCollision(0, 1,puzzle);
            case 4:
                //gameActivity.puzzle.setBall(0,gameActivity.puzzle.getBall().getPosY());
                return moveballCheckCollision(-1, 0,puzzle);
            default:
                break;
        }
        return 0;
    }

    public int moveballCheckCollision(int directionX,int directionY,BallSwitchPuzzle puzzle)
    {
        ArrayList<BallSwitchObject> objects = puzzle.getObjects();
        Ball ball = puzzle.getBall();
        int startX = ball.getPosX();
        int startY = ball.getPosY();

        int spaces=0;
        boolean hitObject = false; // need to stop moving ball and break;
        while(ball.getPosY()+directionY<puzzle.getSizeY() && ball.getPosX() + directionX < puzzle.getSizeX() && ball.getPosY()+directionY>-1 && ball.getPosX()+directionX>-1)
        {
            //First we move so that we dont hit anything we are currently on
            ball.setPosX(ball.getPosX() + directionX);
            ball.setPosY(ball.getPosY() + directionY);
            for(BallSwitchObject object : objects)
            {
                if(object.getPosY()==ball.getPosY() && object.getPosX()==ball.getPosX() && ball!=object)
                {
                    //Use the object
                    if(object instanceof Switch)
                    {
                        ((Switch) object).changeSwitch();
                    }
                    else if (object instanceof Fan)
                    {
                        moveball(((Fan) object).getDirection(),puzzle);
                    }
                    hitObject=true;
                    break;  //No point checking anything else
                }
            }
            if(hitObject)
            {
                //If we hit the object we need to break out asap (so the ball stops moving)
                break;
            }
            spaces++;
        }
        ball.setPosX(startX);
        ball.setPosY(startY);
        return spaces;
    }


    public int moveballRandom(int direction,BallSwitchPuzzle puzzle)
    {
        //direction 1-north, 2-east,3-south, 4-west
        switch (direction)
        {
            case 1:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),0);
                return moveballCheckCollision(0,-1,puzzle);
            case 2:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getSizeX(),gameActivity.puzzle.getBall().getPosY());
                return moveballCheckCollision(1, 0,puzzle);
            case 3:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),gameActivity.puzzle.getSizeY());
                return moveballCheckCollision(0, 1,puzzle);
            case 4:
                //gameActivity.puzzle.setBall(0,gameActivity.puzzle.getBall().getPosY());
                return moveballCheckCollision(-1, 0,puzzle);
            default:
                break;
        }
        return 0;
    }

    public int moveballCheckCollisionRandom(int directionX,int directionY,BallSwitchPuzzle puzzle)
    {
        ArrayList<BallSwitchObject> objects = puzzle.getObjects();
        Ball ball = puzzle.getBall();
        int startX = ball.getPosX();
        int startY = ball.getPosY();

        int spaces=0;
        boolean hitObject = false; // need to stop moving ball and break;
        while(ball.getPosY()+directionY<puzzle.getSizeY() && ball.getPosX() + directionX < puzzle.getSizeX() && ball.getPosY()+directionY>-1 && ball.getPosX()+directionX>-1)
        {
            //First we move so that we dont hit anything we are currently on
            ball.setPosX(ball.getPosX() + directionX);
            ball.setPosY(ball.getPosY() + directionY);
            for(BallSwitchObject object : objects)
            {
                if(object.getPosY()==ball.getPosY() && object.getPosX()==ball.getPosX() && ball!=object)
                {
                    //Use the object
                    if(object instanceof Switch)
                    {
                        ((Switch) object).changeSwitch();
                    }
                    else if (object instanceof Fan)
                    {
                        moveball(((Fan) object).getDirection(),puzzle);
                    }
                    hitObject=true;
                    break;  //No point checking anything else
                }
            }
            if(hitObject)
            {
                //If we hit the object we need to break out asap (so the ball stops moving)
                break;
            }
            spaces++;
        }
        return spaces;
    }


}
