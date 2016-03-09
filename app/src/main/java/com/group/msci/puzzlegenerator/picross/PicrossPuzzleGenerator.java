package com.group.msci.puzzlegenerator.picross;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by magdi on 29/11/2015.
 */
public class PicrossPuzzleGenerator {
    protected Bitmap foregroundImage;
    protected int puzzleWidth;
    protected int puzzleHeight;
    protected Bitmap originalImage;
    private volatile boolean[][] shadedSquares;

    public PicrossPuzzleGenerator (Bitmap imageT, int across, int down) {
        foregroundImage = imageT;
        puzzleWidth = across;
        puzzleHeight = down;
    }

    public void pixelateImage() {
        int oldWidth = foregroundImage.getWidth();
        int oldHeight = foregroundImage.getHeight();
        float scaleWidth = ((float) puzzleWidth) / oldWidth;
        float scaleHeight = ((float) puzzleHeight) / oldHeight;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(foregroundImage, 0, 0, oldWidth, oldHeight, matrix, false);
        foregroundImage.recycle();
        foregroundImage = resizedBitmap;
        //info found at http://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android/10703256#10703256
    }

    public Bitmap binariseImage() {
        int width, height;
        height = foregroundImage.getHeight();
        width = foregroundImage.getWidth();
        Bitmap bmpGreyscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGreyscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(foregroundImage, 0, 0, paint);
        int threshold;
        height = bmpGreyscale.getHeight();
        width = bmpGreyscale.getWidth();
        threshold = 127;
        Bitmap bmpBinary = Bitmap.createBitmap(bmpGreyscale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // get one pixel color
                int pixel = bmpGreyscale.getPixel(x, y);
                int red = Color.red(pixel);

                //get binary value
                if(red < threshold){
                    bmpBinary.setPixel(x, y, 0x00000000);
                } else{
                    bmpBinary.setPixel(x, y, 0xFFFFFFFF);
                }

            }
        }
        return bmpBinary;
        //info found at http://stackoverflow.com/questions/20299264/android-convert-grayscale-to-binary-image
    }

    public boolean[][] findShadedSquares(Bitmap binaryImage) {
        boolean[][] result = new boolean[puzzleHeight][puzzleWidth];
        for (int x = 0; x < binaryImage.getWidth(); x++) {
            for (int y = 0; y < binaryImage.getHeight(); y++) {
                if (binaryImage.getPixel(x, y) == 0x00000000) {
                    result[x][y] = true;
                } else {
                    result[x][y] = false;
                }
            }
        }
        return result;
    }

    public PicrossPuzzle createPuzzle() {
        CreatePuzzle puzThread = new CreatePuzzle(this);
        Thread x = new Thread(puzThread);
        x.start();
        try {
            x.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return puzThread.getPuzzle();
    }

    public Bitmap getForegroundImage () {
        return foregroundImage;
    }
}

class CreatePuzzle implements Runnable {
    private volatile PicrossPuzzle puzzle;
    private volatile boolean[][] shadedSquares;
    private PicrossPuzzleGenerator gen;

    public CreatePuzzle (PicrossPuzzleGenerator gen1) {
        gen = gen1;
    }

    @Override
    public void run() {
        gen.pixelateImage();
        shadedSquares = gen.findShadedSquares(gen.binariseImage());
        puzzle = new PicrossPuzzle(shadedSquares, gen.getForegroundImage());
    }

    public PicrossPuzzle getPuzzle () {
        return puzzle;
    }
}
