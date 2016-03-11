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
import android.widget.Spinner;
import android.widget.TimePicker;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.maze.model.BaseMaze;
import com.group.msci.puzzlegenerator.maze.subviews.GameView;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;

/**
 * Created by filipt on 11/28/15.
 */
public class MazeView extends Activity {

    private MazeController controller;

    private Spinner mazeTypeChoices;
    private EditText widthField;
    private EditText heightField;
    private CheckBox useTimerCheckBox;
    private TimePicker timePicker;
    private String currentMazeChoice;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_activity);

        Button startBtn = (Button) findViewById(R.id.startButton);
        mazeTypeChoices = (Spinner) findViewById(R.id.maze_types_dd);
        widthField = (EditText) findViewById(R.id.width_field);
        heightField = (EditText) findViewById(R.id.height_field);
        useTimerCheckBox = (CheckBox) findViewById(R.id.time_check_box);
        timePicker = (TimePicker) findViewById(R.id.time_picker);

        timePicker.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.maze_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mazeTypeChoices.setAdapter(adapter);

        mazeTypeChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMazeChoice = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //The default maze type will be the simple maze
                currentMazeChoice = "Simple";
            }
        });

        useTimerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setEnabled(true);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Maybe move +1 to the maze class itself
                int width = Integer.parseInt(widthField.getText().toString()) + 1;
                int height = Integer.parseInt(heightField.getText().toString()) + 1;

                int time = timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute();
                String kind = currentMazeChoice;
                //TODO: add a field for planes and make it appear if Portal Maze is chosen
                int nplanes = 4;

                Intent intent = new Intent(MazeView.this, GameView.class);
                intent.putExtra("maze_params", new MazeParams(width, height, time, nplanes, kind));

                MazeView.this.startActivity(intent);
            }
        });
    }
}
