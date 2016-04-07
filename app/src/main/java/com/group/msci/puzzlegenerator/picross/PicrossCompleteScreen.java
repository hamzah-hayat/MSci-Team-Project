package com.group.msci.puzzlegenerator.picross;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.json.UploadPuzzleJSON;

public class PicrossCompleteScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_complete_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView time = (TextView) findViewById(R.id.finalTime);
        Intent intent = getIntent();
        String timeStr = intent.getStringExtra("FINAL_TIME");
        System.out.println("THE PROBLEM CHILD: " + timeStr);
        time.setText(timeStr);
        Button sharePuzzle = (Button) findViewById(R.id.sharePuzzle);
        sharePuzzle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        //if (button.get.equals("Share Puzzle")) {
            UploadPuzzleJSON jsonGetter = new UploadPuzzleJSON('p', "gobble", "degook");
            Thread x = new Thread(jsonGetter);
            x.start();
            try {
                x.join();
            }
            catch (InterruptedException ex) {
                //do nothing
            }
        //}
    }
}
