package com.group.msci.puzzlegenerator.BallSwitch;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.PuzzleCode;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleController {

    BallSwitchPuzzleGame gameActivity;
    BallSwitchPuzzleView gameView;
    public BallSwitchPuzzleMoveBall ballMover;

    public BallSwitchPuzzleController(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
    }

    public void setView(BallSwitchPuzzleView viewIn){gameView=viewIn;}


    public void setUpMainMenuButtons()
    {
        //Setup main menu buttons so they work
        ImageButton ballSwitchStartButton = gameActivity.findButtonById(R.id.playButton);
        ballSwitchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();
            }
        });

        ImageButton ballSwitchHelpButton = gameActivity.findButtonById(R.id.helpButton);
        ballSwitchHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.showHelpScreen();
            }
        });

        ImageButton ballSwitchLeaderBoardButton = gameActivity.findButtonById(R.id.scoreboardButton);
        ballSwitchLeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gameActivity,
                        BallSwitchPuzzleScoreboard.class);
                gameActivity.startActivity(intent);
            }
        });


    }

    public void setUpGameButtons()
    {
        //Setup gameButtons so they reset the board
        ImageButton ballSwitchResetButton = gameActivity.findButtonById(R.id.ballSwitchResetButton);
        ballSwitchResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reset all the items on the screen
                gameActivity.getPuzzle().resetPuzzle();
                ballMover.resetBallPosition();
            }
        });

        ImageButton ballSwitchShareButton = gameActivity.findButtonById(R.id.ballSwitchShareButton);
        ballSwitchShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallSwitchPuzzle puzzle = gameActivity.getPuzzle();
                puzzle.resetPuzzle();
                Gson gson = new Gson();
                String[] puzzleData = new String[4+gameActivity.getPuzzle().getObjects().size()];
                //Need the following
                //1.Size X
                //2.Size Y
                //3.The Ball
                //4.Winning moves
                //5.All other objects
                puzzleData[0] = Integer.toString(puzzle.getSizeX());
                puzzleData[1] = Integer.toString(puzzle.getSizeY());
                puzzleData[2] = Integer.toString(puzzle.getBall().getPosX()) + "," + Integer.toString(puzzle.getBall().getPosY());
                for(Integer move : puzzle.winningMoves)
                {
                    puzzleData[3] += Integer.toString(move) + ",";
                }
                puzzleData[3] = puzzleData[3].substring(4,puzzleData[3].length()-1);    //Get rid of last comma

                int counter = 4;
                for(BallSwitchObject object : puzzle.getObjects())
                {
                    if(!(object instanceof Ball))
                    {
                        if(object instanceof Fan)
                        {
                            puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY()) +","+Integer.toString(((Fan) object).getDirection());
                        }
                        else if(object instanceof Switch)
                        {
                            puzzleData[counter] = object.getClass().getName() + "," + Integer.toString(object.getPosX()) + "," + Integer.toString(object.getPosY()) ;
                        }
                        counter++;
                    }
                }
                System.out.println(puzzleData);
                //Need to turn puzzle into gson




                UploadPuzzleJSON uploader = new UploadPuzzleJSON('b', gson.toJson(puzzleData), "ballswitch");
                Thread uploadThread = new Thread(uploader);
                uploadThread.start();
                String code = "";

                try {
                    uploadThread.join();
                    code = uploader.getJSON().getString("shareCode");
                    PuzzleCode.getInstance().setCode("b" + code);
                } catch (InterruptedException|JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public void moveball(int direction)
    {
        Ball ball = gameActivity.getPuzzle().getBall();
        //direction 1-north, 2-east,3-south, 4-west
        switch (direction)
        {
            case 1:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),0);
                //moveballCheckCollision(0,-1);
                if (ball.getPosY() != 0) {
                    ballMover.addMovementBall(1);
                }
                break;
            case 2:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getSizeX(),gameActivity.puzzle.getBall().getPosY());
                //moveballCheckCollision(1, 0);
                if (ball.getPosX() != gameActivity.puzzle.getSizeX()-1) {
                    ballMover.addMovementBall(2);
                }
                break;
            case 3:
                //gameActivity.puzzle.setBall(gameActivity.puzzle.getBall().getPosX(),gameActivity.puzzle.getSizeY());
                //moveballCheckCollision(0, 1);
                if (ball.getPosY() != gameActivity.puzzle.getSizeY()-1) {
                    ballMover.addMovementBall(3);
                }
                break;
            case 4:
                //gameActivity.puzzle.setBall(0,gameActivity.puzzle.getBall().getPosY());
                //moveballCheckCollision(-1, 0);
                if (ball.getPosX() != 0) {
                    ballMover.addMovementBall(4);
                }
                break;
            default:
                break;
        }
    }

    public void moveballCheckCollision(int directionX,int directionY)
    {
        ArrayList<BallSwitchObject> objects = gameActivity.puzzle.getObjects();
        Ball ball = gameActivity.getPuzzle().getBall();

        boolean hitObject = false; // need to stop moving ball and break;
        while(ball.getPosY()+directionY<gameActivity.puzzle.getSizeY() && ball.getPosX()+directionX<gameActivity.puzzle.getSizeX() && ball.getPosY()+directionY>-1 && ball.getPosX()+directionX>-1)
        {
            //First we move so that we dont hit anything we are currently on
            ball.setPosX(ball.getPosX() + directionX);
            ball.setPosY(ball.getPosY() + directionY);

            //Animate movement
            if(directionX==1)
            {
                //East
                gameView.gameCanvas.addAnimationBall(2);
            }
            else if(directionX==-1)
            {
                //West
                gameView.gameCanvas.addAnimationBall(4);
            }
            if(directionY==1)
            {
                //South
                gameView.gameCanvas.addAnimationBall(3);
            }
            else if(directionY==-1)
            {
                //North
                gameView.gameCanvas.addAnimationBall(1);
            }

            for(BallSwitchObject object : objects)
            {
                if(object.getPosY()==ball.getPosY() && object.getPosX()==ball.getPosX() && ball!=object)
                {
                    //Use the object
                    object.use(ball,gameActivity);
                    hitObject=true;
                    break;  //No point checking anything else
                }
            }
            if(hitObject)
            {
                //If we hit the object we need to break out asap (so the ball stops moving)
                break;
            }
        }
    }
}
