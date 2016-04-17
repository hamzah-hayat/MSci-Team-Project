package com.group.msci.puzzlegenerator.maze;

import android.app.Activity;
import android.os.Bundle;
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
    private static final String USER_SCORE_FMT = "%s:\t%s";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        usernameScores = new ArrayList<>();
        setContentView(R.layout.activity_maze_scoreboard);

        listView = (ListView) findViewById(R.id.leaderboard_list_view);
        JSONObject userScores = (new ScoreboardJSON("Maze")).getJSON();
        try {
            JSONArray usernames = userScores.getJSONArray("users");
            JSONArray scores = userScores.getJSONArray("scores");
            for (int i = 0; i < usernames.length(); ++i) {
                usernameScores.add(String.format(USER_SCORE_FMT, usernames.get(i), scores.get(i)));
            }
        } catch (JSONException e) {

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                usernameScores);
        listView.setAdapter(adapter);

    }
}
