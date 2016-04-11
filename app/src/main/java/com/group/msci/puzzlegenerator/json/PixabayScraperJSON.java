package com.group.msci.puzzlegenerator.json;

import android.app.Activity;

import com.group.msci.puzzlegenerator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

/**
 * Created by Magdi on 06/04/2016.
 */
public class PixabayScraperJSON implements Runnable {
    private volatile JSONObject jsonFile;
    private InputStream nounDataStream;
    private String urlBase;

    public PixabayScraperJSON (InputStream in) {
        nounDataStream = in;
    }

    @Override
    public void run() {
        buildJSON();
    }

    public JSONObject getJSON() {
        return jsonFile;
    }

    public void buildJSON() {
        urlBase = "https://pixabay.com/api/?key=2370232-bcc3504084d5e0efd1dd71d8a&q=";
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(nounDataStream));
            Random rand = new Random();
            int lineNumber = rand.nextInt(146603);
            line = "";
            for (int i = 0; i < lineNumber; i++) {
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("damn, wrong path -.-");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String urlStr = urlBase + line;
            URL urlTemp = new URL(urlStr);
            URI uri = new URI(urlTemp.getProtocol(), urlTemp.getUserInfo(), urlTemp.getHost(), urlTemp.getPort(), urlTemp.getPath(), urlTemp.getQuery(), urlTemp.getRef());
            URL url = uri.toURL();
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}