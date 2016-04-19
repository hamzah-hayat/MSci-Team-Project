package com.group.msci.puzzlegenerator.maze.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by filipt on 11/03/2016.
 * Represents the parameters needed to fully describe a maze. Used to send
 * mazes between activities and get shared mazes from the server.
 */
public class MazeParams implements Parcelable {

    private Integer width;
    private Integer height;
    private String type;
    private Integer time;
    private Integer nplanes;
    private Seed seed;

    @Override
    public void writeToParcel(Parcel out, int flags) {
        Gson gson = new Gson();
        out.writeInt(width);
        out.writeInt(height);
        out.writeString(type);
        out.writeInt(time);
        out.writeInt(nplanes);
        out.writeString(gson.toJson(seed));
    }

    public MazeParams(int width, int height, int time, int nplanes, String type, Seed seed) {
        this.width = width;
        this.height = height;
        this.time = time;
        this.nplanes = nplanes;
        this.type = type;
        this.seed = seed;
    }

    public MazeParams(Parcel in) {
        Gson gson = new Gson();
        this.width = in.readInt();
        this.height = in.readInt();
        this.type = in.readString();
        this.time = in.readInt();
        this.nplanes = in.readInt();
        this.seed = gson.fromJson(in.readString(), Seed.class);
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

    public Seed getSeed() {
        return seed;
    }
}
