package com.group.msci.puzzlegenerator.utils.user;

import android.util.Log;

import com.group.msci.puzzlegenerator.utils.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by filipt on 15/04/2016.
 */
public class UserListRetriever implements Runnable {

    private UrlBuilder builder;
    private JSONArray userList;

    public UserListRetriever() {
        builder = new UrlBuilder("loginLoad.php");
        builder.setPrefix("login/");
    }

    public JSONArray getUserList() {

        if (userList == null)
            throw new IllegalStateException("User list has not yet been fetched from the URL");
        return userList;
    }

    @Override
    public void run() {
        try {
            URL url = builder.toURL();
            InputStream urlStream = url.openConnection().getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();
            String input = bf.readLine();

            while (input != null) {
                responseBuilder.append(input);
                input = bf.readLine();
            }
            userList = new JSONObject(responseBuilder.toString()).getJSONArray("usernames");
        } catch (IOException e) {

        } catch (JSONException e) {

        }
    }
}
