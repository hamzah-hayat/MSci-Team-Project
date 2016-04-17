package com.group.msci.puzzlegenerator.maze.subviews;

import android.graphics.Canvas;
import android.util.SparseArray;

import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Point;
import com.group.msci.puzzlegenerator.maze.model.Point3D;

/**
 * Created by Filipt on 13/02/2016.
 */
public class MoveAnimation implements Runnable {

    private static final int MOVE_DRAW_CNT = 2;
    private static final float moveSize = 1f / MOVE_DRAW_CNT;

    private MazeBoard board;
    private GameInstanceController parentActivity;
    private int direction;
    private Maze currentMaze;
    private boolean isRunning;

    public MoveAnimation(MazeBoard board, GameInstanceController activity) {
        this.board = board;
        currentMaze = board.getMaze();
        this.parentActivity = activity;
        isRunning = false;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return isRunning;
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

    private int nextDirection(int direction, Point playerPos) {
        SparseArray<Point> neighbours = BaseMaze.__all_neighbours(playerPos);

        for (int i = 0; i < neighbours.size(); ++i) {
            try {
                if (i != BaseMaze.opposite.get(direction) &&
                        (currentMaze.at(neighbours.get(i)) > BaseMaze.WALL)) {
                    return i;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        return -1;
    }

    private boolean atOpening(Point point) {
        return point.equals(currentMaze.entry()) || point.equals(currentMaze.exit());
    }

    private void reDrawMaze() {
        Canvas canvas = null;
        try {
            canvas = board.getHolder().lockCanvas();
            synchronized (board.getHolder()) {
                board.draw(canvas);
                board.postInvalidate();
            }
        } finally {
            if (canvas != null) {
                board.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void displaySolved() {
        parentActivity.uploadScoreIfShared();
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                parentActivity.showSolvedDialog();
            }
        });
    }

    @Override
    public void run() {
        isRunning = true;
        Point nextCell = BaseMaze.neighbour_at(direction, currentMaze.playerPos());
        boolean validDirection = !currentMaze.isWall(nextCell);

        if (validDirection) {
            Point current = currentMaze.playerPos();
            //For portal mazes only
            boolean notInLastPlane = (current instanceof Point3D) &&
                                     (((Point3D) current).z != currentMaze.getNumberOfPlanes() - 1);

            do {
                if (currentMaze.atGate(nextCell) && notInLastPlane)   {
                    //Time to move through portal to another plane

                    currentMaze.movePlayer(direction);
                    board.playerDotX = currentMaze.entryGate().x + moveSize;
                    board.playerDotY = currentMaze.entryGate().y + moveSize;
                    current = currentMaze.playerPos();
                    reDrawMaze();
                    break;
                }
                currentMaze.movePlayer(direction);

                if (currentMaze.getCurrentPlane() >= currentMaze.getNumberOfPlanes()) {
                    displaySolved();
                    break;
                }

                for (int i = 0; i < MOVE_DRAW_CNT; ++i) {
                    updatePositions();
                    reDrawMaze();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }

                current = currentMaze.playerPos();
                direction = nextDirection(direction, current);
                if (direction > -1) nextCell = BaseMaze.neighbour_at(direction, current);

            } while (!currentMaze.isJunction(current) && (direction > -1) && !atOpening(current));

            if (current.equals(currentMaze.exit())) {
                parentActivity.stopTimer();
                displaySolved();
            }
        }
        isRunning = false;
    }
}
