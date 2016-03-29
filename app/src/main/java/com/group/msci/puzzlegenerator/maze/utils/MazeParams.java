package com.group.msci.puzzlegenerator.maze.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filipt on 11/03/2016.
 */
public class MazeParams implements Parcelable {

    private int width;
    private int height;
    private String type;
    private int time;
    private int nplanes;

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(width);
        out.writeInt(height);
        out.writeString(type);
        out.writeInt(time);
        out.writeInt(nplanes);
    }

    public MazeParams(int width, int height, int time, int nplanes, String type) {
        this.width = width;
        this.height = height;
        this.time = time;
        this.nplanes = nplanes;
        this.type = type;
    }

    public MazeParams(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.type = in.readString();
        this.time = in.readInt();
        this.nplanes = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MazeParams> CREATOR
            = new Parcelable.Creator<MazeParams>() {

        @Override
        public MazeParams createFromParcel(Parcel in) {
            return new MazeParams(in);
        }

        @Override
        public MazeParams[] newArray(int size) {
            return new MazeParams[size];
        }
    };

    public int getWidth() {
        return width;
    }

    public int getTime() {
        return time;
    }

    public int getNplanes() {
        return nplanes;
    }

    public String getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }
}