package com.nikhilvermavit.nikhilverma.imdb.parser;

import android.util.Log;

import com.nikhilvermavit.nikhilverma.imdb.Activites.MyTask;
import com.nikhilvermavit.nikhilverma.imdb.Models.ActorDetailModel;
import com.nikhilvermavit.nikhilverma.imdb.Models.Trailor_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 04-01-2015.
 */
public class Actor_Detail_PARSER {
    public static String title = "",
            videoURL = "";
    public static List<Trailor_Model> list_te = new ArrayList<Trailor_Model>();
    static String[] filming;

    public static List<ActorDetailModel> parseFeednew(String content) {
        final String image_extra = "_LX32_CR13,0,0,0_AL_.jpg";
        List<ActorDetailModel> list = new ArrayList<ActorDetailModel>();
        List<Trailor_Model> list_t = new ArrayList<Trailor_Model>();
        // list = null;
        //list_t = null;
        if (content.length() < 50)
            return null;
        try {
            JSONArray main = new JSONArray(content);
            JSONObject maino = main.getJSONObject(0);
            JSONArray second = maino.getJSONArray("actors");
            for (int i = 0; i < second.length(); i++) {
                JSONObject oo = second.getJSONObject(i);
                ActorDetailModel a = new ActorDetailModel();
                if (oo.getString("actorId").length() != 0)
                    a.setActorId(oo.getString("actorId"));
                else a.setActorId("\t\t N/A \t");
                if (oo.getString("actorName").length() != 0)
                    a.setActorName(oo.getString("actorName"));
                else a.setActorName("\t\t N/A \t");
                if (oo.getString("character").length() != 0)
                    a.setCharacter(oo.getString("character"));
                else a.setCharacter("\t\t N/A \t");
                if (oo.getString("urlCharacter").length() != 0)
                    a.setUrlCharacter(oo.getString("urlCharacter"));
                else a.setUrlCharacter("\t\t N/A \t");
                String stray = oo.getString("urlPhoto");
                if (stray.length() != 0) {
                    String url = stray.substring(0, stray.length() - 25) + image_extra;
                    a.setUrlPhoto(url);
                } else {
                    stray = "htp/:sd";
                    a.setUrlPhoto(stray);
                }
                a.setUrlProfile(oo.getString("urlProfile"));
                list.add(a);
            }
            try {
                JSONArray filmingLocations = maino.getJSONArray("filmingLocations");
                filming = new String[filmingLocations.length()];
                Log.e("MainActivity.mo.setFilmingLoc", filmingLocations.length() + "");

                for (int i = 0; i < filmingLocations.length(); i++)
                    filming[i] = filmingLocations.getString(i);
                MyTask.mo.setFilmingLoc(filming);
            } catch (Exception e) {
                Log.e("MainActivity.mo.setFilmingLoc", e.toString());
                MyTask.mo.setFilmingLoc(new String[]{"N/A"});
            }
            JSONObject trailor = maino.getJSONObject("trailer");
            title = trailor.getString("title");
            videoURL = trailor.getString("videoURL");
            JSONArray quali = trailor.getJSONArray("qualities");
            for (int i = 0; i < quali.length(); i++) {
                Trailor_Model tm = new Trailor_Model();
                JSONObject q = quali.getJSONObject(i);
                tm.setQuality(q.getString("quality"));
                tm.setVideoURL(q.getString("videoURL"));
                list_t.add(tm);
            }
        } catch (JSONException e) {
            Log.e("JSONException.Nikhil", e.toString());
        }
        setList(list_t);
        return list;
    }

    private static void setList(List<Trailor_Model> list_t) {
        list_te = list_t;
    }

}
