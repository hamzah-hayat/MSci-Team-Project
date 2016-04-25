package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleCreator;
import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleMoveBall;
import com.group.msci.puzzlegenerator.R;

import java.util.Random;

/**
 * Created by Hamzah on 25/04/2016.
 */
public class BallSwitchPuzzleGeneratorMenu extends Activity {

    String currentMazeDifficulty;

    //This activity is used for the Generator
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balls_create);
        setUpGeneratorMenuButtons();
    }

    public void setUpGeneratorMenuButtons()
    {

        Spinner ballDifficultyChoices = (Spinner) findViewById(R.id.ball_difficulty_dd);

        ArrayAdapter<CharSequence> difficulty_adapter = ArrayAdapter.createFromResource(this,
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

        ImageButton ballSwitchPlayButton = (ImageButton) findViewById(R.id.playGame);
        ballSwitchPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                Intent intent = new Intent(BallSwitchPuzzleGeneratorMenu.this,
                        BallSwitchPuzzleGame.class);


                switch (currentMazeDifficulty) {
                    case "Easy":
                        intent.putExtra("difficulty", 0);
                        break;
                    case "Medium":
                        intent.putExtra("difficulty", 1);
                        break;
                    case "Hard":
                        intent.putExtra("difficulty", 2);
                        break;
                }

                startActivity(intent);


                /*
                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();
                */

            }
        });

        ImageButton ballSwitchRandomButton =  (ImageButton) findViewById(R.id.random);
        ballSwitchRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the game
                //Need to change view and start gameloop
                Intent intent = new Intent(BallSwitchPuzzleGeneratorMenu.this,
                        BallSwitchPuzzleGame.class);

                intent.putExtra("difficulty", new Random().nextInt(3));
                startActivity(intent);
                /*
                gameActivity.startGame();
                gameView.showGameScreen();
                setUpGameButtons();
                ballMover = new BallSwitchPuzzleMoveBall(gameActivity);
                ballMover.resetBallPosition();
                */

            }
        });


    }
}
