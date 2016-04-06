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
public class AddLoginJSON implements Runnable {
    private String username;
    private String password;
    private String firstName;
    private String lastname;
    private String email;
    private boolean result;
    private volatile JSONObject jsonFile;

    public AddLoginJSON (String user, String pass, String fName, String lName, String eAddress) {
        username = user;
        password = pass;
        firstName = fName;
        lastname = lName;
        email = eAddress;
    }

    @Override
    public void run() {
        addNewLogin();
    }


    public JSONObject getJSON() {
        return jsonFile;
    }


    public void addNewLogin() {
        try {
            URL url = new URL("https://webprojects.eecs.qmul.ac.uk/ma334/login/save.php?username="
                    + username + "&password=" + password + "&name=" + firstName + lastname
                    + "email=" + email);
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