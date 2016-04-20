package com.group.msci.puzzlegenerator.picross;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.ScoreboardJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by filipt on 15/04/2016.
 */
public class PicrossScoreboard extends Activity {

    private ListView listView;
    private ArrayList<String> usernameScores;
    private static final String USER_SCORE_FMT = "Username: %s\nScore: %d\nPuzzle ID: %d";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        ScoreboardJSON scoreGetter = new ScoreboardJSON('p');
        Thread uploadThread = new Thread(scoreGetter);
        uploadThread.start();

        usernameScores = new ArrayList<>();
        setContentView(R.layout.content_picross_scoreboard);
        listView = (ListView) findViewById(R.id.picrossListView);

        try {
            //Show a loading wheel in the activity
            uploadThread.join();
        } catch (InterruptedException e) {
            Log.i("MazeScoreBoard", "Score download thread failed to finish");
        }

        try {
            JSONArray ranks = scoreGetter.getJSON().getJSONArray("ranks");
            for (int i = 0; i < ranks.length(); ++i) {
                JSONObject record = ranks.getJSONObject(i);
                String name = record.getString("player");
                int score = record.getInt("score");
                int id = record.getInt("shareCode");
                usernameScores.add(String.format(Locale.US, USER_SCORE_FMT, name, score, id));
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
