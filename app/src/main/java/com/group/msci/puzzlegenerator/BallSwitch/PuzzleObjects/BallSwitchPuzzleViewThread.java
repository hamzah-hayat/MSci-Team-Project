package com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGameSurface;
import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleView;

/**
 * Created by Hamzah on 16/04/2016.
 */
public class BallSwitchPuzzleViewThread extends Thread {
    //This class will be used as a thread to continually animate the canvas/view
    private SurfaceHolder surfaceHolder;
    private BallSwitchPuzzleGameSurface surface;
    private boolean run = false;

    public BallSwitchPuzzleViewThread(SurfaceHolder surfaceHolderIn, BallSwitchPuzzleGameSurface surfaceIn)
    {
        surfaceHolder = surfaceHolderIn;
        surface = surfaceIn;
    }

    public void setRunning(boolean runIn)
    {
        run = runIn;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    @Override
    public void run()
    {
        Canvas c;
        while(run)
        {
            c = null;
            //Limit frame rate to 60fps
            surface.timeNow = System.currentTimeMillis();
            surface.timeDelta = surface.timeNow - surface.timePrevFrame;
            if(surface.timeDelta<16)
            {
                try
                {
                    Thread.sleep(16-surface.timeDelta);
                }
                catch(InterruptedException ex)
                {

                }
            }
            surface.timePrevFrame = System.currentTimeMillis();

            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    //call methods to draw and process next fame
                    surface.onDraw(c);
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }

        }
    }
}