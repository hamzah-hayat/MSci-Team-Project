package com.group.msci.puzzlegenerator.utils.json;

import android.content.Context;
import android.util.Log;

import com.group.msci.puzzlegenerator.utils.UrlBuilder;
import com.group.msci.puzzlegenerator.utils.user.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by filipt on 17/04/2016.
 */
public class UploadScoreJSON implements Runnable{

        private UrlBuilder builder;
        private volatile JSONObject jsonFile;

        public UploadScoreJSON(char puzzleType, int puzzleID, int score, Context ctx) {
            String username = UserManager.getCurrentUsername(ctx);
            builder = new UrlBuilder(findScript(puzzleType));
            builder.setPrefix("score/");
            builder.addParam("username", username);
            builder.addParam("score", Integer.toString(score));
            builder.addParam("shareCode ", Integer.toString(puzzleID));
        }

        public String findScript(char puzzleType) {
            if (puzzleType == 'd') {
                return "dotSave.php";
            }
            else if (puzzleType == 'm') {
                return "mazeSave.php";
            }
            else if (puzzleType == 'p') {
                return "picrossSave.php";
            }
            else if (puzzleType == 'b'){
                return "ballSave.php";
            }
            else {
                throw new IllegalArgumentException("Wrong maze argument");
            }

        }

        public JSONObject getJSON() {
            return jsonFile;
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
                Log.i("Upload Score JSON", "Upload response: " + responseBuilder.toString());
                Log.i("UploadScoreJSON", "url: " + url.toString());
                jsonFile = new JSONObject(responseBuilder.toString());

            } catch (IOException|JSONException e) {
                e.printStackTrace();
            }
        }

}
