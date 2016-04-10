package com.group.msci.puzzlegenerator.maze.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by filipt on 10/04/2016.
 */
public class Seed {

    private ArrayList<Integer> seeds;
    private int pos;
    private boolean useDefault;

    public Seed(boolean useDefault) {
        pos = 0;
        this.useDefault = useDefault;
        seeds = new ArrayList<>();
    }

    public Seed(String line) {
        useDefault = Boolean.parseBoolean(line.split(":")[0]);
        Log.i("Seed", line);
        line = line.split(":")[1];
        if ((line.length() > 0) && (!line.equals("-"))) {
            for (String num : line.split("-")) {
                seeds.add(Integer.parseInt(num));
            }
        }
    }

    public Seed(int seed) {
        this(false);
        seeds.add(seed);
    }

    public Seed(ArrayList<Integer> seeds) {
        this(false);
        this.seeds = seeds;
    }

    public int get() {
        return seeds.get(pos++);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Boolean.toString(useDefault));
        sb.append(":");
        for (Integer seed : seeds) {
            sb.append(seed);
            sb.append("-");
        }

        if (seeds.size() == 0) {
            sb.append("-");
        }

        return sb.toString();
    }

    public boolean getUseDefault() {return useDefault;}


}
