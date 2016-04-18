package com.group.msci.puzzlegenerator.picross;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class PicrossCompleteScreen extends AppCompatActivity implements View.OnClickListener {

    private String puzzleData;
    private int shareCode;
    private boolean puzzleUploaded = false;
    private boolean scoreUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MediaPlayer winSound = MediaPlayer.create(this, R.raw.win);
        winSound.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_complete_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView time = (TextView) findViewById(R.id.finalTime);
        Intent intent = getIntent();
        String timeStr = intent.getStringExtra("FINAL_TIME");
        puzzleData = intent.getStringExtra("PUZZLE_DATA");
        System.out.println("THE PROBLEM CHILD: " + timeStr);
        time.setText(timeStr);
        Button sharePuzzle = (Button) findViewById(R.id.sharePuzzle);
        sharePuzzle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UploadPuzzleJSON jsonGetter = new UploadPuzzleJSON('p', puzzleData, "hello, world!");
        Thread x = new Thread(jsonGetter);
        x.start();
        try {
            x.join();
        }
        catch (InterruptedException ex) {
            //do nothing
        }
        JSONObject jsonFile = jsonGetter.getJSON();
        try {
            if (jsonFile.getBoolean("Success")) {
                shareCode = jsonFile.getInt("shareCode");
                System.out.println("Uploaded!");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String prefix = "";
                if (shareCode < 10) {
                    prefix = "00000";
                }
                else if (shareCode < 100) {
                    prefix = "0000";
                }
                else if (shareCode < 1000) {
                    prefix = "000";
                }
                else if (shareCode < 10000) {
                    prefix = "00";
                }
                else if (shareCode < 100000) {
                    prefix = "0";
                }
                else if (shareCode < 1000000) {
                    prefix = "";
                }
                builder.setTitle("Puzzle Code: p" + prefix + shareCode);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PicrossCompleteScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                puzzleUploaded = true;
            }
            else {
                AlertDialog.Builder builderError = new AlertDialog.Builder(this);
                builderError.setTitle("Error! Puzzle not uploaded!");
                builderError.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PicrossCompleteScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builderError.create();
                dialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
