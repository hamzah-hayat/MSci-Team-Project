package com.group.msci.puzzlegenerator.utils.json;

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
    private String urlBase;

    public DownloadPuzzleJSON (String puzzleCodeT) {
        puzzleCode = puzzleCodeT;
    }

    @Override
    public void run() {
        buildJSON();
    }


    public JSONObject getJSON() {
        return jsonFile;
    }


    public void buildJSON() {
        if (puzzleCode.charAt(0) == 'd') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/dotLoad.php?shareCode=";
        }
        else if (puzzleCode.charAt(0) == 'm') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/mazeLoad.php?shareCode=";
        }
        else if (puzzleCode.charAt(0) == 'p') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/picrossLoad.php?shareCode=";
        }
        else {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/ballLoad.php?shareCode=";
        }
        try {
            System.out.println(urlBase + Integer.parseInt(puzzleCode.substring(1)));
            URL url = new URL(urlBase + Integer.parseInt(puzzleCode.substring(1)));
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