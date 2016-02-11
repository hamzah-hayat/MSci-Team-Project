package com.group.msci.puzzlegenerator.dottodot;

import java.util.ArrayList;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotMapFactory {
    private int precision;
    private ArrayList<Foreground> layers;

    public DotMapFactory(int p, ArrayList<Foreground> lyrs){
        precision=p;
        layers=lyrs;

    }

    public DotMapFactory(){}

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public ArrayList<Foreground> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<Foreground> layers) {
        this.layers = layers;
    }

    public void addLayer(Foreground f) {
        layers.add(f);
    }

    public DotToDotPuzzle getDotToDotPuzzle() {
        return new DotToDotPuzzle();
    }
}
