package com.group.msci.puzzlegenerator.BallSwitch;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

import java.util.Scanner;

/**
 * This is a class that creates a BallSwitchPuzzle Game
 * also controls the gameloop
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchPuzzleGame {

    BallSwitchPuzzle puzzle;

    public static void main(String[] args)
    {
        //Test main Method
    }

    public BallSwitchPuzzleGame()
    {
        //Setup a puzzle
        BallSwitchPuzzle puzzle = setupPuzzle();
        //Setup gameloop
        gameLoop();
        ///Finish when game is done
        System.out.println("Game is done");
    }

    public BallSwitchPuzzle setupPuzzle()
    {
        //Make a puzzle here
        puzzle = new BallSwitchPuzzle(5,5);
        //Put some stuff into it
        puzzle.addObject(new Ball(0,0));
        puzzle.addObject(new Switch(1, 1));
        return puzzle;
    }

    public void gameLoop()
    {
        Scanner scanIn = new Scanner(System.in);
        String input = scanIn.next();
        while(!input.equals("x"))
        {
            //Play the game
        }
    }

    private boolean gameDone()
    {
        for(BallSwitchObject obj : puzzle.getObjects())
        {
            if (obj.getClass().getName().equals("Switch"))
            {
                Switch switchObj = (Switch) obj;
                if(!switchObj.getSwitched())
                {
                    return false;
                }
            }
        }
        return true;
    }
}
