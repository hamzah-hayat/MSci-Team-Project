package com.group.msci.puzzlegenerator.dottodot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotView2 extends Activity {
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

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
                            Uri selectedImage = imageReturnedIntent.getData();
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap readImage = BitmapFactory.decodeStream(imageStream);


                            AndroidCannyEdgeDetector det = new AndroidCannyEdgeDetector();
                            det.setSourceImage(readImage);
                            det.process();
                            Bitmap edgImg = det.getEdgesImage();
                            edgImg = Bitmap.createScaledBitmap(edgImg, 900, 900, true);
                            setContentView(R.layout.activity_dot_to_dot_view2);
                            ImageView mImg = (ImageView) findViewById(R.id.imageView2);

                            mImg.setImageBitmap(edgImg);
                            //setContentView(R.layout.dottodot_activity);

                            //Check each pixel to see if its white or black and store all white into ArrayList

                            ArrayList<Integer> xCo = new ArrayList<>();
                            ArrayList<Integer> yCo = new ArrayList<>();


                            for(int x = 0; x < edgImg.getWidth(); x++) {
                                for(int y = 0; y < edgImg.getHeight(); y++) {
                                    if(edgImg.getPixel(x, y) == Color.WHITE) {
                                       xCo.add(x);
                                       yCo.add(y);
                                    }
                                }
                            }

                            setContentView(new DotsView(this, xCo, yCo));


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
