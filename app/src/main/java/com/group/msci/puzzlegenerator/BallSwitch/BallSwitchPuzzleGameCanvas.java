package com.group.msci.puzzlegenerator.BallSwitch;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;

/**
 * Created by Hamzah on 25/02/2016.
 */
public class BallSwitchPuzzleGameCanvas extends View {
    Paint paint = new Paint();
    BallSwitchPuzzle puzzle;

    int screenWidth,screenHeight;

    public  BallSwitchPuzzleGameCanvas(Context context,BallSwitchPuzzle puzzleIn) {
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
    }

    @Override
    public void onDraw(Canvas canvas) {
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


        //Draw the ball
        int objectXLeft = puzzle.getBall().getPosX() * gridWidthSpace;
        int objectYTop = puzzle.getBall().getPosY() * gridHeightSpace;
        int objectXRight = (puzzle.getBall().getPosX()+1) * gridWidthSpace;
        int objectYBottom = (puzzle.getBall().getPosY()+1) * gridHeightSpace;
        paint.setColor(Color.BLACK);    //Set the colour back to default
        ColorFilter filter = new LightingColorFilter(Color.BLACK, 1);
        paint.setColorFilter(filter);
        RectF box = new RectF(objectXLeft,objectYTop,objectXRight,objectYBottom);
        puzzle.getBall().draw(box,canvas,paint);

        //Now draw the menu buttons and bar

    }


}