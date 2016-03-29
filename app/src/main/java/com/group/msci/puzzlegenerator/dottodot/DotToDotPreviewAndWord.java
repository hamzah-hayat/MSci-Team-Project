package com.group.msci.puzzlegenerator.dottodot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Mustafa on 28/03/2016.
 */
public class DotToDotPreviewAndWord extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_to_dot_preview_and_word);
        Intent intent = getIntent();
        final Uri myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(myImageURI);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap readImage = BitmapFactory.decodeStream(imageStream);
        Bitmap scaledImg = Bitmap.createScaledBitmap(readImage, 450, 450, true);
        ImageView mImg = (ImageView) findViewById(R.id.imageView);
        mImg.setImageBitmap(scaledImg);

        Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText word = (EditText) findViewById(R.id.editText);
                if(word.getText().toString().equals("")) {
                    new AlertDialog.Builder(DotToDotPreviewAndWord.this).setTitle("No input given").setMessage("Please enter a word to use as the answer for the image").setNeutralButton("Close", null).show();
                }
                else {
                    Intent intent = new Intent(DotToDotPreviewAndWord.this, DotToDotView2.class);
                    intent.putExtra("FINAL_IMG_URI", myImageURI);
                    intent.putExtra("WORD_STRING", word.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}