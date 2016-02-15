package com.group.msci.puzzlegenerator.maze.subviews;

import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;

import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Point;

import java.util.List;
import java.util.Map;

/**
 * Created by Filipt on 13/02/2016.
 */
public class MoveAnimation extends Thread {

    private MazeBoard board;
    private int direction;
    private Maze boardMaze;
    private final float moveSize = 0.1f;
    //private Point destination;
    //private List<Integer> moves;

    public MoveAnimation(int direction, MazeBoard board) {
        super();
        this.board = board;
        this.direction = direction;
        boardMaze = board.getMaze();
    }

    private void updatePositions() {
        switch (direction) {
            case BaseMaze.EAST:
                board.playerDotX += moveSize;
                break;
            case BaseMaze.WEST:
                board.playerDotX -= moveSize;
                break;
            case BaseMaze.NORTH:
                board.playerDotY -= moveSize;
                break;
            case BaseMaze.SOUTH:
                board.playerDotY += moveSize;
                break;
        }
    }

    private void moveOne(int direction) {
        //long ticks = 2;
        //long startTime;
        //long sleepTime;
        int cellMoveCount = 0;
        boardMaze.movePlayer(direction);

        while(cellMoveCount < 10) {
            ++cellMoveCount;
            updatePositions();

            Canvas canvas = null;
            //startTime = System.currentTimeMillis();
            try {
                canvas = board.getHolder().lockCanvas();
                synchronized (board.getHolder()) {
                    //board.onDraw(canvas);
                    board.drawPlayer(canvas);
                    board.postInvalidate();
                }
            } finally {
                if (canvas != null) {
                    board.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            //sleepTime = ticks - (System.currentTimeMillis() - startTime);

            /*
            try {
                Thread.sleep(0, 10);
                //if (sleepTime > 0) {Thread.sleep(sleepTime);}
                //else {Thread.sleep(1);}
            } catch (InterruptedException e) {}*/

        }
    }

    private int nextDirection(int direction, Point playerPos) {
        SparseArray<Point> neighbours = BaseMaze.__all_neighbours(playerPos);

        for (int i = 0; i < neighbours.size(); ++i) {
            if (i != BaseMaze.opposite.get(direction) && (boardMaze.at(neighbours.get(i)) > BaseMaze.WALL)) {
                return i;
            }
        }

        return -1;
    }

    public boolean atOpening(Point point) {
        return point.equals(boardMaze.entry()) || point.equals(boardMaze.entry());
    }

    public void run() {
        if (boardMaze.at(BaseMaze.neighbour_at(direction, boardMaze.playerPos())) <= BaseMaze.WALL)
            return;
        Point current = null;
        do {
            moveOne(direction);
            current = boardMaze.playerPos();
            direction = nextDirection(direction, current);
        } while(!boardMaze.isJunction(current) && (direction > -1) && !atOpening(current));
    }
}
