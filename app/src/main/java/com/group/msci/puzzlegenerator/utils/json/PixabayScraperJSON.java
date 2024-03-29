package com.group.msci.puzzlegenerator.utils.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
    private int lines = 146603;

    public void setLines(int l) {
        lines = l;
    }

    public String getNoun() {
        return noun;
    }

    private String noun;

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
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(nounDataStream));
            Random rand = new Random();
            int lineNumber = rand.nextInt(lines);
            noun = "";
            for (int i = 0; i < lineNumber; i++) {
                noun = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("damn, wrong path -.-");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String urlStr = urlBase + noun;
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