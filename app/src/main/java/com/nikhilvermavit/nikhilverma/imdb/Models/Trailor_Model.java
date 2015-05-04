package com.nikhilvermavit.nikhilverma.imdb.Models;

/**
 * Created by Nikhil Verma on 05-01-2015.
 */
public class Trailor_Model {
    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    private String quality;
    private String videoURL;
}
