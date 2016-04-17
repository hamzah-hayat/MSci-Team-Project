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
public class CheckLoginJSON implements Runnable {
    private String username;
    private String password;
    private boolean result;
    private volatile JSONObject jsonFile;

    public CheckLoginJSON (String user, String pass) {
        username = user;
        password = pass;
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
            URL url = new URL("https://webprojects.eecs.qmul.ac.uk/ma334/login/load.php?username=" + username + "&password=" + password);
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