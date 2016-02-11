package com.group.msci.puzzlegenerator.imagefinder;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class NounGenerator {
    public NounGenerator () {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("res/raw/noundata.txt"));
            String countLine = reader.readLine();
            int wordCount = Integer.parseInt(countLine.substring(11));
            Random rand = new Random();
            int numChosen = rand.nextInt(wordCount);
            String wordChosen = "";
            for (int i = 0; i < numChosen; i++) {
                wordChosen = reader.readLine();
            }
            ImageFinder finder = new ImageFinder();
            Bitmap imageToUse = finder.findImage(wordChosen);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
