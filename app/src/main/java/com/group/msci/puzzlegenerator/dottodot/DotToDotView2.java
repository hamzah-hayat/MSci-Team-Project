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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotView2 extends Activity implements View.OnTouchListener {
    private static final int SELECT_PHOTO = 100;
    float startX = 0, startY = 0, endX = 0, endY = 0;
    Canvas canvas;
    Paint paint;
    ImageView connDots;
    ArrayList<Dot> dots;
    float x , y;
    float startx, starty;
    boolean isDrawing = false;
    String puzzleWord;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_dot_to_dot_view2);
        Intent intent = getIntent();
        Uri myImageURI = intent.getParcelableExtra("FINAL_IMG_URI");
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(myImageURI);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap readImage = BitmapFactory.decodeStream(imageStream);
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
        Bitmap scaledImg = Bitmap.createScaledBitmap(edgImg, (dv.getLayoutParams().width)-50, dv.getLayoutParams().height, true);

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
        canvas = new Canvas(dotted);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        //canvas.drawBitmap(scaledImg, 0, 0, paint);
        for (int i = 0; i < dots.size(); i = i + 350) {
            Dot curr = dots.get(i);
            int x = curr.getxPos();
            int y = curr.getyPos();
            canvas.drawCircle(x, y, 5, paint);
            fDots.add(curr);
        }

        dv.setDots(fDots);
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
                if(input.equals(puzzleWord)) {
                    new AlertDialog.Builder(DotToDotView2.this)
                            .setTitle("Correct!")
                            .setMessage("You have guessed the image correctly. You done it in x sec")
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

        //mImg.setImageBitmap(dotted);

        //mImg.setOnTouchListener(this);


    }


    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                startx = x;
                starty = y;
                v.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                v.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                canvas.drawLine(startx, starty, x, y, paint);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void drawGUI() {}

    public void switchToMain() {}

    public void switchToCurrentPuzzle() {}

    public void showLeaderboard() {}

    public void showSolution() {}

}
