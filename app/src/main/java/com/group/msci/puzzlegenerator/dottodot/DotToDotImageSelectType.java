package com.group.msci.puzzlegenerator.dottodot;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Mustafa on 27/03/2016.
 */
public class DotToDotImageSelectType extends Activity {
    private static final int SELECT_PHOTO = 100;
    private EditText link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dottodot_image_select_type);
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
        Button linkButton = (Button) findViewById(R.id.button3);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DotToDotImageSelectType.this);
                builder.setTitle("Enter URL");
                builder.setMessage("Enter the URL of the image");
                link = new EditText(DotToDotImageSelectType.this);
                builder.setView(link);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetConn()) {
                            if (URLUtil.isValidUrl(link.getText().toString())) {
                                Intent intent = new Intent(DotToDotImageSelectType.this, DotToDotPreviewAndWord.class);
                                intent.putExtra("URL_STRING", link.getText().toString());
                                startActivity(intent);
                            } else {
                                new AlertDialog.Builder(DotToDotImageSelectType.this)
                                        .setTitle("Invalid URL")
                                        .setMessage("Please check your URL again and make sure you entered it correctly")
                                        .setNeutralButton("Close", null)
                                        .show();
                            }
                        } else {
                            new AlertDialog.Builder(DotToDotImageSelectType.this)
                                    .setTitle("No Internet Connection")
                                    .setMessage("You currently have no internet connection, please reconnect and try again")
                                    .setNeutralButton("Close", null)
                                    .show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();

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
                    Intent intent = new Intent(DotToDotImageSelectType.this, DotToDotPreviewAndWord.class);
                    intent.putExtra("SELECTED_IMAGE_URI", selectedImage);
                    startActivity(intent);
                }
        }
    }

    public boolean isNetConn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }
}
