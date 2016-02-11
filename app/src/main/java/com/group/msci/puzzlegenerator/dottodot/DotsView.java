package com.group.msci.puzzlegenerator.dottodot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Mustafa on 07/02/2016.
 */
public class DotsView extends View {
    private Paint paint;
    private ArrayList<Integer> x;
    private ArrayList<Integer> y;


    public DotsView(Context context, ArrayList<Integer> x, ArrayList<Integer> y) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        this.x = x;
        this.y = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        if(x.size() == y.size()) {
            for(int i = 0; i < x.size(); i = i + 10) {
                canvas.drawCircle(x.get(i), y.get(i), 5, paint);
            }
        }
    }
}
