package com.group.msci.puzzlegenerator.dottodot;

import android.media.Image;

import java.util.Date;

/**
 * Created by Mustafa on 12/01/2016.
 */
public class DotToDotPuzzle {
    private int ID;
    private int creator;
    private Date dateCreated;
    private Image image;
    private DotMap dotMap;

    public DotToDotPuzzle(int id, int creat, Date dc, Image img, DotMap dMap) {
        ID = id;
        creator = creat;
        dateCreated = dc;
        image = img;
        dotMap = dMap;
    }

    public DotToDotPuzzle() {}

    public int getID() {
        return ID;
    }

    public int getCreator() {
        return creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Image getImage() {
        return image;
    }

    public DotMap getDotMap() {
        return dotMap;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setDotMap(DotMap dotMap) {
        this.dotMap = dotMap;
    }
}
