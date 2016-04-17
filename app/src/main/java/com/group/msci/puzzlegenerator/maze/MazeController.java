package com.group.msci.puzzlegenerator.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.subviews.GameInstanceController;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
import com.group.msci.puzzlegenerator.maze.utils.Seed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by filipt on 11/28/15.
 */
public class MazeController extends Activity {

    private Spinner mazeTypeChoices;
    private Spinner mazeDifficultyChoices;
    private EditText minutesField;
    private EditText secondsField;
    private EditText planeNoField;
    private CheckBox useTimerCheckBox;

    private String currentMazeChoice;
    private String currentMazeDifficulty;

    private int nplanes;

    private static final HashMap<String, Integer> difficulties = new HashMap<>();

    static {
        difficulties.put("Easy", 21);
        difficulties.put("Medium", 41);
        difficulties.put("Challenging", 61);
        difficulties.put("Hard", 81);
        difficulties.put("Very Hard", 101);
    }


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_preplay);

        nplanes = 1;
        ImageButton startBtn = (ImageButton) findViewById(R.id.start_button);
        mazeTypeChoices = (Spinner) findViewById(R.id.maze_types_dd);
        mazeDifficultyChoices = (Spinner) findViewById(R.id.maze_difficulty_dd);
        secondsField = (EditText) findViewById(R.id.seconds_field);
        minutesField = (EditText) findViewById(R.id.minutes_field);
        planeNoField = (EditText) findViewById(R.id.plane_no_field);
        useTimerCheckBox = (CheckBox) findViewById(R.id.time_check_box);

        //Will be disabled unless 'use timer' is checked'.
        secondsField.setEnabled(false);
        minutesField.setEnabled(false);

        //By default this will be disabled until portal maze is selected
        planeNoField.setEnabled(false);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.maze_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mazeTypeChoices.setAdapter(adapter);

        mazeTypeChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMazeChoice = (String) parent.getItemAtPosition(position);
                if (currentMazeChoice.equals("Portal")) {
                    planeNoField.setEnabled(true);
                } else {
                    planeNoField.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //The default maze type will be the simple maze
                currentMazeChoice = "Simple";
            }
        });

        ArrayAdapter<CharSequence> difficulty_adapter = ArrayAdapter.createFromResource(this,
                R.array.maze_difficulties, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mazeDifficultyChoices.setAdapter(difficulty_adapter);


        mazeDifficultyChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMazeDifficulty = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               currentMazeDifficulty = "Medium";
            }
        });

        useTimerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timePicker.setEnabled(true);
                secondsField.setEnabled(!secondsField.isEnabled());
                minutesField.setEnabled(!minutesField.isEnabled());
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Add an extra unit to the width and height parameters
                 * to account for the outer walls
                 */
                int wallLength = difficulties.get(currentMazeDifficulty);
                boolean useTimer = useTimerCheckBox.isChecked();


                int time = (useTimer) ? Integer.parseInt(minutesField.getText().toString()) * 60 +
                        Integer.parseInt(secondsField.getText().toString()) : 0;
                String kind = currentMazeChoice;
                nplanes = (planeNoField.isEnabled()) ? Integer.parseInt(planeNoField.getText().toString()) : nplanes;

                Intent intent = new Intent(MazeController.this, GameInstanceController.class);
                Seed seed;
                Random random = new Random();

                if (kind.equals("Portal")) {
                    List<Long> seeds = new ArrayList<Long>(nplanes);
                    for (int i = 0; i < nplanes; ++i)
                        seeds.add(System.currentTimeMillis() + random.nextLong());

                    seed = new Seed(seeds);
                }
                else {
                   seed = new Seed(System.currentTimeMillis());
                }

                intent.putExtra("maze_params", new MazeParams(wallLength, wallLength, time,
                                                              nplanes, kind, seed));

                MazeController.this.startActivity(intent);
            }
        });
    }
}

