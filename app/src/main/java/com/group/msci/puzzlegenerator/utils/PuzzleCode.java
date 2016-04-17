package com.group.msci.puzzlegenerator.utils;

/**
 * Created by filipt on 17/04/2016.
 */
public class PuzzleCode {

    private static PuzzleCode instance;

    private boolean isSet;
    private char puzzleTypeCode;
    private String code;
    private int numericCode;

    private PuzzleCode() {
        isSet = false;
    }

    public boolean isSet() {
        return isSet;
    }

    public String getCode() {
       return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.numericCode = Integer.parseInt(code.substring(1, code.length()));
        this.puzzleTypeCode = code.charAt(0);
        this.isSet = true;
    }

    public int numericCode() {
        return numericCode;
    }

    public char getTypeCode() {
        return puzzleTypeCode;
    }

    public static void init() {
       instance = new PuzzleCode();
    }

    public static PuzzleCode getInstance() {
        return instance;
    }


}
