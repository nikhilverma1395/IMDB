package com.example.nikhilverma.imdb;

import android.graphics.Bitmap;

/**
 * Created by Nikhil Verma on 04-01-2015.
 */
public class newModel {
   public  newModel()
   {

   }
    private String actorId;
    private String actorName;

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlCharacter() {
        return urlCharacter;
    }

    public void setUrlCharacter(String urlCharacter) {
        this.urlCharacter = urlCharacter;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public newModel(String actorId, Bitmap bitmap, String urlProfile, String urlPhoto, String urlCharacter, String character, String actorName) {
        this.actorId = actorId;
        this.bitmap = bitmap;
        this.urlProfile = urlProfile;
        this.urlPhoto = urlPhoto;

        this.urlCharacter = urlCharacter;
        this.character = character;
        this.actorName = actorName;
    }

    private String character;
    private String urlCharacter;
    private String urlPhoto;
    private String urlProfile;
    private Bitmap bitmap;


}
