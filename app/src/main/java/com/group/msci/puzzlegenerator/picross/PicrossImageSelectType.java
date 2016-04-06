package com.group.msci.puzzlegenerator.picross;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.R;

public class PicrossImageSelectType extends Activity {

    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_image_select_type);
        Button galleryButton = (Button) findViewById(R.id.galleryBtn);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        Button savedPuzzles = (Button) findViewById(R.id.savedPuzzles);
        savedPuzzles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent savedPuzzlesIntent = new Intent(PicrossImageSelectType.this, PicrossSavedPuzzles.class);
                startActivity(savedPuzzlesIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            switch(requestCode) {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Intent intent = new Intent(PicrossImageSelectType.this, PicrossPuzzleOptionsGUI.class);
                        intent.putExtra("SELECTED_IMAGE_URI", selectedImage);
                        startActivity(intent);
                    }
            }
    }
}
