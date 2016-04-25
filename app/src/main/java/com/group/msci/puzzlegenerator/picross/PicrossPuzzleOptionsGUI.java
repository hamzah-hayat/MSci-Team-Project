package com.group.msci.puzzlegenerator.picross;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.group.msci.puzzlegenerator.foreground.ForegroundDetection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class PicrossPuzzleOptionsGUI extends AppCompatActivity implements View.OnClickListener {
    PicrossPuzzleGenerator puzzleGen;
    Bitmap selectedImage;
    Bitmap original;
    ImageView image;
    SeekBar thresholdSeek;
    int thresholdInt;
    Uri image_uri;
    Bitmap pixelated;
    int puzzleWidth = 15;
    int puzzleHeight = 15;
    String urlLink;
    ProgressDialog pd;

    class FDTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(PicrossPuzzleOptionsGUI.this);
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
                    return selectedImage;
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
            Bitmap ySI = yourSelectedImage.copy(Bitmap.Config.ARGB_8888, true);
            InputStream in = getResources().openRawResource(R.raw.network);
            System.out.println("START FOREGROUND");
            ForegroundDetection fd = new ForegroundDetection(in);
            fd.setBackground(Color.WHITE);
            fd.setOutline(true);
            try {
                yourSelectedImage = fd.getForeground(ySI);
            } catch(IOException e) {
                e.printStackTrace();
            }
            return yourSelectedImage;
        }

        @Override
        protected void onPostExecute(Bitmap ySI) {
            // Pass the result data back to the main activity
            PicrossPuzzleOptionsGUI.this.selectedImage = ySI;
            if (PicrossPuzzleOptionsGUI.this.pd != null) {
                PicrossPuzzleOptionsGUI.this.pd.dismiss();
            }
            setContentView(R.layout.activity_picross_puzzle_options_gui);
            startPreviewer();
            return;
        }
    }

    public void startPreviewer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        original = Bitmap.createBitmap(selectedImage);
        puzzleGen = new PicrossPuzzleGenerator(selectedImage, 25, 25);
        image = (ImageView) findViewById(R.id.previewImage);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageBitmap(selectedImage);
        SeekBar thresholdSeek = (SeekBar) findViewById(R.id.threshold);
        Button small = (Button) findViewById(R.id.picross_small);
        Button medium = (Button) findViewById(R.id.picross_medium);
        Button large = (Button) findViewById(R.id.picross_large);
        small.setOnClickListener(this);
        medium.setOnClickListener(this);
        large.setOnClickListener(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_puzzle_options_gui);
        this.pd = ProgressDialog.show(this, "Foreground Extraction",
                "Loading... Please wait!\nThis can take up to a minute!", true, false);
        // Start a new thread that will download all the data
        new FDTask().execute();
    }

    @Override
    public void onClick(View v) {
        MediaPlayer buttonPress = MediaPlayer.create(this, R.raw.buttonclick);
        buttonPress.start();
        if (v.getId() == R.id.picross_preview) {
            puzzleGen.setForegroundImage(original);
            original = Bitmap.createBitmap(original);
            puzzleGen.setThreshold(thresholdInt);
            try {
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
        else if (v.getId() == R.id.picross_small) {
            puzzleHeight = 5;
            puzzleWidth = 5;
        }
        else if (v.getId() == R.id.picross_medium) {
            puzzleHeight = 10;
            puzzleWidth = 10;
        }
        else if (v.getId() == R.id.picross_large) {
            puzzleHeight = 15;
            puzzleWidth = 15;
            System.out.println("Large!");
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
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }
}
