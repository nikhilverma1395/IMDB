package com.nikhilvermavit.nikhilverma.imdb.Models;

import android.graphics.Bitmap;

/**
 * Created by Nikhil Verma on 17-12-2014.
 */
public class Model {
    private String Title;
    private String Year;
    private String[] filmingLoc;
    private String Released;

    public String[] getFilmingLoc() {
        return filmingLoc;
    }

    public Model() {
        Title = "";
        Year = "";
        Released = "";
        Runtime = "";
        Genre = "";
        Director = "";
        Writer = "";
        Actors = "";
        Language = "";
        Plot = "";
        Awards = "";
        Poster = "";
        imdbRating = "";
        imdbID = "";
        imdbVotes = "";
        Type = "";
        Response = "";
        image=null;
        Type="";
    }

    public void setFilmingLoc(String[] filmingLoc) {
        this.filmingLoc = filmingLoc;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Awards;
    private String Poster;
    private String imdbRating;
    private String imdbID;
    private String imdbVotes;
    private String Type;
    private String Response;

    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }



    public Model(String title, String year, String released, String runtime,
                 String genre, String director, String writer, String actors, String plot,
                 String language, String awards, String poster, String imdbRating, String imdbID,
                 String imdbVotes, String type, String response) {
        Title = title;
        Year = year;
        Released = released;
        Runtime = runtime;
        Genre = genre;
        Director = director;
        Writer = writer;
        Actors = actors;
        Language = language;
        Plot = plot;
        Awards = awards;
        Poster = poster;
        this.imdbRating = imdbRating;
        this.imdbID = imdbID;
        this.imdbVotes = imdbVotes;
        Type = type;
        Response = response;
    }
}
