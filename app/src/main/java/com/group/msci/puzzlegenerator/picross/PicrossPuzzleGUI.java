package com.group.msci.puzzlegenerator.picross;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
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
import com.group.msci.puzzlegenerator.dottodot.URLBitmap;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

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
        setContentView(R.layout.picross_play);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String answerArrayStr = intent.getStringExtra("ANSWER_ARRAY");
        if (answerArrayStr == null) {
            if (intent.hasExtra("SELECTED_IMAGE_URI")) {
                Uri myImageURI = intent.getParcelableExtra("SELECTED_IMAGE_URI");
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(myImageURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                PicrossPuzzleGenerator puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 5, 5);
                puzzleGen.setThreshold(intent.getIntExtra("THRESHOLD", 125));
                puzzleGen.setPuzzleWidth(intent.getIntExtra("PUZZLE_WIDTH", 25));
                puzzleGen.setPuzzleHeight(intent.getIntExtra("PUZZLE_HEIGHT", 25));
                puzzle = puzzleGen.createPuzzle();
                System.out.println("PUZZLE NOT LOADED FROM DB, USED GALLERY");
            }
            else {
                String urlLink = intent.getStringExtra("URL_STRING");
                System.out.println("urlLink = " + urlLink);
                URLBitmap retImg = new URLBitmap(urlLink);
                Thread x = new Thread(retImg);
                x.start();
                try {
                    x.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //null error here?
                Bitmap yourSelectedImage = retImg.getrImg();
                if (yourSelectedImage == null) {
                    System.out.println("WHY IS THIS THING NULL?!");
                }
                PicrossPuzzleGenerator puzzleGen = new PicrossPuzzleGenerator(yourSelectedImage, 5, 5);
                puzzleGen.setThreshold(intent.getIntExtra("THRESHOLD", 125));
                puzzle = puzzleGen.createPuzzle();
                System.out.println("PUZZLE NOT LOADED FROM DB, USED INTERNET");
            }
        }
        else {
            String[] brokenUp = answerArrayStr.split(";");
            boolean[][] loadedAnswerArray = new boolean[brokenUp.length][brokenUp[0].length()];
            for (int i = 0; i < loadedAnswerArray.length; i++) {
                for (int j = 0; j < loadedAnswerArray[i].length; j++) {
                    if (brokenUp[i].charAt(j) == '0') {
                        loadedAnswerArray[i][j] = false;
                    }
                    else {
                        loadedAnswerArray[i][j] = true;
                    }
                }
            }
            puzzle = new PicrossPuzzle(loadedAnswerArray);
        }
        setButtons();
        /*ImageView view = (ImageView) findViewById(R.id.testingImage);
        view.setImageBitmap(puzzle.foregroundImage);*/
        ImageButton shade = (ImageButton) findViewById(R.id.shade);
        final MediaPlayer buttonPress = MediaPlayer.create(this, R.raw.buttonclick);
        shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPress.start();
                shading = true;
            }
        });
        ImageButton cross = (ImageButton) findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPress.start();
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
        buttonGrid.setColumnCount(puzzle.getWidth() + 1);
        buttonGrid.setRowCount(puzzle.getHeight() + 1);
        buildGrid();
    }

    public void buildGrid() {
        //alright, time to think up a new algo to figure this out.
        //so currently, the grid is puzzle height + 1 and puzzle width + 1
        //as we're still working left-right, top-down, we start with the column clues.
        //now, because the top-left square is blank, we'll add a blank at the beginning
        TextView blank = new TextView(this);
        blank.setText(" ");
        buttonGrid.addView(blank);
        for (int i = 0; i < puzzle.getWidth(); i++) {
            String columnBuilder = "";
            for (int j = 0; j < puzzle.getPuzzleCluesColumns().get(i).size(); j++) {
                columnBuilder += "\n" + puzzle.getPuzzleCluesColumns().get(i).get(j);
            }
            if (columnBuilder.isEmpty()) {
                columnBuilder = "0";
            }
            TextView columnClue = new TextView(this);
            columnClue.setText(columnBuilder);
            columnClue.setGravity(Gravity.BOTTOM);
            buttonGrid.addView(columnClue);
        }
        //lol columns done, and rows will be just as easy
        for (int i = 0; i < puzzle.getHeight(); i++) {
            String rowBuilder = "";
            for (int j = 0; j < puzzle.getPuzzleCluesRows().get(i).size(); j++) {
                rowBuilder += " " + puzzle.getPuzzleCluesRows().get(i).get(j) ;
            }
            TextView rowClue = new TextView(this);
            rowClue.setText(rowBuilder);
            buttonGrid.addView(rowClue);
            for (int j = 0; j < puzzle.getWidth(); j++) {
                PicrossSquare sq = new PicrossSquare(this, i, j);
                sq.setOnClickListener(this);
                sq.setBackgroundResource(R.drawable.unshaded);
                buttonGrid.addView(sq);
            }
        }
        for (int i = 0; i < puzzle.getPuzzleCluesRows().size(); i++) {
            for (int j = 0; j < puzzle.getPuzzleCluesRows().get(i).size(); j++) {
                System.out.print(puzzle.getPuzzleCluesRows().get(i).get(j) + ", ");
            }
            System.out.println();
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
        String puzzleData = "";
        for (int i = 0; i < puzzle.answerArray.length; i++) {
            for (int j = 0; j < puzzle.answerArray[i].length; j++) {
                if (puzzle.answerArray[i][j]) {
                    puzzleData += "1";
                }
                else {
                    puzzleData += "0";
                }
            }
            puzzleData += ";";
        }
        intent.putExtra("PUZZLE_DATA", puzzleData);
        startActivity(intent);
    }
}
