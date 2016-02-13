package com.group.msci.puzzlegenerator.maze.subviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;

/**
 * Created by filipt on 12/02/2016.
 */
public class MazeBoard extends SurfaceView implements SurfaceHolder.Callback{

    private Paint wallPaint;
    private Paint pathPaint;
    private Paint playerPaint;
    private Maze currentMaze;


    public MazeBoard(Context context) {
        super(context);
        init();
    }

    public MazeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MazeBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        wallPaint = new Paint();
        pathPaint = new Paint();
        playerPaint = new Paint();
        initPaint(wallPaint, Color.BLACK);
        initPaint(pathPaint, Color.WHITE);
        initPaint(playerPaint, Color.RED);
    }

    private void initPaint(Paint p, int color) {
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        // Use Color.parseColor to define HTML colors
        canvas.drawPaint(pathPaint);
        if (currentMaze == null) {
            //canvas.drawText();
            canvas.drawPaint(wallPaint);
        }
        else {
           drawMaze2D(canvas, viewHeight, viewWidth);
        }
    }

    private void drawMaze2D(Canvas canvas, int viewHeight, int viewWidth) {
        int mazeWidth = currentMaze.width();
        int mazeHeight = currentMaze.height();
        int cellHeight = viewHeight / mazeHeight;
        int cellWidth = viewWidth / mazeWidth;

        for (int i = 0; i < mazeHeight; ++i) {
            for (int j = 0; j < mazeWidth; ++j) {
                Paint color = (currentMaze.at(j, i) <= BaseMaze.WALL) ? wallPaint : pathPaint;
                float left = j * cellWidth;
                float top = i * cellHeight;
                float right = (j + 1) * cellWidth;
                float bottom = (i + 1) * cellHeight;
                canvas.drawRect(left, top, right, bottom, color);
            }
        }
    }

    protected void setMaze(Maze maze) {
        currentMaze = maze;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
