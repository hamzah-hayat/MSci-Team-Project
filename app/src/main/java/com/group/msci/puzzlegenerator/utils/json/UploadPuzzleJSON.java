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
public class UploadPuzzleJSON implements Runnable { //DON'T KNOW HOW TO IMPLEMENT THIS
    private char puzzleType;
    private String puzzleData;
    private String puzzleText;
    private volatile JSONObject jsonFile;
    private String urlBase;

    public UploadPuzzleJSON(char puzzleCodeT, String data, String text) {
        puzzleType = puzzleCodeT;
        puzzleData = data;
        puzzleText = text;
    }

    @Override
    public void run() {
        buildJSON();
    }


    public JSONObject getJSON() {
        return jsonFile;
    }


    public void buildJSON() {
        if (puzzleType == 'd') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/dotSave.php?puzzleData=" + puzzleData + "&puzzleText=" + puzzleText;
        }
        else if (puzzleType == 'm') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/mazeSave.php?puzzleData=" + puzzleData + "&puzzleText=" + puzzleText;
        }
        else if (puzzleType == 'p') {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/picrossSave.php?puzzleData=" + puzzleData + "&puzzleText=" + puzzleText;
        }
        else {
            urlBase = "http://webprojects.eecs.qmul.ac.uk/ma334/puzzle/ballSave.php?puzzleData=" + puzzleData + "&puzzleText=" + puzzleText;
        }
        try {
            URL url = new URL(urlBase);
            InputStream urlStream = url.openConnection().getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();

            String input = bf.readLine();
            while (input != null) {
                responseBuilder.append(input);
                input = bf.readLine();
            }
            jsonFile = new JSONObject(responseBuilder.toString());
            System.out.println("Hello");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}