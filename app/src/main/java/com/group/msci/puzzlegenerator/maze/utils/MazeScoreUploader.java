package com.group.msci.puzzlegenerator.maze.utils;

import android.content.Context;

import com.group.msci.puzzlegenerator.utils.UrlBuilder;
import com.group.msci.puzzlegenerator.utils.user.UserManager;

import java.io.IOException;
import java.net.URL;

/**
 * Created by filipt on 15/04/2016.
 */
public class MazeScoreUploader implements Runnable{

    private UrlBuilder builder;

    public MazeScoreUploader(int score, Context ctx) {
        String username = UserManager.getCurrentUsername(ctx);
        builder = new UrlBuilder("mazeSave.php");
        builder.setPrefix("score/");
        builder.addParam("username", username);
        builder.addParam("score", Integer.toString(score));
    }

    @Override
    public void run() {
        try {
            URL url = builder.toURL();
            url.openConnection().getInputStream();
        } catch (IOException e) {

        }
    }
}
