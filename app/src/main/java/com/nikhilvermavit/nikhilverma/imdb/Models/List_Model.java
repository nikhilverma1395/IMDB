package com.nikhilvermavit.nikhilverma.imdb.Models;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class List_Model {
    private String Title;
    private String Rating;

    public List_Model() {
        Title = Rating = Year = URL = "";
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    private String Year;

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    private String URL;
}
