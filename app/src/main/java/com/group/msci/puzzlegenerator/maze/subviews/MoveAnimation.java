package com.group.msci.puzzlegenerator.maze.subviews;

import android.graphics.Canvas;
import android.util.SparseArray;

import com.group.msci.puzzlegenerator.maze.Maze;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.model.Point;

/**
 * Created by Filipt on 13/02/2016.
 */
public class MoveAnimation extends Thread {

    private MazeBoard board;
    private GameView parentActivity;
    private int direction;
    private Maze currentMaze;
    private final float moveSize = 0.1f;

    public MoveAnimation(int direction, MazeBoard board, GameView activity) {
        super();
        this.board = board;
        this.direction = direction;
        currentMaze = board.getMaze();
        this.parentActivity = activity;
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

    @Override
    public void run() {
        if (currentMaze.at(BaseMaze.neighbour_at(direction, currentMaze.playerPos())) <= BaseMaze.WALL)
            return;
        Point current = new Point(0,0);

        do {
            int cellMoveCount = 0;
            currentMaze.movePlayer(direction);

            while(cellMoveCount < 10) {
                ++cellMoveCount;
                updatePositions();

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

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}

            }
            current = currentMaze.playerPos();
            direction = nextDirection(direction, current);
        } while(!currentMaze.isJunction(current) && (direction > -1) && !atOpening(current));

        if (current.equals(currentMaze.exit())) {
            parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parentActivity.showSolvedDialog();
                }
            });
        }
    }
}
