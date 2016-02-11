package com.group.msci.puzzlegenerator.picross;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PicrossPuzzleGUI extends AppCompatActivity {

    protected PicrossPuzzle puzzle;
    protected ArrayList<ArrayList<ImageButton>> buttonList = new ArrayList<ArrayList<ImageButton>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_puzzle_gui);
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
        Intent intent = getIntent();
        Uri myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(myImageURI);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
        PicrossPuzzleGenerator puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 100, 100);
        puzzle = puzzleGen.createPuzzle();
        setButtons();
    }

    public void setPuzzle(PicrossPuzzle puzzleT) {
        puzzle = puzzleT;
    }

    public PicrossPuzzle getPuzzle() {
        return puzzle;
    }

    public void setButtons() {
        GridLayout grid = (GridLayout) findViewById(R.id.puzzleGrid);
        grid.setColumnCount(puzzle.grid.getGrid()[0].length);
        grid.setRowCount(puzzle.grid.getGrid().length);
        for (int i = 0; i < puzzle.grid.getGrid().length; i++) {
            buttonList.add(new ArrayList<ImageButton>());
            for (int j = 0; j < puzzle.grid.getGrid()[i].length; j++) {
                ImageButton button = new ImageButton(this);
                buttonList.get(i).add(button);
                grid.addView(button);
            }
        }
    }
}
