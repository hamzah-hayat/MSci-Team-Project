package com.group.msci.puzzlegenerator.dottodot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Mustafa on 07/02/2016.
 */
public class DotsView extends View {
    public static final float TOUCH_TOLERANCE = 4;
    protected Paint dotsPaint;
    protected Bitmap dotsBitmap;
    protected Canvas dotsCanvas;
    protected boolean isDrawing = false;
    protected float x, y;
    protected float startx, starty;
    protected ArrayList<Dot> dots = new ArrayList<>();
    int vWidth, vLength;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dotsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        dotsCanvas = new Canvas(dotsBitmap);
        for(int i = 0; i < dots.size(); i++) {
            Dot temp = dots.get(i);
            dotsCanvas.drawCircle(temp.getxPos(), temp.getyPos(), 25, dotsPaint);
        }
        vWidth = w;
        vLength = h;
    }


    public DotsView(Context context) {
        super(context);
        init();
    }

    public DotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        dotsPaint = new Paint();
        dotsPaint.setAntiAlias(true);
        dotsPaint.setDither(true);
        dotsPaint.setColor(Color.GREEN);
        //dotsPaint.setStyle(Paint.Style.STROKE);
        dotsPaint.setStrokeJoin(Paint.Join.ROUND);
        dotsPaint.setStrokeCap(Paint.Cap.ROUND);
        dotsPaint.setStrokeWidth(5);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                startx = x;
                starty = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                for(int j = 0; j < dots.size(); j++) {
                    Dot temp = dots.get(j);
                    if(temp.getxPos() >= startx - 20 && temp.getxPos() <= startx + 20)
                        if(temp.getyPos() >= starty - 20 && temp.getyPos() <= starty +  20) {
                            for(int i = 0; i < dots.size(); i++) {
                                Dot curr = dots.get(i);
                                if (curr.getxPos() >= x - 20 && curr.getxPos() <= x + 20) {
                                    if (curr.getyPos() >= y - 20 && curr.getyPos() <= y + 20) {
                                        dotsCanvas.drawLine(startx, starty, x, y, dotsPaint);
                                        invalidate();
                                    }
                                }
                            }

                        }
                }

                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(dotsBitmap, 0, 0, dotsPaint);

        float dx = Math.abs(x-startx);
        float dy = Math.abs(y-starty);
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(startx, starty, x, y, dotsPaint);
        }
    }

    public void setDots(ArrayList<Dot> a) {
        dots = a;
    }

    public int getvWidth() {
        return vWidth;
    }

    public int getvLength() {
        return vLength;
    }

    public void removeOverlappingDots() {
        //Check that one dot does not have the same x and y as another dot within the range of the radius (25)
        //If it does then does than remove the dot found to overlap from arraylist of dots
    }

    public void removeEdgeDots() {
        //Check each dot x and y and that with the radius added on it does not
        //go below 0 in x and y or if x is more 300 or if y is more than 300
    }
}
