package com.group.msci.puzzlegenerator.dottodot;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.PixabayScraperJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by Mustafa on 11/04/2016.
 */
public class DotToDotMainScreen extends Activity {
    private MediaPlayer buttonPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dots_main);

        buttonPress = MediaPlayer.create(this, R.raw.buttonclick);
        ImageButton play = (ImageButton) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPress.start();
                InputStream in = getResources().openRawResource(R.raw.animalwords);
                PixabayScraperJSON scraper = new PixabayScraperJSON(in);
                scraper.setLines(227);
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

                        JSONArray allLinks = jsonFile.getJSONArray("hits");
                        int randCap = 0;
                        if (totalHits >= 20) {
                            randCap = 20;
                        } else {
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
                        } catch (IllegalArgumentException ex) {
                            new AlertDialog.Builder(DotToDotMainScreen.this)
                                    .setTitle("No Hits")
                                    .setMessage("There are no images for that particular word, press Play again to generate a new one")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
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
                buttonPress.start();
                Intent intent = new Intent(DotToDotMainScreen.this, DotToDotImageSelectType.class);
                startActivity(intent);
            }
        });

        ImageButton help = (ImageButton) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPress.start();
                Intent intent = new Intent(DotToDotMainScreen.this, DotToDotHelp.class);
                startActivity(intent);
            }
        });

        ImageButton score = (ImageButton) findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPress.start();
                Intent intent = new Intent(DotToDotMainScreen.this, DotToDotScoreboard.class);
                startActivity(intent);
            }
        });
    }

}
