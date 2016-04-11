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
                //IMAGE SCRAPER WITH INTENT CHANGING TO DotToDotView2
            }
        });

        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
