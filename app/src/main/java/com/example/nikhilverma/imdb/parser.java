package com.example.nikhilverma.imdb;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nikhil Verma on 17-12-2014.
 */
public class parser {


    public static Model parseFeed(String content) {

        try {

            JSONObject obj = new JSONObject(content);
            Model flower;
            if (obj.getString("Response") == "False") {
                return null;
            }

            flower = new Model(obj.getString("Title"), obj.getString("Year"), obj.getString("Released"), obj.getString("Runtime"), obj.getString("Genre"), obj.getString("Director")
                    , obj.getString("Writer"), obj.getString("Actors"), obj.getString("Plot"), obj.getString("Language"), obj.getString("Awards"), obj.getString("Poster"), obj.getString("imdbRating")
                    , obj.getString("imdbID"), obj.getString("imdbVotes"), obj.getString("Type"), obj.getString("Response"));

            final String EXTRA = "SX1920.jpg";
            String tem = obj.getString("Poster");
            String der = tem + EXTRA;
            Log.d("URL", der);
            return flower;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}




