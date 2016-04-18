package com.group.msci.puzzlegenerator.maze.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.ScoreboardJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by filipt on 15/04/2016.
 */
public class MazeScoreBoardController extends Activity {

    private ListView listView;
    private ArrayList<String> usernameScores;
    private static final String USER_SCORE_FMT = "%s:\t%d";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        ScoreboardJSON scoreGetter = new ScoreboardJSON('m');
        Thread uploadThread = new Thread(scoreGetter);
        uploadThread.start();

        usernameScores = new ArrayList<>();
        setContentView(R.layout.maze_scoreboard);
        listView = (ListView) findViewById(R.id.leaderboard_list_view);

        try {
            //Show a loading wheel in the activity
            uploadThread.join();
        } catch (InterruptedException e) {
            Log.i("MazeScoreBoard", "Score download thread failed to finish");
        }

        try {
            JSONArray ranks = scoreGetter.getJSON().getJSONArray("ranks");
            for (int i = 0; i < ranks.length(); ++i) {
                String name = ranks.getJSONObject(i).getString("player");
                int score = ranks.getJSONObject(i).getInt("score");
                usernameScores.add(String.format(USER_SCORE_FMT, name, score));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                usernameScores);
        listView.setAdapter(adapter);

    }
}