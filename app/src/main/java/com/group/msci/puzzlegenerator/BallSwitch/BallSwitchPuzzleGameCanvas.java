package com.group.msci.puzzlegenerator.BallSwitch;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        paint.setStrokeWidth(1f);
        paint.setColor(Color.BLACK);
        puzzle = puzzleIn;

        //Find the screen height and width
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Draw the current game puzzle
        //Need to use game puzzle and screen width and size to create grid

        System.out.println("screenWidth " + screenWidth);
        System.out.println("screenHeight " + screenHeight);
        int gridWidthSpace = screenWidth/puzzle.getSizeX();
        int gridHeightSpace = screenHeight/puzzle.getSizeY();
        System.out.println("gridWidthSpace " + gridWidthSpace);
        System.out.println("gridHeightSpace " + gridHeightSpace);
        //Create Grid
        for(int i =0;i<=puzzle.getSizeX();i++)
        {
            canvas.drawLine(i * gridWidthSpace, 0, i * gridWidthSpace, screenHeight, paint);
        }
        for(int i =1;i<puzzle.getSizeY();i++)
        {
           canvas.drawLine(0, i * gridHeightSpace, screenWidth, i * gridHeightSpace, paint);
        }

        //Now create objects
        //Find the ball and draw it
        Ball ball = puzzle.getBall();
        canvas.drawCircle(ball.getPosX()*gridWidthSpace,ball.getPosY()*gridHeightSpace,100,paint);
        //Then draw switchs

    }


}