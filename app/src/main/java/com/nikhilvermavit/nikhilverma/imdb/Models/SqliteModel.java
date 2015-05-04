package com.nikhilvermavit.nikhilverma.imdb.Models;

/**
 * Created by Nikhil Verma on 02-01-2015.
 */
public class SqliteModel {
    private float ID;
    private String TITLE;
    private String IMAGE_URL;
    private String YEAR;

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public void setID(float ID) {
        this.ID = ID;
    }

    public SqliteModel() {
        ID = 0;
        TITLE = "";
        IMAGE_URL = "";
        RATING = 0.0F;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public float getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public float getRATING() {
        return RATING;
    }

    public void setRATING(float RATING) {
        this.RATING = RATING;
    }

    private float RATING;
}
