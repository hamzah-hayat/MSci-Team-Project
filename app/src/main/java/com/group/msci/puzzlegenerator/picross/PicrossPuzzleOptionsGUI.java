package com.group.msci.puzzlegenerator.picross;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class PicrossPuzzleOptionsGUI extends AppCompatActivity implements View.OnClickListener {
    PicrossPuzzleGenerator puzzleGen;
    Bitmap original;
    ImageView image;
    SeekBar thresholdSeek;
    int thresholdInt;
    Uri image_uri;
    Bitmap pixelated;
    int puzzleWidth;
    int puzzleHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_puzzle_options_gui);
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
        Intent intent = getIntent();
        Uri myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
        image_uri = myImageURI;
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(myImageURI);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
        original = Bitmap.createBitmap(yourSelectedImage);
        puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 25, 25);
        image = (ImageView) findViewById(R.id.previewImage);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageBitmap(yourSelectedImage);
        SeekBar thresholdSeek = (SeekBar) findViewById(R.id.selectThreshold);
        SeekBar verticalSeek = (SeekBar) findViewById(R.id.selectVerticalSize);
        SeekBar horizontalSeek = (SeekBar) findViewById(R.id.selectHorizontalSize);
        final TextView displayThreshold = (TextView) findViewById(R.id.displayThreshold);
        final TextView displayVertical = (TextView) findViewById(R.id.displayVerticalSize);
        final TextView displayHorizontal = (TextView) findViewById(R.id.displayHorizontalSize);
        Button previewButton = (Button) findViewById(R.id.previewButton);
        previewButton.setOnClickListener(this);
        Button generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(this);
        thresholdSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String threshold = String.valueOf(progress);
                if (threshold.length() == 1) {
                    threshold = "00" + threshold;
                } else if (threshold.length() == 2) {
                    threshold = "0" + threshold;
                }
                displayThreshold.setText(threshold);
                thresholdInt = progress;
            }
        });
        verticalSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String height = String.valueOf(progress);
                if (height.length() == 1) {
                    height = "0" + height;
                }
                displayVertical.setText(height);
                puzzleHeight = progress;
            }
        });
        horizontalSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String width = String.valueOf(progress);
                if (width.length() == 1) {
                    width = "0" + width;
                }
                displayHorizontal.setText(width);
                puzzleWidth = progress;
            }
        });

    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        if (button.getText().equals("Preview")) {
            puzzleGen.setForegroundImage(original);
            original = Bitmap.createBitmap(original);
            puzzleGen.setThreshold(thresholdInt);
            pixelated = puzzleGen.pixelateImage(original, puzzleWidth, puzzleHeight);
            image.setImageBitmap(puzzleGen.binariseImage(pixelated));
        }
        else {
            Intent intent = new Intent(PicrossPuzzleOptionsGUI.this, PicrossPuzzleGUI.class);
            intent.putExtra("THRESHOLD", thresholdInt);
            intent.putExtra("SELECTED_IMAGE_URI", image_uri);
            intent.putExtra("PUZZLE_WIDTH", puzzleWidth);
            intent.putExtra("PUZZLE_HEIGHT", puzzleHeight);
            startActivity(intent);
        }
    }
}
