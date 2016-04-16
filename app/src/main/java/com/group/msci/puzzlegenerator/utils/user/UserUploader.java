package com.group.msci.puzzlegenerator.utils.user;

import android.util.Log;

import com.group.msci.puzzlegenerator.utils.UrlBuilder;

import java.io.IOException;
import java.net.URL;

/**
 * Created by filipt on 15/04/2016.
 */
public class UserUploader implements Runnable {

    private UrlBuilder builder;

    public UserUploader(String username) {
        builder = new UrlBuilder("loginSave.php");
        builder.setPrefix("login/");
        Log.i("User Uploader username", username
        );
        builder.addParam("username", username);
    }

    @Override
    public void run() {
        try {
            Log.i("User Uploader", "uploading");
            URL url = builder.toURL();
            url.openConnection().getInputStream();
        } catch (IOException e) {

        }
    }
}
