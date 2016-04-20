package com.group.msci.puzzlegenerator.picross;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

public class PicrossMainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picross_main);
        ImageButton playButton = (ImageButton) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PicrossMainMenu.this, PicrossImageSelectType.class);
                startActivity(intent);
            }
        });
        ImageButton helpButton = (ImageButton) findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PicrossMainMenu.this, PicrossHelpPage.class);
                startActivity(intent);
            }
        });
        ImageButton scoreboardButton = (ImageButton) findViewById(R.id.score);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PicrossMainMenu.this, PicrossScoreboard.class);
                startActivity(intent);
            }
        });
    }
}
