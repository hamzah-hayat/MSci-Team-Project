package com.group.msci.puzzlegenerator.foreground;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.group.msci.puzzlegenerator.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class TestForeground extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_foreground);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ImageView iv = (ImageView)findViewById(R.id.imageView3);

        InputStream in = getResources().openRawResource(R.raw.cow);

        Bitmap c = BitmapFactory.decodeStream(in);
        Bitmap b = c.copy(Bitmap.Config.ARGB_8888, true);

        in = getResources().openRawResource(R.raw.network);

        AssetManager am = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("network.eg");
        } catch (IOException e) {
            e.printStackTrace();
        }



        ForegroundDetection fd = new ForegroundDetection(in);
        fd.setBackground(Color.BLACK);
        fd.setOutline(true);
        try {
            b = fd.getForeground(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

       // b = ImageProcessing.blur(b);
        iv.setImageBitmap(b);//set bitmap here
    }
    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("network.eg");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void writeBytesToFile(InputStream is, File file) throws IOException{

        byte[] buffer = new byte[is.available()];
        is.read(buffer);


        OutputStream outStream = new FileOutputStream(file);
        outStream.write(buffer);
    }

}
