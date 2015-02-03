package com.example.nikhilverma.imdb.parser;

import android.util.Log;

import com.example.nikhilverma.imdb.Models.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nikhil Verma on 17-12-2014.
 */
public class main_parser {


    public static Model parseFeed(String content) {

        try {

            JSONObject obj = new JSONObject(content);
            Model flower;
            if (obj.getString("Response") == "False") {
                return null;
            }
            final String EXTRA = "LX1920.jpg";
            String der = "";
            try {
                String tem = obj.getString("Poster");
                String d = tem.substring(0, tem.length() - 9);
                der = d + EXTRA;
                Log.d("URL", der);
            } catch (Exception e) {
                der = "N/A";
            }
            flower = new Model(obj.getString("Title"), obj.getString("Year"), obj.getString("Released"), obj.getString("Runtime"), obj.getString("Genre"), obj.getString("Director")
                    , obj.getString("Writer"), obj.getString("Actors"), obj.getString("Plot"), obj.getString("Language"), obj.getString("Awards"), der, obj.getString("imdbRating")
                    , obj.getString("imdbID"), obj.getString("imdbVotes"), obj.getString("Type"), obj.getString("Response"));
            return flower;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}




