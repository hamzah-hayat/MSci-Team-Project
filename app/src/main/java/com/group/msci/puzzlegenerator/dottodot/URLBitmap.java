package com.group.msci.puzzlegenerator.dottodot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mustafa on 06/04/2016.
 */
class URLBitmap implements Runnable {
    private String urlLink;
    private volatile Bitmap rImg;

    public URLBitmap(String l) {
        urlLink = l;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            rImg = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Bitmap getrImg() {
        return rImg;
    }

}