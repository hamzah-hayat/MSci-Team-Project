package com.group.msci.puzzlegenerator.dottodot;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class Score {
    private int totalScore;
    private int timetTaken;
    private int numberOfDots;
    private Seed seed;

    public Score(int ttlScore, int timeTake, int noDots, Seed s){
        totalScore = ttlScore;
        timetTaken = timeTake;
        numberOfDots = noDots;
        seed=s;
    }

    public int getNumberOfDots() {
        return numberOfDots;
    }

    public void setNumberOfDots(int numberOfDots) {
        this.numberOfDots = numberOfDots;
    }

    public int getTimetTaken() {
        return timetTaken;
    }

    public void setTimetTaken(int timetTaken) {
        this.timetTaken = timetTaken;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }
}
