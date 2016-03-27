package com.group.msci.puzzlegenerator.dottodot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                            Bitmap scaledImg = Bitmap.createScaledBitmap(edgImg, 450, 450, true);
                            setContentView(R.layout.activity_dot_to_dot_view2);
                            ImageView mImg = (ImageView) findViewById(R.id.imageView2);
                            connDots = (ImageView) findViewById(R.id.imageView2);

                            mImg.setImageBitmap(scaledImg);
                            //setContentView(R.layout.dottodot_activity);

                            //Check each pixel to see if its white or black and store all white into ArrayList

                            dots = new ArrayList<>();

                            for (int x = 0; x < scaledImg.getWidth(); x++) {
                                for (int y = 0; y < scaledImg.getHeight(); y++) {
                                    if (scaledImg.getPixel(x, y) != Color.BLACK) {
                                        dots.add(new Dot(x, y));
                                    }
                                }
                            }

                            Bitmap dotted = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
                            canvas = new Canvas(dotted);
                            paint = new Paint();
                            paint.setColor(Color.GRAY);
                            //canvas.drawBitmap(scaledImg, 0, 0, paint);
                            for (int i = 0; i < dots.size(); i = i + 75) {
                                Dot curr = dots.get(i);
                                int x = curr.getxPos();
                                int y = curr.getyPos();
                                canvas.drawCircle(x, y, 5, paint);
                            }

                            mImg.setImageBitmap(dotted);

                            mImg.setOnTouchListener(this);

                            //setContentView(new DotsView(this, xCo, yCo));

                        }

                }
            } catch (FileNotFoundException e) {
            }

        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        int act = event.getAction();
        switch (act) {
            case MotionEvent.ACTION_DOWN:
                canvas.drawCircle(event.getRawX(), event.getRawY(), 5, paint);
                connDots.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
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
