package com.group.msci.puzzlegenerator.dottodot;

import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotMap {
    private ArrayList<Dot> dotList;
    private int width;
    private int length;

    public DotMap(ArrayList<Dot> dList, int w, int l) {
        dotList = dList;
        width = w;
        length = l;
    }

    public ArrayList<Dot> getDotList() {
        return dotList;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public void setDotList(ArrayList<Dot> dotList) {
        this.dotList = dotList;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Dot getDotAt(int i) {
        return dotList.get(i);
    }
}
