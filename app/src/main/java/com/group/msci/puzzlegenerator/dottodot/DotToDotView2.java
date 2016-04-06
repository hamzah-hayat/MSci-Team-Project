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
import android.widget.ImageView;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotView2 extends Activity {
    private static final int SELECT_PHOTO = 100;
    ImageView connDots;
    ArrayList<Dot> dots;
    protected TextView time;
    private long startTime = 0L;
    private Handler timeHandler = new Handler();
    float x , y;
    float startx, starty;
    String puzzleWord;
    long timeInMS = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Bitmap readImage;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_dot_to_dot_view2);
        Intent intent = getIntent();
        if(intent.hasExtra("FINAL_IMG_URI")) {
            Uri myImageURI = intent.getParcelableExtra("FINAL_IMG_URI");
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(myImageURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            readImage = BitmapFactory.decodeStream(imageStream);
        }
        else if(intent.hasExtra("FINAL_IMG_URL")) {
            String finalURL = intent.getStringExtra("FINAL_IMG_URL");
            URLBitmap retImg = new URLBitmap(finalURL);
            Thread x = new Thread(retImg);
            x.start();
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readImage = retImg.getrImg();
        }
        puzzleWord = intent.getStringExtra("WORD_STRING");

        AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
        det.setSourceImage(readImage);
        det.process();
        Bitmap edgImg = det.getEdgesImage();
        //ImageView mImg = (ImageView) findViewById(R.id.imageView2);
        //connDots = (ImageView) findViewById(R.id.imageView2);
        //mImg.setImageBitmap(scaledImg);
        //setContentView(R.layout.dottodot_activity);

        DotsView dv = (DotsView) findViewById(R.id.dotsView2);
        Bitmap scaledImg = Bitmap.createScaledBitmap(edgImg, dv.getLayoutParams().width, dv.getLayoutParams().height, true);
        Log.i("orig width", Integer.toString(dv.getLayoutParams().width));
        Log.i("orig length", Integer.toString(dv.getLayoutParams().height));

        dots = new ArrayList<>();
        ArrayList<Dot> fDots = new ArrayList<>();
        for (int x = 0; x < scaledImg.getWidth(); x++) {
            for (int y = 0; y < scaledImg.getHeight(); y++) {
                if (scaledImg.getPixel(x, y) != Color.BLACK) {
                    dots.add(new Dot(x, y));
                }
            }
        }

        Bitmap dotted = Bitmap.createBitmap(dv.getLayoutParams().width, dv.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        //canvas.drawBitmap(scaledImg, 0, 0, paint);
        fDots.add(dots.get(0));
        for (int i = 0; i < dots.size(); i = i + 250) {
            Dot cDot = dots.get(i);
            fDots.add(cDot);
        }

        dv.setDots(fDots);
        dv.removeEdgeDots();
        dv.setvWidth(dv.getLayoutParams().width);
        dv.setvLength(dv.getLayoutParams().height);

        dv.removeOverlappingDots(); //VERY INEFFICIENT USE ITERATOR
        dv.removeOverlappingDots();
        dv.removeOverlappingDots();
        dv.removeOverlappingDots();
        dv.removeOverlappingDots();

        Button exitButton = (Button) findViewById(R.id.button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DotToDotView2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button guessButton = (Button) findViewById(R.id.button4);
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputText = (EditText) findViewById(R.id.editText);
                String input = inputText.getText().toString();
                CharSequence timeSeq = time.getText();
                String strTime = timeSeq.toString();
                if(input.equals(puzzleWord)) {
                    new AlertDialog.Builder(DotToDotView2.this)
                            .setTitle("Correct!")
                            .setMessage("You have guessed the image correctly. You done it in " + strTime)
                                    .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(DotToDotView2.this, DotToDotImageSelectType.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNeutralButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DotToDotView2.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                else {
                    new AlertDialog.Builder(DotToDotView2.this)
                            .setTitle("Incorrect!")
                            .setMessage("The answer you have given is incorrect")
                            .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    inputText.setText("");
                                }
                            })
                            .setPositiveButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DotToDotView2.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        });

        time = (TextView) findViewById(R.id.textView10);
        startTime = SystemClock.uptimeMillis();
        timeHandler.postDelayed(updateTimerThread, 0);

        //mImg.setImageBitmap(dotted);

        //mImg.setOnTouchListener(this);
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
