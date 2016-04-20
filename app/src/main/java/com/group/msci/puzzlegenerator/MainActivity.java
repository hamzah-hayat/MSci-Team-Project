package com.group.msci.puzzlegenerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.group.msci.puzzlegenerator.BallSwitch.BallSwitchPuzzleGame;
import com.group.msci.puzzlegenerator.dottodot.DotToDotMainScreen;
import com.group.msci.puzzlegenerator.dottodot.DotToDotView;
import com.group.msci.puzzlegenerator.utils.PuzzleCode;
import com.group.msci.puzzlegenerator.utils.json.DownloadPuzzleJSON;
import com.group.msci.puzzlegenerator.maze.controllers.MazeMainMenuController;
import com.group.msci.puzzlegenerator.maze.controllers.GameInstanceController;
import com.group.msci.puzzlegenerator.maze.utils.MazeParams;
import com.group.msci.puzzlegenerator.picross.PicrossMainMenu;
import com.group.msci.puzzlegenerator.picross.PicrossPuzzleGUI;
import com.group.msci.puzzlegenerator.utils.user.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File unameFile = new File(getApplicationContext().getFilesDir(), UserManager.STORAGE_FILE);

        if (!unameFile.exists()) {
            Intent intent = new Intent(this, UserManager.class);
            this.startActivity(intent);
        }

        //Initialize facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        PuzzleCode.init();

        setContentView(R.layout.puzzld_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ImageButton fab = (ImageButton) findViewById(R.id.codeButton);
        fab.setOnClickListener(this);

        ImageButton mazeBtn = (ImageButton) findViewById(R.id.maze);
        mazeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MazeMainMenuController.class);
                MainActivity.this.startActivity(intent);
            }
        });
        ImageButton picrossBtn = (ImageButton) findViewById(R.id.picross);
        picrossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PicrossMainMenu.class);
                startActivity(intent);
            }
        });

        ImageButton dotBtn = (ImageButton) findViewById(R.id.dots);
        dotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DotToDotMainScreen.class);
                MainActivity.this.startActivity(intent);
            }
        });

        //BallSwitch stuff here
        ImageButton ballSwitchMenuButton = (ImageButton) findViewById(R.id.ball);
        ballSwitchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BallSwitchPuzzleGame.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Code Here");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadPuzzleJSON downJson = new DownloadPuzzleJSON(input.getText().toString());
                Thread x = new Thread(downJson);
                x.start();
                try {
                    System.out.println("Waiting...");
                    x.join();
                } catch (InterruptedException ex) {
                    //do nothing
                }
                JSONObject jsonFile = downJson.getJSON();
                PuzzleCode pc = PuzzleCode.getInstance();
                pc.setCode(input.getText().toString());
                char puzzleType = pc.getTypeCode();
                if (puzzleType == 'p') {
                    try {
                        String puzzleData = jsonFile.getString("puzzleData");
                        if (puzzleData.equals("")) {
                            //problem
                        } else {
                            //no problem
                            Intent intent = new Intent(MainActivity.this, PicrossPuzzleGUI.class);
                            intent.putExtra("ANSWER_ARRAY", puzzleData);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (puzzleType == 'd') {
                    try {
                        String puzD = jsonFile.getString("puzzleData");
                        if (puzD.equals("")) {
                            //problem
                        } else {
                            //no problem
                            Intent intent = new Intent(MainActivity.this, DotToDotView.class);
                            intent.putExtra("ANSWER_ARRAY", puzD);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (puzzleType == 'm') {
                    try {
                        String encodedMaze = jsonFile.getString("puzzleData");

                        if (!encodedMaze.equals("")) {
                            Gson gson = new Gson();
                            MazeParams params = gson.fromJson(encodedMaze, MazeParams.class);
                            /**
                             * Start the particular maze immediately,
                             * without going through the creation menus
                             */
                            Intent intent = new Intent(MainActivity.this,
                                                       GameInstanceController.class);
                            intent.putExtra("maze_params", params);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
