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
import android.widget.ImageButton;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;
import com.group.msci.puzzlegenerator.utils.json.UploadScoreJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class PicrossCompleteScreen extends AppCompatActivity implements View.OnClickListener {

    private String puzzleData;
    private int shareCode;
    private boolean puzzleUploaded = false;
    private boolean scoreUploaded = false;
    private int score;
    private boolean isPuzzleUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MediaPlayer winSound = MediaPlayer.create(this, R.raw.win);
        winSound.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_complete_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView time = (TextView) findViewById(R.id.finalTime);
        Intent intent = getIntent();
        String timeStr = intent.getStringExtra("FINAL_TIME");
        puzzleData = intent.getStringExtra("PUZZLE_DATA");
        System.out.println("THE PROBLEM CHILD: " + timeStr);
        time.setText(timeStr);
        ImageButton sharePuzzle = (ImageButton) findViewById(R.id.sharePuzzle);
        sharePuzzle.setOnClickListener(this);
        ImageButton upScore = (ImageButton) findViewById(R.id.uploadScore);
        String[] timeSplit = timeStr.split(":");
        score = 90000 - (Integer.parseInt(timeSplit[0]) * 10) - Integer.parseInt(timeSplit[1]);
        upScore.setOnClickListener(this);
        isPuzzleUploaded = intent.getBooleanExtra("PUZZLE_DOWNLOADED", false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.uploadScore) {
            if (isPuzzleUploaded) {
                UploadScoreJSON x = new UploadScoreJSON('p', 0, score, this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Score Uploaded!");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PicrossCompleteScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Upload Puzzle First!");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
        }
        else if (v.getId() == R.id.sharePuzzle) {
            if (isPuzzleUploaded) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Puzzle Already Uploaded!");
                builder.setPositiveButton("Sorry!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                UploadPuzzleJSON jsonGetter = new UploadPuzzleJSON('p', puzzleData, "hello, world!");
                Thread x = new Thread(jsonGetter);
                x.start();
                try {
                    x.join();
                } catch (InterruptedException ex) {
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
                        } else if (shareCode < 100) {
                            prefix = "0000";
                        } else if (shareCode < 1000) {
                            prefix = "000";
                        } else if (shareCode < 10000) {
                            prefix = "00";
                        } else if (shareCode < 100000) {
                            prefix = "0";
                        } else if (shareCode < 1000000) {
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
                    } else {
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
    }
}
