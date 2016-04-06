package com.group.msci.puzzlegenerator.json;

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
public class DownloadPuzzleJSON implements Runnable {
    private String puzzleCode;
    private volatile JSONObject jsonFile;

    public DownloadPuzzleJSON (String puzzleCodeT) {
        puzzleCode = puzzleCodeT;
    }

    @Override
    public void run() {
        addNewLogin();
    }


    public JSONObject getJSON() {
        return jsonFile;
    }


    public void addNewLogin() {
        if (puzzleCode.charAt(0) == 'd') {

        }
        else if (puzzleCode.charAt(0) == 'm') {

        }
        else if (puzzleCode.charAt(0) == 'p') {

        }
        else if (puzzleCode.charAt(0) == 'b') {

        }
        try {
            URL url = new URL("https://webprojects.eecs.qmul.ac.uk/ma334/puzzle/picrossLoad.php?puzzlecode=" + puzzleCode.substring(1));
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

    public boolean registerSuccessful() {
        try {
            return jsonFile.getBoolean("DetailsSaved");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}