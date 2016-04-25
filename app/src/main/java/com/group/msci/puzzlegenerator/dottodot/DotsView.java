package com.group.msci.puzzlegenerator.dottodot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mustafa on 07/02/2016.
 */
public class DotsView extends View {
    public static final float TOUCH_TOLERANCE = 4;
    protected Paint dotsPaint;
    protected Bitmap dotsBitmap;
    private Bitmap backgroundBitmap;
    protected Canvas dotsCanvas;
    protected boolean isDrawing = false;
    protected float x, y;
    protected float startx, starty;
    protected ArrayList<Dot> dots = new ArrayList<>();
    protected int vWidth, vLength;
    private int DOT_RADIUS = 30;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dotsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        dotsCanvas = new Canvas(dotsBitmap);
        for(int i = 0; i < dots.size(); i++) {
            Dot temp = dots.get(i);
            dotsCanvas.drawCircle(temp.getxPos(), temp.getyPos(), 30, dotsPaint);
        }
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
        dotsPaint.setColor(Color.GRAY);
        //dotsPaint.setStyle(Paint.Style.STROKE);
        dotsPaint.setStrokeJoin(Paint.Join.ROUND);
        dotsPaint.setStrokeCap(Paint.Cap.ROUND);
        dotsPaint.setStrokeWidth(15);

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
                    if(temp.getxPos() >= startx - 30 && temp.getxPos() <= startx + 30)
                        if(temp.getyPos() >= starty - 30 && temp.getyPos() <= starty +  30) {
                            for(int i = 0; i < dots.size(); i++) {
                                Dot curr = dots.get(i);
                                if (curr.getxPos() >= x - 30 && curr.getxPos() <= x + 30) {
                                    if (curr.getyPos() >= y - 30 && curr.getyPos() <= y + 30) {
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

    public void setBackgroundBitmap(Bitmap b) { backgroundBitmap = b; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap,0,0,dotsPaint);
        }
        canvas.drawBitmap(dotsBitmap, 0, 0, dotsPaint);

        for(int i = 0; i < dots.size(); i++) {
            Dot c = dots.get(i);
            canvas.drawCircle(c.getxPos(), c.getyPos(), 30, dotsPaint);
        }

        float dx = Math.abs(x-startx);
        float dy = Math.abs(y-starty);
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(startx, starty, x, y, dotsPaint);
        }
    }

    public void setDots(ArrayList<Dot> a) { dots = a; }

    public ArrayList<Dot> getDots() {
        return dots;
    }

    public int getvWidth() {
        return vWidth;
    }

    public int getvLength() {
        return vLength;
    }

    public void setvWidth(int w) {
        vWidth = w;
    }

    public void setvLength(int l) {
        vLength = l;
    }

    public void removeOverlappingDots() {
        boolean removed = true;
        int origLength = dots.size();

        while(removed) {
            for (int i = 1; i < dots.size(); i++) {
                Dot temp = dots.get(i);
                for (int j = 0; j < dots.size(); j++) {
                    Dot iDot = dots.get(j);
                    if (i == j) { }
                    else if (iDot.getxPos() < temp.getxPos() + 60 && iDot.getxPos() > temp.getxPos() - 60) {
                        if (iDot.getyPos() < temp.getyPos() + 60 && iDot.getyPos() > temp.getyPos() - 60) {
                            dots.remove(j);
                        }
                    }
                }
            }
            if (dots.size() < origLength) {
                origLength = dots.size();
            } else {
                removed = false;
            }
        }

    }

        //Check that one dot does not have the same x and y as another dot within the range of the radius (25)
        //If it does then does than remove the dot found to overlap from arraylist of dots


    public void removeEdgeDots() {
        for(int i = 0; i < dots.size(); i++) {
            Dot temp = dots.get(i);
            if((temp.getxPos() - DOT_RADIUS) < 0) {
                temp.setxPos(temp.getxPos() + Math.abs(temp.getxPos() - DOT_RADIUS));
                if((temp.getyPos() - DOT_RADIUS) < 0) {
                    temp.setyPos(temp.getyPos() + Math.abs(temp.getyPos() - DOT_RADIUS));
                }
            }
            else if((temp.getyPos() - DOT_RADIUS) < 0) {
                temp.setyPos(temp.getyPos() + Math.abs(temp.getyPos() - DOT_RADIUS));
                if((temp.getxPos() - DOT_RADIUS) < 0) {
                    temp.setxPos(temp.getxPos() + Math.abs(temp.getxPos() - DOT_RADIUS));
                }
            }
            else if((temp.getxPos() + DOT_RADIUS) > 900) {
                temp.setxPos(temp.getxPos() - ((temp.getxPos() + DOT_RADIUS) - temp.getxPos()));
                if((temp.getyPos() + DOT_RADIUS) > 900) {
                    temp.setyPos(temp.getyPos() - ((temp.getyPos() + DOT_RADIUS) - temp.getyPos()));
                }
            }
            else if((temp.getyPos() + DOT_RADIUS) > 900) {
                temp.setyPos(temp.getyPos() - ((temp.getyPos() + DOT_RADIUS) - temp.getyPos()));
                if((temp.getxPos() + DOT_RADIUS) > 900) {
                    temp.setxPos(temp.getxPos() - ((temp.getxPos() + DOT_RADIUS) - temp.getxPos()));
                }
            }
            else {}
        }

        //Check each dot x and y and that with the radius added on it does not
        //go below 0 in x and y or if x is more 300 or if y is more than 300
    }
}
