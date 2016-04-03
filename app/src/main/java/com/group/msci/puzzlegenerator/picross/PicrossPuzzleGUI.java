package com.group.msci.puzzlegenerator.picross;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

import bolts.Task;

public class PicrossPuzzleGUI extends AppCompatActivity implements View.OnClickListener {

    protected PicrossPuzzle puzzle;
    protected ArrayList<ArrayList<ImageButton>> buttonList = new ArrayList<ArrayList<ImageButton>>();
    protected GridLayout buttonGrid;
    protected boolean shading = true;
    protected TextView timer;
    private long startTime = 0L;
    private Handler timeHandler = new Handler();
    long timeInMS = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_puzzle_gui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        puzzleGen.setThreshold(intent.getIntExtra("THRESHOLD", 125));
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
        timer = (TextView) findViewById(R.id.timerView);
        startTime = SystemClock.uptimeMillis();
        timeHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMS = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMS;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            if (secs % 60 < 10) {
                timer.setText(("" + mins + ":0" + Integer.toString(secs % 60)));
            }
            else {
                timer.setText(("" + mins + ":" + Integer.toString(secs % 60)));
            }
            timeHandler.postDelayed(this, 0);
        }
    };

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
        buttonGrid.setColumnCount(puzzle.getWidth() + puzzle.getLargestClueRow());
        buttonGrid.setRowCount(puzzle.getHeight() + puzzle.getLargestClueColumn());
        buildGrid();
    }

    public void buildGrid() {
        //alright, starting from last working build.
        //now, the way this needs to be done is to build the clues up iteratively.
        //so, that means the first things I'm adding are blank spaces, as I'm adding left-right
        //so first for loop:
        for (int i = 0; i < puzzle.getLargestClueColumn(); i++) {
            for (int j = 0; j < puzzle.getLargestClueRow(); j++) {
                TextView blank = new TextView(this);
                blank.setText(" ");
                buttonGrid.addView(blank);
            }
            for (int j = puzzle.getLargestClueRow(); j < puzzle.getWidth() + puzzle.getLargestClueRow(); j++) {
                if (puzzle.getPuzzleCluesColumns().get(j - puzzle.getLargestClueRow()).size() <= i || puzzle.getPuzzleCluesColumns().get(j - puzzle.getLargestClueRow()).size() == 0) {
                    TextView columnClue = new TextView(this);
                    buttonGrid.addView(columnClue);
                }
                else {
                    TextView columnClue = new TextView(this);
                    System.out.println(i + " " + j);
                    columnClue.setText(Integer.toString(puzzle.getPuzzleCluesColumns().get(j - puzzle.getLargestClueRow()).get(i)));
                    columnClue.setGravity(Gravity.CENTER);
                    buttonGrid.addView(columnClue);
                }
            }
        }
        //boom, success on column clues! :O
        //and now for row clues. these will be more complex, as they'll have to be put in with the buttons
        //however, it's very possible, thankfully
        ArrayList<ImageButton> currentIArray;
        for (int i = 0; i < puzzle.getHeight(); i++) {
            for (int j = 0; j < puzzle.getWidth() + puzzle.getLargestClueRow(); j++) {
                if (j < puzzle.getLargestClueRow()) { //deal with clues
                    if (puzzle.getPuzzleCluesRows().get(i).size() <= j || puzzle.getPuzzleCluesRows().get(i).size() == 0) {
                        TextView columnClue = new TextView(this);
                        buttonGrid.addView(columnClue);
                    }
                    else {
                        TextView rowClue = new TextView(this);
                        rowClue.setText(Integer.toString(puzzle.getPuzzleCluesRows().get(i).get(j)) + " ");
                        rowClue.setGravity(Gravity.CENTER);
                        buttonGrid.addView(rowClue);
                    }
                }
                else {
                    buttonList.add(new ArrayList<ImageButton>());
                    currentIArray = buttonList.get(i);
                    PicrossSquare button = new PicrossSquare(this, i, j - puzzle.getLargestClueRow());
                    button.setBackgroundResource(R.drawable.unshaded);
                    button.setOnClickListener(this);
                    currentIArray.add(button);
                    buttonGrid.addView(button);
                }
            }
        }
    }

    public void shade (PicrossSquare button) {
        button.shade();
    }

    public void cross (PicrossSquare button) {
        button.cross();
    }

    public boolean checkAllAnswers() {
        int numberMissing = 0;
        for (int i = 0; i < puzzle.answerArray.length; i++) {
            for (int j = 0; j < puzzle.answerArray[i].length; j++) {
                if (puzzle.answerArray[i][j] != puzzle.currentAnswers[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof PicrossSquare) {
            PicrossSquare tile = (PicrossSquare) v;
            if (shading) {
                if (puzzle.checkIfCorrect((tile.getXPosition()), tile.getYPosition())) {
                    shade(tile);
                    if (checkAllAnswers()) {
                        gameOver();
                    }
                }
                else {
                    System.out.println("WRONG");
                    startTime -= 60000;
                }
            }
            else {
                cross(tile);
            }
        }
    }

    public void gameOver() { //everything accurate, get current time and upload score
        System.out.println("GAME OVER, YOU WIN");
        Intent intent = new Intent(PicrossPuzzleGUI.this, PicrossCompleteScreen.class);
        CharSequence timeSeq = timer.getText();
        String time = timeSeq.toString();
        intent.putExtra("FINAL_TIME", time);
        startActivity(intent);
    }
}
