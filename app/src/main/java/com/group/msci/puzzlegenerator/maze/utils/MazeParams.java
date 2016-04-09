package com.group.msci.puzzlegenerator.maze.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;
import java.net.Inet4Address;

/**
 * Created by filipt on 11/03/2016.
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
        out.writeInt(width);
        out.writeInt(height);
        out.writeString(type);
        out.writeInt(time);
        out.writeInt(nplanes);
        out.writeString(seed.toString());

    }

    public MazeParams(int width, int height, int time, int nplanes, String type, Seed seed) {
        this.width = width;
        this.height = height;
        this.time = time;
        this.nplanes = nplanes;
        this.type = type;
        this.seed = seed;
    }

    public MazeParams(String mpline) {
        String[] components = mpline.split(" ");
        this.width = Integer.parseInt(components[0]);
        this.height = Integer.parseInt(components[1]);
        this.time = Integer.parseInt(components[2]);
        this.nplanes = Integer.parseInt(components[3]);
        this.type = components[4];
        this.seed = new Seed(components[5]);
    }

    public MazeParams(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.type = in.readString();
        this.time = in.readInt();
        this.nplanes = in.readInt();
        this.seed = new Seed(in.readString());

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


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field field : getClass().getDeclaredFields()) {
            try {
                sb.append(field.get(this).toString());
                sb.append(" ");
            } catch (IllegalAccessException e) {
                Log.i("MazeParams", "Access error");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
        }

        return sb.toString();
    }
}
