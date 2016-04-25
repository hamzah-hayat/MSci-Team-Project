package com.group.msci.puzzlegenerator.BallSwitch;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGameSurface;
import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleView;

/**
 * Created by Hamzah on 16/04/2016.
 */
public class BallSwitchPuzzleMoveThread extends Thread {
    //This class will be used as a thread to move the ball
    private boolean run = false;
    BallSwitchPuzzleMoveBall ballMover;

    public BallSwitchPuzzleMoveThread(BallSwitchPuzzleMoveBall ballMoverIn)
    {
        ballMover = ballMoverIn;
    }

    public void setRunning(boolean runIn)
    {
        run = runIn;
    }

    @Override
    public void run()
    {
        while(run)
        {
            //Limit frame rate to 60fps
            ballMover.timeNow = System.currentTimeMillis();
            ballMover.timeDelta = ballMover.timeNow - ballMover.timePrevFrame;
            if(ballMover.timeDelta<16)
            {
                try
                {
                    Thread.sleep(16-ballMover.timeDelta);
                }
                catch(InterruptedException ex)
                {
                    run = false;
                }
            }
            ballMover.timePrevFrame = System.currentTimeMillis();
            ballMover.moveBall();
        }
        System.out.println("Loop Broken");
    }
}
