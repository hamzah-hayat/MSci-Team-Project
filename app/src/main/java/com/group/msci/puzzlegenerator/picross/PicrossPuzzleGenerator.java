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
        foregroundImage = Bitmap.createScaledBitmap(foregroundImage, puzzleWidth, puzzleHeight, true);
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
        threshold = 150;
        Bitmap bmpBinary = Bitmap.createBitmap(bmpGreyscale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // get one pixel color
                int pixel = bmpGreyscale.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                //get binary value
                boolean marked = false;
                if (red < threshold && !marked) {
                    bmpBinary.setPixel(x, y, 0xFF000000);
                    marked = true;
                }
                else {
                    bmpBinary.setPixel(x, y, 0xFFFFFFFF);
                }
                if (!marked) {
                    if (green < threshold) {
                        bmpBinary.setPixel(x, y, 0xFF000000);
                        marked = true;
                    }
                    else {
                        bmpBinary.setPixel(x, y, 0xFFFFFFFF);
                    }
                }
                if (!marked) {
                    if (blue < threshold) {
                        bmpBinary.setPixel(x, y, 0xFF000000);
                        marked = true;
                    }
                    else {
                        bmpBinary.setPixel(x, y, 0xFFFFFFFF);
                    }
                }
            }
        }
        return bmpBinary;
        //info found at http://stackoverflow.com/questions/20299264/android-convert-grayscale-to-binary-image
    }

    public boolean[][] findShadedSquares(Bitmap binaryImage) {
        boolean[][] result = new boolean[puzzleHeight][puzzleWidth];
        for (int x = 0; x < binaryImage.getHeight(); x++) {
            for (int y = 0; y < binaryImage.getWidth(); y++) {
                if (binaryImage.getPixel(x, y) == Color.BLACK) {
                    result[x][y] = true;
                    System.out.println("Puzzle Cell Shaded! Pixel Value: " + binaryImage.getPixel(x, y));
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
