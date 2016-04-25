package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import java.util.Random;

/**
 * Created by Hamzah on 30/11/2015.
 */
public class BallSwitchPuzzleController {

    BallSwitchPuzzleGame gameActivity;
    BallSwitchPuzzleView gameView;
    public BallSwitchPuzzleMoveBall ballMover;
    String currentMazeDifficulty;

    public BallSwitchPuzzleController(BallSwitchPuzzleGame gameActivityIn)
    {
        gameActivity = gameActivityIn;
        ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
        ballMover.resetBallPosition();
    }

    public void setView(BallSwitchPuzzleView viewIn){gameView=viewIn;}

    public void setUpGeneratorMenuButtons()
    {

        Spinner ballDifficultyChoices = (Spinner) gameActivity.findViewById(R.id.ball_difficulty_dd);

        ArrayAdapter<CharSequence> difficulty_adapter = ArrayAdapter.createFromResource(gameActivity,
                R.array.ball_difficulties, android.R.layout.simple_spinner_item);

        difficulty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ballDifficultyChoices.setAdapter(difficulty_adapter);

        ballDifficultyChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMazeDifficulty = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentMazeDifficulty = "Medium";
            }
        });



        //Setup buttons on the generator page so that you can create a puzzle of varying difficulty

        ImageButton ballSwitchPlayButton = gameActivity.findButtonById(R.id.playGame);
        ballSwitchPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop

                switch (currentMazeDifficulty)
                {
                    case "Easy":
                        gameActivity.setCreator(new BallSwitchPuzzleCreator(0, gameActivity.getResources()));
                        break;
                    case "Medium":
                        gameActivity.setCreator(new BallSwitchPuzzleCreator(1, gameActivity.getResources()));
                        break;
                    case "Hard":
                        gameActivity.setCreator(new BallSwitchPuzzleCreator(2, gameActivity.getResources()));
                        break;
                }

                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();

            }
        });

        ImageButton ballSwitchRandomButton = gameActivity.findButtonById(R.id.random);
        ballSwitchRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop

                gameActivity.setCreator(new BallSwitchPuzzleCreator(new Random().nextInt(3), gameActivity.getResources()));

                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();

            }
        });


    }

    public void setUpMainMenuButtons()
    {
        //Setup main menu buttons so they work
        ImageButton ballSwitchStartButton = gameActivity.findButtonById(R.id.playButton);
        ballSwitchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                /*
                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();
                */

                gameView.showGeneratorScreen();
                setUpGeneratorMenuButtons();
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
                //Need to turn puzzle into gson




                UploadPuzzleJSON uploader = new UploadPuzzleJSON('b', gson.toJson(puzzleData), "ballswitch");
                Thread uploadThread = new Thread(uploader);
                uploadThread.start();
                String code = "";

                try {
                    uploadThread.join();
                    code = uploader.getJSON().getString("shareCode");   //this is the code of the puzzle
                    showSharedDialog(true, code);
                    PuzzleCode.getInstance().setCode("b" + code);
                } catch (InterruptedException|JSONException e) {
                    showSharedDialog(false, code);
                    e.printStackTrace();
                }
            }
        });



    }

    //Used from filips stuff
    public void showSharedDialog(boolean success, String code) {
        String msg = (success) ? "Successfully shared the puzzle, the puzzle's code is " +
                code : "Unfortunately failed to share the puzzle";
        final AlertDialog alertDialog = new AlertDialog.Builder(gameActivity).create();
        alertDialog.setTitle("Puzzle Sharing");
        alertDialog.setMessage(msg);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
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
}
