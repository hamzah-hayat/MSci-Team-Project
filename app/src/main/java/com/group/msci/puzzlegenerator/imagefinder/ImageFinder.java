package com.group.msci.puzzlegenerator.imagefinder;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by magdi on 21/01/2016.
 */
public class ImageFinder {
    public ImageFinder () {
        String key="AIzaSyCFRn1_MCj_MoAVUQfC78Wm9wQXRhC61Po";
        String noun = "";
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q=" + noun + "&alt=json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output;
        System.out.println("Output from Server .... \n");
        try {
            while ((output = br.readLine()) != null) {

                if(output.contains("\"link\": \"")){
                    String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                    System.out.println(link);       //Will print the google search links
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();
    }

    public Bitmap findImage (String noun) {
        return null; //finds image
    }
}
