package com.group.msci.puzzlegenerator.BallSwitch;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;

import java.util.ArrayList;

/**
 * Created by Hamzah on 25/02/2016.
 */
public class BallSwitchPuzzleGameSurface extends SurfaceView implements SurfaceHolder.Callback {
    Paint paint = new Paint();
    BallSwitchPuzzle puzzle;
    BallSwitchPuzzleViewThread thread;
    BallSwitchPuzzleGame activity;

    //Frame speed
    public long timeNow;
    public long timePrev = 0;
    public long timePrevFrame = 0;
    public long timeDelta;

    //An ArrayList to store a set of directions which i use to move the ball
    ArrayList<Integer> ballMoveDirections = new ArrayList<>();
    public boolean animatingBall = false;
    float ballXPosition=0;
    float ballYPosition=0;
    float ballXPositionToMoveTo=0;
    float ballYPositionToMoveTo=0;

    int screenWidth,screenHeight;

    public BallSwitchPuzzleGameSurface(Context context, BallSwitchPuzzle puzzleIn) {
        super(context);
        paint.setStrokeWidth(3f);
        paint.setColor(Color.BLACK);
        puzzle = puzzleIn;

        //Find the screen height and width
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        //-200 is to bottom of screen
        screenHeight = metrics.heightPixels-500;
        screenWidth = metrics.widthPixels;

        //Set inital place of the ball
        ballXPosition = puzzle.getBall().getPosX();
        ballYPosition = puzzle.getBall().getPosY();

        activity = (BallSwitchPuzzleGame)context;
        //Set thread
        getHolder().addCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Draw the background first (aka just white colour)
        canvas.drawColor(Color.WHITE);
        //Draw the current game puzzle
        //Need to use game puzzle and screen width and size to create grid

        //System.out.println("screenWidth " + screenWidth);
        //System.out.println("screenHeight " + screenHeight);
        int gridWidthSpace = screenWidth/(puzzle.getSizeX());
        int gridHeightSpace = screenHeight/(puzzle.getSizeY());
        //System.out.println("gridWidthSpace " + gridWidthSpace);
        //System.out.println("gridHeightSpace " + gridHeightSpace);
        //Create Grid
        for(int i =1;i<puzzle.getSizeX();i++)
        {
            canvas.drawLine(i * gridWidthSpace, 0, i * gridWidthSpace, screenHeight, paint);
            //System.out.println("Drawing line " + i * gridWidthSpace);
            //canvas.drawText(Integer.toString(gridWidthSpace), i * gridWidthSpace, 100, paint);
        }
        for(int i =1;i<puzzle.getSizeY();i++) {
            canvas.drawLine(0, i * gridHeightSpace, screenWidth, i * gridHeightSpace, paint);
            //System.out.println("Drawing line " + i * gridHeightSpace);
            //canvas.drawText(Integer.toString(gridHeightSpace), 0, i * gridHeightSpace, paint);
        }

        //Draw all the objects in the puzzle
        for(BallSwitchObject object : puzzle.getObjects())
        {
            //Dont paint the ball
            if(object!= puzzle.getBall()) {
                int objectXLeft = object.getPosX() * gridWidthSpace;
                int objectYTop = object.getPosY() * gridHeightSpace;
                int objectXRight = (object.getPosX() + 1) * gridWidthSpace;
                int objectYBottom = (object.getPosY() + 1) * gridHeightSpace;
                paint.setColor(Color.BLACK);    //Set the colour back to default
                ColorFilter filter = new LightingColorFilter(Color.BLACK, 1);
                paint.setColorFilter(filter);

                //Need to create a rectangle filling the box this object is in
                RectF box = new RectF(objectXLeft, objectYTop, objectXRight, objectYBottom);
                object.draw(box, canvas, paint);
                paint.setColor(Color.BLACK);
            }
        }


        /*
        //Draw the ball
        //If our ball needs to move, animate its movement first
        if(!ballMoveDirections.isEmpty())
        {
            if(animatingBall)
            {
                //Time to animate the ball moving to the next position
                if((ballXPosition>=ballXPositionToMoveTo && ballYPosition>=ballYPositionToMoveTo && (ballMoveDirections.get(0)==2 || ballMoveDirections.get(0)==3)
                        ||(ballXPosition<=ballXPositionToMoveTo && ballYPosition<=ballYPositionToMoveTo && (ballMoveDirections.get(0)==1 || ballMoveDirections.get(0)==4))))
                {
                    //We've reached the end of movement
                    //Now reset ballX and Y position and remove move from arraylist
                    ballXPosition=ballXPositionToMoveTo;
                    ballYPosition=ballYPositionToMoveTo;
                    ballMoveDirections.remove(0);
                    //Now set up the next positions we need to go
                    if(!ballMoveDirections.isEmpty())
                    {
                        switch (ballMoveDirections.get(0))
                        {
                            //direction 1-north, 2-east,3-south, 4-west
                            case 1:
                                ballXPositionToMoveTo=ballXPosition;
                                ballYPositionToMoveTo=ballYPosition-1;
                                break;
                            case 2:
                                ballXPositionToMoveTo=ballXPosition+1;
                                ballYPositionToMoveTo=ballYPosition;
                                break;
                            case 3:
                                ballXPositionToMoveTo=ballXPosition;
                                ballYPositionToMoveTo=ballYPosition+1;
                                break;
                            case 4:
                                ballXPositionToMoveTo=ballXPosition-1;
                                ballYPositionToMoveTo=ballYPosition;
                                break;
                        }
                    }
                    else
                    {
                        animatingBall=false;
                    }
                    //Draw the ball so it doesn't disappear!
                    drawBall(gridWidthSpace,gridHeightSpace,canvas,(int)ballXPosition,(int)ballYPosition);
                }
                else
                {
                    //Slowly move the ball in the correct direction
                    switch (ballMoveDirections.get(0))
                    {
                        //direction 1-north, 2-east,3-south, 4-west
                        case 1:
                            ballYPosition-=0.1f;
                            break;
                        case 2:
                            ballXPosition+=0.1f;
                            break;
                        case 3:
                            ballYPosition+=0.1f;
                            break;
                        case 4:
                            ballXPosition-=0.1f;
                            break;
                    }
                    //Draw the ball
                    float objectXLeft = ballXPosition * gridWidthSpace;
                    float objectYTop = ballYPosition * gridHeightSpace;
                    float objectXRight = (ballXPosition+1) * gridWidthSpace;
                    float objectYBottom = (ballYPosition+1) * gridHeightSpace;
                    paint.setColor(Color.BLACK);    //Set the colour back to default
                    ColorFilter filter = new LightingColorFilter(Color.BLACK, 1);
                    paint.setColorFilter(filter);
                    RectF box = new RectF(objectXLeft,objectYTop,objectXRight,objectYBottom);
                    puzzle.getBall().draw(box,canvas,paint);

                }
            }
        } else {
            drawBall(gridWidthSpace,gridHeightSpace,canvas,puzzle.getBall().getPosX(),puzzle.getBall().getPosY());
        }

        */

        //Draw the ball
        float objectXLeft = activity.getController().ballMover.ballXPosition * gridWidthSpace;
        float objectYTop = activity.getController().ballMover.ballYPosition * gridHeightSpace;
        float objectXRight = (activity.getController().ballMover.ballXPosition+1) * gridWidthSpace;
        float objectYBottom = (activity.getController().ballMover.ballYPosition+1) * gridHeightSpace;
        paint.setColor(Color.BLACK);    //Set the colour back to default
        ColorFilter filter = new LightingColorFilter(Color.BLACK, 1);
        paint.setColorFilter(filter);
        RectF box = new RectF(objectXLeft,objectYTop,objectXRight,objectYBottom);
        puzzle.getBall().draw(box,canvas,paint);
        //Now draw the menu buttons and bar

    }

