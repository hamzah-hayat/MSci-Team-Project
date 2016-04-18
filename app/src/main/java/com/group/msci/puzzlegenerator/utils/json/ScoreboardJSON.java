package com.group.msci.puzzlegenerator.utils.json;

import android.util.Log;

import com.group.msci.puzzlegenerator.utils.UrlBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Magdi on 06/04/2016.
 */
public class ScoreboardJSON implements Runnable {

    private UrlBuilder builder;
    private volatile JSONObject jsonFile;

    public ScoreboardJSON (char type) {
        builder = new UrlBuilder(findScript(type));
        builder.setPrefix("score/");
    }

    public JSONObject getJSON() {
        return jsonFile;
    }

    public String findScript(char puzzleType) {
        if (puzzleType == 'd') {
            return "dotLoad.php";
        }
        else if (puzzleType == 'm') {
            return "mazeLoad.php";
        }
        else if (puzzleType == 'p') {
            return "picrossLoad.php";
        }
        else if (puzzleType == 'b'){
            return "ballLoad.php";
        }
        else {
            throw new IllegalArgumentException("Wrong maze argument");
        }
    }

    @Override
    public void run() {
        try {
            Log.i("Scoreboard", "Hellow");
            InputStream urlStream = builder.toURL().openConnection().getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();

            String input = bf.readLine();
            while (input != null) {
                responseBuilder.append(input);
                input = bf.readLine();
            }
            jsonFile = new JSONObject(responseBuilder.toString());
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }
    }
}