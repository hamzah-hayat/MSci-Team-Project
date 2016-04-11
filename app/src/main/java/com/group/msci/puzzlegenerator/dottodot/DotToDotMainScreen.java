package com.group.msci.puzzlegenerator.dottodot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.json.PixabayScraperJSON;
import com.group.msci.puzzlegenerator.picross.PicrossPuzzleOptionsGUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by Mustafa on 11/04/2016.
 */
public class DotToDotMainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dots_main);

        ImageButton play = (ImageButton) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Intent intent = new Intent(DotToDotMainScreen.this, DotToDotView.class);
                        intent.putExtra("URL_STRING_RAND", imageURL);
                        intent.putExtra("ANSWER", scraper.getNoun());
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

        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(DotToDotMainScreen.this, DotToDotImageSelectType.class);
                startActivity(intent);
            }
        });

        ImageButton help = (ImageButton) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(DotToDotMainScreen.this, DotToDotImageSelectType.class);
                //startActivity(intent);
            }
        });

        ImageButton score = (ImageButton) findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(DotToDotMainScreen.this, DotToDotImageSelectType.class);
                //startActivity(intent);
            }
        });
    }

}