    public void addAnimationBall(int direction)
    {
        if(ballMoveDirections.isEmpty())
        {
            ballMoveDirections.add(direction);
            switch (ballMoveDirections.get(0))
            {
                //direction 1-north, 2-east,3-south, 4-west
                case 1:
                    ballXPositionToMoveTo=ballXPosition;
                    ballYPositionToMoveTo=ballYPosition-1;
                    break;
                case 2:
                    ballXPositionToMoveTo=ballXPosition+1;
                    ballYPositionToMoveTo=ballYPosition;
                    break;
                case 3:
                    ballXPositionToMoveTo=ballXPosition;
                    ballYPositionToMoveTo=ballYPosition+1;
                    break;
                case 4:
                    ballXPositionToMoveTo=ballXPosition-1;
                    ballYPositionToMoveTo=ballYPosition;
                    break;
            }
        }
        else
        {
            ballMoveDirections.add(direction);
        }
        animatingBall=true;
    }

    private void drawBall(int gridWidthSpace,int gridHeightSpace,Canvas canvas,int posX,int posY)
    {
        //Just draw the ball where it is
        int objectXLeft = posX * gridWidthSpace;
        int objectYTop = posY * gridHeightSpace;
        int objectXRight = (posX+1) * gridWidthSpace;
        int objectYBottom = (posY+1) * gridHeightSpace;
        paint.setColor(Color.BLACK);    //Set the colour back to default
        ColorFilter filter = new LightingColorFilter(Color.BLACK, 1);
        paint.setColorFilter(filter);
        RectF box = new RectF(objectXLeft,objectYTop,objectXRight,objectYBottom);
        puzzle.getBall().draw(box,canvas,paint);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new BallSwitchPuzzleViewThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public void setPuzzle(BallSwitchPuzzle puzzleIn)
    {
        puzzle = puzzleIn;
    }

    public void resetBallPosition()
    {
        ballXPosition = puzzle.getBall().getPosX();
        ballYPosition = puzzle.getBall().getPosY();
    }
}