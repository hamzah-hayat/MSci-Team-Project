package com.group.msci.puzzlegenerator.dottodot;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.json.UploadPuzzleJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Mustafa on 28/03/2016.
 */
public class DotToDotPreviewAndWord extends AppCompatActivity implements View.OnClickListener {
    protected Bitmap readImage;
    protected Uri myImageURI;
    protected String urlLink;
    protected boolean isGallery = false;
    protected ArrayList<Dot> dots;
    private String puzzleDataD = "";
    private int shareCode;
    private ArrayList<Dot> passDot;
    private String puzData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dots_preplay);
        Intent intent = getIntent();

        if(intent.hasExtra("SELECTED_IMAGE_URI")) {
            myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
            isGallery = true;
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(myImageURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            readImage = BitmapFactory.decodeStream(imageStream);
        }
        else if(intent.hasExtra("URL_STRING")){
            urlLink = intent.getStringExtra("URL_STRING");
            URLBitmap retImg = new URLBitmap(urlLink);
            Thread x = new Thread(retImg);
            x.start();
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readImage = retImg.getrImg();
        }


        AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
        det.setSourceImage(readImage);
        det.process();
        Bitmap edgImg = det.getEdgesImage();

        DotsView dotV = (DotsView) findViewById(R.id.dotsView);
        Bitmap scaledImg = Bitmap.createScaledBitmap(edgImg, dotV.getLayoutParams().width, dotV.getLayoutParams().height, true);

        dots = new ArrayList<>();
        ArrayList<Dot> fDots = new ArrayList<>();
        for (int x = 0; x < scaledImg.getWidth(); x++) {
            for (int y = 0; y < scaledImg.getHeight(); y++) {
                if (scaledImg.getPixel(x, y) != Color.BLACK) {
                    dots.add(new Dot(x, y));
                }
            }
        }

        fDots.add(dots.get(0));
        for (int i = 0; i < dots.size(); i = i + 150) {
            Dot cDot = dots.get(i);
            fDots.add(cDot);
        }

        dotV.setDots(fDots);
        dotV.removeEdgeDots();
        dotV.removeOverlappingDots();


        passDot = dotV.getDots();


        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            EditText word = (EditText) findViewById(R.id.dots_answer);
            if(word.getText().toString().equals("")) {
                new AlertDialog.Builder(DotToDotPreviewAndWord.this)
                        .setTitle("No input given")
                        .setMessage("Please enter a word to use as the answer for the image")
                        .setNeutralButton("Close", null).show();
            }
            else {
                for(int i = 0; i < passDot.size(); i++) {
                    Dot temp = passDot.get(i);
                    puzzleDataD += temp.getxPos() + "," + temp.getyPos() +";";
                }
                puzzleDataD += word.getText().toString() + ";";
                System.out.println(puzzleDataD);
                UploadPuzzleJSON jsonGetter = new UploadPuzzleJSON('d', puzzleDataD, "hello, world!");
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
                    }
                    else {
                        System.out.println("Failed!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Please Enter Code Here");
                TextView input = new TextView(this);
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
                input.setText("Puzzle Code: d" + prefix + shareCode);
                builder.setView(input);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DotToDotPreviewAndWord.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }

    }

}
