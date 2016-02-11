package com.group.msci.puzzlegenerator.imagefinder;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by magdi on 21/01/2016.
 */
public class ImageFinder {
    public ImageFinder () {

    }

    public Bitmap findImage (String noun) {
        try {
            URL bingConn = new URL("https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?Query=%27Datamarket%27&$top=10&$format=Json");
            HttpURLConnection conn = (HttpURLConnection) bingConn.openConnection();
            conn.setRequestMethod("GET");
            byte[] accountKeyEncoded = Base64.encode((":vaELPGayBneMeTSAw2SePb/7QlF6DoRLRaLYMX0/DZ4").getBytes(), Base64.DEFAULT);
            conn.setRequestProperty("Authorisation", "Basic " + accountKeyEncoded);
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)conn.getContent()));
            String line = "";
            String fullFile = "";
            while ((line = in.readLine()) != null) {
                fullFile += line + "\n";
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseInput (String fullFile) {
        String[] everyLine = fullFile.split("\n");
        ArrayList<String> links = new ArrayList<String>();
        for (int i = 0; i < everyLine.length; i++) {

        }
        return null;
    }

    private String chooseImageLink (String[] links) {
        return null;
    }
}
