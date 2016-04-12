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
    ArrayList<Dot> pDots;
    private Bitmap readImage;
    private String urlLink;
    //private Bitmap readImage;
    private String data;
    private ArrayList<Dot> allDots;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dots_play);
        Intent intent = getIntent();
        DotsView dv = (DotsView) findViewById(R.id.dotsView2);
        if(intent.hasExtra("ANSWER_ARRAY")) {
            data = intent.getStringExtra("ANSWER_ARRAY");
            dataSplit = data.split(";");
            puzzleWord = dataSplit[dataSplit.length-1].toLowerCase();
            pDots = new ArrayList<>();
            for(int i = 0; i < dataSplit.length-2; i++) {
                String[] xyPair = dataSplit[i].split(",");
                pDots.add(new Dot(Integer.parseInt(xyPair[0]), Integer.parseInt(xyPair[1])));
            }
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
            readImage = retImg.getrImg();

            InputStream in = getResources().openRawResource(R.raw.network);
            ForegroundDetection fd = new ForegroundDetection(in);
            Bitmap mutableImg = readImage.copy(Bitmap.Config.ARGB_8888, true);
            try {
                mutableImg = fd.getForeground(mutableImg);
            } catch(IOException e) {
                e.printStackTrace();
            }

            AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
            det.setSourceImage(mutableImg);
            det.process();
            Bitmap edgImg = det.getEdgesImage();
            det=null;
            System.gc();

            Bitmap scaledImg = Bitmap.createScaledBitmap(edgImg, dv.getLayoutParams().width, dv.getLayoutParams().height, true);

            allDots = new ArrayList<>();
            for (int x = 0; x < scaledImg.getWidth(); x++) {
                for (int y = 0; y < scaledImg.getHeight(); y++) {
                    if (scaledImg.getPixel(x, y) != Color.BLACK) {
                        allDots.add(new Dot(x, y));
                    }
                }
            }
            pDots = new ArrayList<>();
            for (int i = 0; i < allDots.size(); i = i + 100) {
                Dot cDot = allDots.get(i);
                pDots.add(cDot);
            }
        }


        dv.setDots(pDots);
        if(intent.hasExtra("URL_STRING_RAND")) {
            dv.removeEdgeDots();
            dv.removeOverlappingDots();

        }

        ImageButton exit = (ImageButton) findViewById(R.id.dots_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readImage=null;
                allDots=null;
                System.gc();
                finish();
                Intent intent = new Intent(DotToDotView.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton guess = (ImageButton) findViewById(R.id.dots_guess);
        guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputText = (EditText) findViewById(R.id.dots_answer);
                String input = inputText.getText().toString();
                CharSequence timeSeq = time.getText();
                String strTime = timeSeq.toString();
                if(input.equals(puzzleWord)) {
                    new AlertDialog.Builder(DotToDotView.this)
                            .setTitle("Correct!")
                            .setMessage("You have guessed the image correctly. You completed the puzzle in " + strTime)
                                    .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(DotToDotView.this, DotToDotMainScreen.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNeutralButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DotToDotView.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                else {
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
                                    Intent intent = new Intent(DotToDotView.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        });

        time = (TextView) findViewById(R.id.time);
        startTime = SystemClock.uptimeMillis();
        timeHandler.postDelayed(updateTimerThread, 0);

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


    public void drawGUI() {}

    public void switchToMain() {}

    public void switchToCurrentPuzzle() {}

    public void showLeaderboard() {}

    public void showSolution() {}

}
