package com.group.msci.puzzlegenerator.dottodot;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.foreground.ForegroundDetection;
import com.group.msci.puzzlegenerator.utils.PuzzleCode;
import com.group.msci.puzzlegenerator.utils.json.UploadScoreJSON;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotView extends Activity {
    //ImageView connDots;
    //ArrayList<Dot> dots;
    private TextView time;
    private long startTime = 0L;
    private Handler timeHandler = new Handler();
    private String puzzleWord;
    private long timeInMS = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private String[] dataSplit;
    private Bitmap readImage;
    private String urlLink;
    //private Bitmap readImage;
    private String data;
    private ArrayList<Dot> allDots;
    private double subHeight, subWidth;
    private double scaleH, scaleW;
    private Bitmap scaledImg;
    private DotsView dv;
    private int showCount = 0;
    private String timeAsString;
    private int score;
    private Intent intent;
    private DotMap mappedDots;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dots_play);
        intent = getIntent();
        dv = (DotsView) findViewById(R.id.dotsView2);
        if(intent.hasExtra("ANSWER_ARRAY")) {
            data = intent.getStringExtra("ANSWER_ARRAY");
            dataSplit = data.split(";");
            subWidth = Double.parseDouble(dataSplit[dataSplit.length - 2]);
            subHeight = Double.parseDouble(dataSplit[dataSplit.length - 1]);
            puzzleWord = dataSplit[dataSplit.length-3].toLowerCase();
            ArrayList<Dot> pDots = new ArrayList<>();
            for(int i = 0; i < dataSplit.length-4; i++) {
                String[] xyPair = dataSplit[i].split(",");
                pDots.add(new Dot(Integer.parseInt(xyPair[0]), Integer.parseInt(xyPair[1])));
            }
            mappedDots = new DotMap(pDots, (int)subWidth, (int)subHeight);
        }
        else if(intent.hasExtra("URL_STRING_RAND")) {
            puzzleWord = intent.getStringExtra("ANSWER").toLowerCase();
            urlLink = intent.getStringExtra("URL_STRING_RAND");
            URLBitmap retImg = new URLBitmap(urlLink);
            Thread xx = new Thread(retImg);
            xx.start();
            try {
                xx.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readImage = Bitmap.createScaledBitmap(retImg.getrImg(), dv.getLayoutParams().width, dv.getLayoutParams().height, true);

            InputStream in = getResources().openRawResource(R.raw.network);
            System.out.println("START FOREGROUND");
            ForegroundDetection fd = new ForegroundDetection(in);
            fd.setBackground(Color.BLACK);
            fd.setOutline(true);
            Bitmap mutableImg = readImage.copy(Bitmap.Config.ARGB_8888, true);
            try {
                mutableImg = fd.getForeground(mutableImg);
            } catch(IOException e) {
                e.printStackTrace();
            }

            System.out.println("START EDGE DETECTION");
            AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
            det.setSourceImage(mutableImg);
            det.process();
            Bitmap edgImg = det.getEdgesImage();
            det=null;
            System.gc();

            scaledImg = Bitmap.createScaledBitmap(edgImg, dv.getLayoutParams().width, dv.getLayoutParams().height, true);

            allDots = new ArrayList<>();
            for (int x = 0; x < scaledImg.getWidth(); x++) {
                for (int y = 0; y < scaledImg.getHeight(); y++) {
                    if (scaledImg.getPixel(x, y) != Color.BLACK) {
                        allDots.add(new Dot(x, y));
                    }
                }
            }
            ArrayList<Dot> pDots = new ArrayList<>();
            for (int i = 0; i < allDots.size(); i = i + 100) {
                Dot cDot = allDots.get(i);
                pDots.add(cDot);
            }
            mappedDots = new DotMap(pDots, dv.getLayoutParams().width, dv.getLayoutParams().height);
        }

        if(intent.hasExtra("ANSWER_ARRAY")) { //SCALE IMAGE TO CORRECT DIMENSIONS

            double newHeight = dv.getLayoutParams().height;
            double newWidth = dv.getLayoutParams().width;

            scaleH = subHeight / newHeight;
            scaleW = subWidth / newWidth;

            ArrayList<Dot> readDots = mappedDots.getDotList();

            for (Dot d : readDots) {
                double newX = d.getxPos() / scaleW;
                double newY = d.getyPos() / scaleH;
                d.setxPos((int) (newX + 0.5d));
                d.setyPos((int) (newY + 0.5d));
            }
            mappedDots.setDotList(readDots);
        }

        dv.setDots(mappedDots.getDotList());
        if(intent.hasExtra("URL_STRING_RAND")) {
            dv.removeEdgeDots();
            dv.removeOverlappingDots();
        }
        else if(intent.hasExtra("ANSWER_ARRAY")) {
            dv.removeEdgeDots();
        }
        System.out.println(puzzleWord);

        Button show = (Button) findViewById(R.id.show);
                if(intent.hasExtra("ANSWER_ARRAY")) {
                    show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(DotToDotView.this)
                                    .setTitle("Not available")
                                    .setMessage("Displaying the original image underneath the dots is not available for shared puzzles")
                                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    });
                }

                else if(intent.hasExtra("URL_STRING_RAND"))

                {
                    showSolution(show);
                }

                ImageButton exit = (ImageButton) findViewById(R.id.dots_exit);
                exit.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view){
                    readImage = null;
                    allDots = null;
                    System.gc();
                    finish();
                    Intent intent = new Intent(DotToDotView.this, MainActivity.class);
                    startActivity(intent);
                }
                }

                );

                ImageButton guess = (ImageButton) findViewById(R.id.dots_guess);
                guess.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view){
                    final EditText inputText = (EditText) findViewById(R.id.dots_answer);
                    String input = inputText.getText().toString();
                    timeAsString = time.getText().toString();
                    score = calculateScore(timeAsString);
                    if (input.equals(puzzleWord)) {
                        if (intent.hasExtra("ANSWER_ARRAY")) {
                            uploadScore();
                        }
                        new AlertDialog.Builder(DotToDotView.this)
                                .setTitle("Correct!")
                                .setMessage("You have guessed the image correctly. Your score for the puzzle is " + score)
                                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent intent = new Intent(DotToDotView.this, DotToDotMainScreen.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNeutralButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switchToMain();
                                    }
                                })
                                .show();
                    } else {
                        new AlertDialog.Builder(DotToDotView.this)
                                .setTitle("Incorrect!")
                                .setMessage("The answer you have given is incorrect")
                                .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        inputText.setText("");
                                    }
                                })
                                .setPositiveButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switchToMain();
                                    }
                                })
                                .show();
                    }
                }
                }

                );

        Button hint = (Button) findViewById(R.id.hint);
        hint.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View view){
                    puzzleWord.length();
                    new AlertDialog.Builder(DotToDotView.this)
                            .setTitle("Hint")
                            .setMessage("The word has " + puzzleWord.length() + " characters. The word starts with " + puzzleWord.charAt(0) + " and ends with " + puzzleWord.charAt(puzzleWord.length() - 1))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                }

                );

                time=(TextView)

                findViewById(R.id.time);

                startTime=SystemClock.uptimeMillis();
                timeHandler.postDelayed(updateTimerThread,0);

            }

            private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMS = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMS;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            if (secs % 60 < 10) {
                time.setText(("" + mins + ":0" + Integer.toString(secs % 60)));
            }
            else {
                time.setText(("" + mins + ":" + Integer.toString(secs % 60)));
            }
            timeHandler.postDelayed(this, 0);
        }
    };

    public int calculateScore(String sTime) {
        String[] hms = sTime.split(":");
        int hours = 0;
        int minutes = 0;
        int secs = 0;

        if(hms.length == 3) {
            hours = Integer.parseInt(hms[0]);
            minutes = Integer.parseInt(hms[1]);
            secs = Integer.parseInt(hms[2]);
        }
        else if(hms.length == 2) {
            minutes = Integer.parseInt(hms[0]);
            secs = Integer.parseInt(hms[1]);
        }

        int duration = (3600 * hours) + (60 * minutes) + secs;
        return duration * 10;

    }

    public void uploadScore() {
        PuzzleCode pc = PuzzleCode.getInstance();
        if (pc.isSet()) {
            (new Thread(new UploadScoreJSON(pc.getTypeCode(), pc.numericCode(),
                    score, getApplicationContext())))
                    .start();
        }
    }


    public void switchToMain() {
        finish();
        Intent intent = new Intent(DotToDotView.this, MainActivity.class);
        startActivity(intent);
    }

    public void showSolution(Button sh) {
        sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showCount == 0) {
                    showCount++;
                    dv.setBackgroundBitmap(scaledImg);
                    dv.invalidate();
                }
                else {
                    showCount = 0;
                    dv.setBackgroundBitmap(null);
                    dv.invalidate();
                }
            }
        });

    }

}
