package com.group.msci.puzzlegenerator.picross;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PicrossPuzzleGUI extends AppCompatActivity implements View.OnClickListener {

    protected PicrossPuzzle puzzle;
    protected ArrayList<ArrayList<ImageButton>> buttonList = new ArrayList<ArrayList<ImageButton>>();
    protected GridLayout buttonGrid;
    protected boolean shading = true;

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
        PicrossPuzzleGenerator puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 25, 25);
        puzzle = puzzleGen.createPuzzle();
        setButtons();
        /*ImageView view = (ImageView) findViewById(R.id.testingImage);
        view.setImageBitmap(puzzle.foregroundImage);*/
        Button shade = (Button) findViewById(R.id.shade);
        shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shading = true;
            }
        });
        Button cross = (Button) findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shading = false;
            }
        });
    }

    public void setPuzzle(PicrossPuzzle puzzleT) {
        puzzle = puzzleT;
    }

    public PicrossPuzzle getPuzzle() {
        return puzzle;
    }

    public void setButtons() {
        ZoomView grid = (ZoomView) findViewById(R.id.puzzleGrid);
        buttonGrid = new GridLayout(this);
        grid.addView(buttonGrid);
        buttonGrid.setColumnCount(puzzle.getWidth());
        buttonGrid.setRowCount(puzzle.getHeight());
        buildClues();
        buildGrid();
    }

    public void buildGrid() {
        ArrayList<ImageButton> currentIArray;
        int iLimit = puzzle.getWidth();
        int jLimit = puzzle.getHeight();
        for (int i = 0; i < puzzle.getHeight(); i++) {
            buttonList.add(new ArrayList<ImageButton>());
            currentIArray = buttonList.get(i);
            for (int j = 0; j < puzzle.getWidth(); j++) {
                final PicrossSquare button = new PicrossSquare(this, i, j);
                button.setBackgroundResource(R.drawable.unshaded);
                button.setOnClickListener(this);
                currentIArray.add(button);
                buttonGrid.addView(button);
            }
        }
    }

    public void buildClues() {

    }

    public void shade (ImageButton button) {
        button.setBackgroundResource(R.drawable.shaded);
    }

    public void cross (ImageButton button) {
        button.setBackgroundResource(R.drawable.crossed);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof PicrossSquare) {
            PicrossSquare tile = (PicrossSquare) v;
            if (shading) {
                if (puzzle.checkIfCorrect((tile.getXPosition()), tile.getyPosition())) {
                    shade(tile);
                }
                else {
                    //penalise score
                }
                //tile.setBackground(new BitmapDrawable(puzzle.foregroundImage));
            }
            else {
                cross(tile);
            }
        }
        for (int i = 0; i < puzzle.getHeight(); i++) {
            System.out.print("[");
            for (int j = 0; j < puzzle.getWidth(); j++) {
                System.out.print(puzzle.answerArray[i][j] + ", ");
            }
            System.out.println("]");
        }
    }
}
