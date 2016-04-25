package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Fan;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.PuzzleCode;
import com.group.msci.puzzlegenerator.utils.json.UploadPuzzleJSON;
import com.group.msci.puzzlegenerator.utils.json.UploadScoreJSON;

import org.json.JSONException;

/**
 * Created by Hamzah on 25/04/2016.
 */
public class BallSwitchPuzzleWinPage extends Activity {

    //This activity is used for the win page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballswitch_complete);

        int time = getIntent().getIntExtra("time", 0);
        String[] puzzleData = getIntent().getStringArrayExtra("puzzleData");

        setUpGameWinButtons(time,puzzleData);

        TextView timeText = (TextView)findViewById(R.id.ballSwitchTime);
        timeText.setText(Integer.toString(time));
    }


    public void setUpGameWinButtons(int scoreIn,String[] puzzleDataIn)
    {
        ImageButton ballSwitchReturnButton = (ImageButton)findViewById(R.id.ballSwitchReturnToMainMenu);
        ballSwitchReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BallSwitchPuzzleWinPage.this,
                        BallSwitchPuzzleMainMenu.class);
                startActivity(intent);
            }
        });

        final int score = scoreIn;
        ImageButton ballSwitchScoreButton = (ImageButton)findViewById(R.id.ballSwitchUploadScore);
        ballSwitchScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Upload score to database
                PuzzleCode pc = PuzzleCode.getInstance();
                if (pc.isSet()) {
                    (new Thread(new UploadScoreJSON(pc.getTypeCode(), pc.numericCode(),
                            100-score, BallSwitchPuzzleWinPage.this)))
                            .start();

                    String msg = "Successfully Uploaded Score of " + (100-score) + " to puzzle with code " + pc.numericCode();
                    final AlertDialog alertDialog = new AlertDialog.Builder(BallSwitchPuzzleWinPage.this).create();
                    alertDialog.setTitle("Score Uploading");
                    alertDialog.setMessage(msg);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        final String[] puzzleData = puzzleDataIn;
        //Setup buttons on win page
        ImageButton ballSwitchShareButton = (ImageButton)findViewById(R.id.ballSwitchReturnButton);
        ballSwitchShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                UploadPuzzleJSON uploader = new UploadPuzzleJSON('b', gson.toJson(puzzleData), "ballswitch");
                Thread uploadThread = new Thread(uploader);
                uploadThread.start();
                String code = "";

                try {
                    uploadThread.join();
                    code = uploader.getJSON().getString("shareCode");   //this is the code of the puzzle
                    showSharedDialog(true, code);
                    PuzzleCode.getInstance().setCode("b" + code);
                } catch (InterruptedException | JSONException e) {
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
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Puzzle Sharing");
        alertDialog.setMessage(msg);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}
