package com.group.msci.puzzlegenerator.maze.controllers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.group.msci.puzzlegenerator.maze.models.Maze;
import com.group.msci.puzzlegenerator.maze.models.BaseMaze;

/**
 * Created by filipt on 12/02/2016.
 */
public class MazeBoard extends SurfaceView {

    private Maze currentMaze;

    private Paint wallPaint;
    private Paint pathPaint;
    private Paint playerPaint;
    private Paint slnPaint;

    private float cellWidth;
    private float cellHeight;
    private float extraPathSpace;
    private int mazeHeight;
    private int mazeWidth;

    /* When onDraw is called before maze is set this should be
     * false.
     */
    protected boolean shouldDrawMaze;
    protected float playerDotX;
    protected float playerDotY;

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
        shouldDrawMaze = true;
        extraPathSpace = 0.2f;
        wallPaint = new Paint();
        pathPaint = new Paint();
        playerPaint = new Paint();
        slnPaint = new Paint();
        initPaint(wallPaint, Color.BLACK);
        initPaint(pathPaint, Color.WHITE);
        initPaint(playerPaint, Color.RED);
        initPaint(slnPaint, Color.YELLOW);
    }

    private void initPaint(Paint p, int color) {
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(wallPaint);
        if (currentMaze != null) {
            mazeWidth = currentMaze.width();
            mazeHeight = currentMaze.height();
            cellHeight = getHeight() / (float) mazeHeight;
            cellWidth = getWidth() / (float) mazeWidth;
            if (shouldDrawMaze) {
                drawMaze2D(canvas);
            }
            drawPlayer(canvas);
        }
    }

    private void drawMaze2D(Canvas canvas) {
        float extraRectWidth = (extraPathSpace * cellWidth);
        float extraRectHeight = (extraPathSpace * cellHeight);

        for (int i = 0; i < mazeHeight; ++i) {
            for (int j = 0; j < mazeWidth; ++j) {
                if (currentMaze.at(j, i) > BaseMaze.WALL) {
                    float left = j * cellWidth - ((j != 1) ? extraRectWidth : extraRectWidth * 2);
                    float top = i * cellHeight - ((i != 1) ? extraRectHeight : extraRectHeight * 2);
                    float right = (j + 1) * cellWidth +
                                  ((j != mazeWidth - 2) ? extraRectWidth : extraRectWidth * 2);
                    float bottom = (i + 1) * cellHeight +
                                   ((i != mazeHeight - 2) ? extraRectHeight : extraRectHeight * 2);

                    canvas.drawRect(left, top, right, bottom,
                                   ((currentMaze.at(j, i) == BaseMaze.PATH) ? slnPaint : pathPaint));

                }
            }
        }
    }

    public void drawPlayer(Canvas canvas) {
        canvas.drawCircle(playerDotX * cellWidth, playerDotY * cellHeight,
                          cellHeight / 2, playerPaint);
    }

    public Maze getMaze() {
        return currentMaze;
    }

    public void setMaze(Maze maze) {
        playerDotX = maze.playerPos().x + 0.5f;
        playerDotY = maze.playerPos().y + 0.5f;
        currentMaze = maze;
    }
}
