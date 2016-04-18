package com.group.msci.puzzlegenerator.picross;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.dottodot.URLBitmap;

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
    String urlLink;
    EditText horizontalSize;
    EditText verticalSize;

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
        Bitmap yourSelectedImage = null;
        if (intent.hasExtra("URL_STRING")) {
            if (isNetConn()) {
                urlLink = intent.getStringExtra("URL_STRING");
                URLBitmap retImg = new URLBitmap(urlLink);
                Thread x = new Thread(retImg);
                x.start();
                try {
                    x.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                yourSelectedImage = retImg.getrImg();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error! No Internet!\nReturning you to main menu.");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PicrossPuzzleOptionsGUI.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
        }
        else {
            Uri myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
            image_uri = myImageURI;
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(myImageURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
        }

        original = Bitmap.createBitmap(yourSelectedImage);
        puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 25, 25);
        image = (ImageView) findViewById(R.id.previewImage);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageBitmap(yourSelectedImage);
        SeekBar thresholdSeek = (SeekBar) findViewById(R.id.threshold);
        verticalSize = (EditText) findViewById(R.id.picross_height);
        horizontalSize = (EditText) findViewById(R.id.picross_width);
        ImageButton previewButton = (ImageButton) findViewById(R.id.picross_preview);
        previewButton.setOnClickListener(this);
        ImageButton generateButton = (ImageButton) findViewById(R.id.picross_play);
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
                thresholdInt = progress;
            }
        });
    }

    @Override
    public void onClick(View v) {
        MediaPlayer buttonPress = MediaPlayer.create(this, R.raw.buttonclick);
        buttonPress.start();
        ImageButton button = (ImageButton) v;
        if (button.getId() == R.id.picross_preview) {
            puzzleGen.setForegroundImage(original);
            original = Bitmap.createBitmap(original);
            puzzleGen.setThreshold(thresholdInt);
            try {
                puzzleHeight = Integer.parseInt(verticalSize.getText().toString());
                puzzleWidth = Integer.parseInt(horizontalSize.getText().toString());
                pixelated = puzzleGen.pixelateImage(original, puzzleWidth, puzzleHeight);
                image.setImageBitmap(puzzleGen.binariseImage(pixelated));
            }
            catch (NumberFormatException ex) {
                puzzleHeight = 25;
                puzzleWidth = 25;
                pixelated = puzzleGen.pixelateImage(original, puzzleWidth, puzzleHeight);
                image.setImageBitmap(puzzleGen.binariseImage(pixelated));
            }
        }
        else {
            Intent intent = new Intent(PicrossPuzzleOptionsGUI.this, PicrossPuzzleGUI.class);
            intent.putExtra("THRESHOLD", thresholdInt);
            if (image_uri == null) {
                intent.putExtra("URL_STRING", urlLink);
            } else {
                intent.putExtra("SELECTED_IMAGE_URI", image_uri);
            }

            intent.putExtra("PUZZLE_WIDTH", puzzleWidth);
            intent.putExtra("PUZZLE_HEIGHT", puzzleHeight);
            startActivity(intent);
        }
    }

    public boolean isNetConn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }
}
