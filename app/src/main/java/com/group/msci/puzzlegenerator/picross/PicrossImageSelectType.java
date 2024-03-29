package com.group.msci.puzzlegenerator.picross;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.PixabayScraperJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;

public class PicrossImageSelectType extends Activity {

    private static final int SELECT_PHOTO = 100;
    private EditText link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picross_create);
        ImageButton galleryButton = (ImageButton) findViewById(R.id.galleryBtn);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        ImageButton linkButton = (ImageButton) findViewById(R.id.weblink);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PicrossImageSelectType.this);
                builder.setTitle("Enter URL");
                builder.setMessage("Enter the URL of the image");
                link = new EditText(PicrossImageSelectType.this);
                builder.setView(link);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetConn()) {
                            if (URLUtil.isValidUrl(link.getText().toString())) {
                                Intent intent = new Intent(PicrossImageSelectType.this, PicrossPuzzleOptionsGUI.class);
                                intent.putExtra("URL_STRING", link.getText().toString());
                                startActivity(intent);
                            } else {
                                new AlertDialog.Builder(PicrossImageSelectType.this)
                                        .setTitle("Invalid URL")
                                        .setMessage("Please check your URL again and make sure you entered it correctly")
                                        .setNeutralButton("Close", null)
                                        .show();
                            }
                        } else {
                            new AlertDialog.Builder(PicrossImageSelectType.this)
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
        ImageButton randButton = (ImageButton) findViewById(R.id.random);
        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream in = getResources().openRawResource(R.raw.noundata);
                PixabayScraperJSON scraper = new PixabayScraperJSON(in);
                Thread x = new Thread(scraper);
                x.start();
                try {
                    x.join();
                }
                catch (InterruptedException ex) {
                    //do nothing
                }
                JSONObject jsonFile = scraper.getJSON();
                try {
                    int totalHits = jsonFile.getInt("totalHits");
                    System.out.println(totalHits);
                    if (totalHits == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PicrossImageSelectType.this);
                        builder.setTitle("No Image Found");
                        builder.setMessage("No image found for chosen word, please try again!");
                        builder.setPositiveButton("Okay", null);
                        AlertDialog al = builder.create();
                        al.show();
                    }
                    JSONArray allLinks = jsonFile.getJSONArray("hits");
                    int randCap = 0;
                    if (totalHits >= 20) {
                        randCap = 20;
                    }
                    else {
                        randCap = totalHits;
                    }
                    try {
                        int randNum = new Random().nextInt(randCap);
                        JSONObject imageJson = (JSONObject) allLinks.get(randNum);
                        String imageURL = imageJson.getString("webformatURL");
                        Intent intent = new Intent(PicrossImageSelectType.this, PicrossPuzzleOptionsGUI.class);
                        intent.putExtra("URL_STRING", imageURL);
                        startActivity(intent);
                    }
                    catch (IllegalArgumentException ex) {
                        //alert dialog here, 0 results found
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
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

    public boolean isNetConn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }
}