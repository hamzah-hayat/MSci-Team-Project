package com.group.msci.puzzlegenerator.picross;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.group.msci.puzzlegenerator.R;
import com.group.msci.puzzlegenerator.utils.json.ScoreboardJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class PicrossScoreboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picross_scoreboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ScoreboardJSON jsonGetter = new ScoreboardJSON("Picross");
        JSONObject jsonFile = jsonGetter.getJSON();
        //score format in json will be an entry called "scores"
            //they SHOULD be parallel, so it SHOULD work if I have an array of scores and an array of names
        //wait, but I never considered this: multiple results. How do I do a json_encode on each result
                    //what would it even look like...?
        //guess it's time to do some PHP testing on bloody konqueror...
            //so what I need to do is run json_encode on each row
        try {
            jsonFile.getJSONArray("Score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
