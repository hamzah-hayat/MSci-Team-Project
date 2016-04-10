package com.group.msci.puzzlegenerator.json;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Magdi on 06/04/2016.
 */
public class ScoreboardJSON implements Runnable {
    private String puzzleType;
    private volatile JSONObject jsonFile;

    public ScoreboardJSON (String type) {
        puzzleType = type;
    }

    @Override
    public void run() {
        buildJSON();
    }


    public JSONObject getJSON() {
        return jsonFile;
    }


    public void buildJSON() {
        try {
            URL url;
            if (puzzleType.equals("Picross")) {
                url = new URL("http://webprojects.eecs.qmul.ac.uk/ma334/score/picrossLoad.php");
            }
            else if (puzzleType.equals("Dot")) {
                url = new URL("http://webprojects.eecs.qmul.ac.uk/ma334/score/dotLoad.php");
            }
            else if (puzzleType.equals("Ball")) {
                url = new URL("http://webprojects.eecs.qmul.ac.uk/ma334/score/ballLoad.php");
            }
            else {
                url = new URL("http://webprojects.eecs.qmul.ac.uk/ma334/score/mazeLoad.php");
            }
            InputStream urlStream = url.openConnection().getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();

            String input = bf.readLine();
            while (input != null) {
                responseBuilder.append(input);
                input = bf.readLine();
            }
            jsonFile = new JSONObject(responseBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}