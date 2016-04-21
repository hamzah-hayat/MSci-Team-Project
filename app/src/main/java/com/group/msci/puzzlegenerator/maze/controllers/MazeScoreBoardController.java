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
import java.util.Collections;
import java.util.List;

/**
 * Created by filipt on 15/04/2016.
 */
public class MazeScoreBoardController extends Activity {

    private ListView listView;
    private ArrayList<String> usernameScores;

    private static final String LIST_RECORD_FMT = "Rank: %d\n%s";
    private static final String USER_SCORE_FMT = "Username: %s\nScore: %d\nPuzzle ID: %d";

    private static class Score implements Comparable<Score> {
        public String username;
        public Integer score;
        public Integer puzzleID;

        public Score(String username, int score, int puzzleID) {
            this.username = username;
            this.score = score;
            this.puzzleID = puzzleID;
        }

        @Override
        public int compareTo(Score other) {
            return score.compareTo(other.score);
        }

        @Override
        public String toString() {
           return String.format(USER_SCORE_FMT, username, score, puzzleID);
        }

    }

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
            List<Score> scores = new ArrayList<Score>();

            for (int i = 0; i < ranks.length(); ++i) {
                JSONObject record = ranks.getJSONObject(i);
                String name = record.getString("player");
                int score = record.getInt("score");
                int id = record.getInt("shareCode");
                scores.add(new Score(name, score, id));
            }
            Collections.sort(scores, Collections.<Score>reverseOrder());

            for (int i = 0; i < scores.size(); ++i) {
                usernameScores.add(String.format(LIST_RECORD_FMT, i + 1, scores.get(i).toString()));

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
