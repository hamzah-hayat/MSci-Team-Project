package com.group.msci.puzzlegenerator.dottodot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotView extends Activity {
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        ImageView mImg = (ImageView) findViewById(R.id.imageView2);
        if (mImg == null) {
            Log.i("log0.1:", "error");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        Log.i("reqest code:", Integer.toString(requestCode));
        if (requestCode == SELECT_PHOTO && imageReturnedIntent != null) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            try {
                switch (requestCode) {
                    case SELECT_PHOTO:
                        if (resultCode == RESULT_OK) {
                            Log.i("log1:", "error");
                            Uri selectedImage = imageReturnedIntent.getData();
                            Log.i("log2:", "error");
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            Log.i("log3:", "error");
                            Bitmap readImage = BitmapFactory.decodeStream(imageStream);

                            if(readImage == null) {
                                Log.i("log null", "ERROR");
                            }
                            Log.i("log4:", "error");

                            AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
                            Log.i("log5:", "error");
                            det.setSourceImage(readImage);
                            Log.i("log6:", "error");
                            det.process();
                            Log.i("log7:", "error");
                            Bitmap edgImg = det.getEdgesImage();
                            Log.i("log8:", "error");
                            edgImg = Bitmap.createScaledBitmap(edgImg, 500, 500, true);
                            Log.i("log9:", "error");
                            LinearLayout v = (LinearLayout)findViewById(R.id.test);
                            ImageView mImg = (ImageView) v.findViewById(R.id.imageView2);
                            Log.i("log11:", "error");
                            if (edgImg == null || mImg == null) {
                                Log.i("Log NULL:", "NULL WTF");
                            }
                            mImg.setImageBitmap(edgImg);
                            Log.i("log12:", "error");



                            setContentView(R.layout.dottodot_activity);
                            Log.i("log13:", "error");


                        }
                }
            } catch (FileNotFoundException e) {
            }
            ;
        }
    }

    public void drawGUI() {}

    public void switchToMain() {}

    public void switchToCurrentPuzzle() {}

    public void showLeaderboard() {}

    public void showSolution() {}
}
