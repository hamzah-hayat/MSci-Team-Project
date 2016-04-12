package com.group.msci.puzzlegenerator.maze.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filipt on 10/04/2016.
 */
public class Seed {

    /**
     * Stores the seed/s of the maze's randomizer.
     */
    private List<Long> seeds;
    private int pos;

    public Seed() {
        pos = 0;
        seeds = new ArrayList<>();
    }

    public Seed(long seed) {
        this();
        seeds.add(seed);
    }

    public Seed(List<Long> seeds) {
        this();
        this.seeds.addAll(seeds);
    }

    public long get() {
        return seeds.get(pos++);
    }

    public void resetPos() {
        pos = 0;
    }
}
