package com.example.nikhilverma.imdb.Activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nikhilverma.imdb.Fragments.web;
import com.example.nikhilverma.imdb.Models.Model;
import com.example.nikhilverma.imdb.Models.SqliteModel;
import com.example.nikhilverma.imdb.Models.Trailor_Model;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.parser.Actor_Detail_PARSER;
import com.example.nikhilverma.imdb.parser.Http;
import com.example.nikhilverma.imdb.parser.main_parser;
import com.example.nikhilverma.imdb.sqlite.DataSource;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 08-01-2015.
 */
public class MyTask extends AsyncTask<String, String, Model> {
    static boolean Act_List;

    MyTask(boolean what) {
        Act_List = what;
    }

    @Override
    protected void onPreExecute() {
        if (Act_List) {
            Log.d("3", "preexecuted");
            MainActivity.met.setVisibility(View.INVISIBLE);
            MainActivity.year.setVisibility(View.INVISIBLE);
            MainActivity.b.setVisibility(View.INVISIBLE);
            MainActivity.pb.setVisibility(View.VISIBLE);
        } else {
            Search.pbci.setVisibility(View.VISIBLE);
            Search.list.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected Model doInBackground(String... params) {

        try {
            Log.d("4", "do in back");
            String content = "";
            try {
                content = Http.getData(params[0]);
                MainActivity.mo = main_parser.parseFeed(content);
                Log.e("json 1", "SUCCESS");
            } catch (Exception ee) {
                Log.e("json 1", "Error j1 \n" + ee.toString());
            }
            try {
                MainActivity.newJson = Http.getData(params[1]);
                MainActivity.list = Actor_Detail_PARSER.parseFeednew(MainActivity.newJson);
                Log.e("json 2", "SUCCESS");
                MainActivity.MYAPIWORKING = true;
            } catch (Exception ee) {
                MainActivity.MYAPIWORKING = false;
                Log.e("json 2", "Error j2 \n" + ee.toString());
            }
            if (MainActivity.mo != null && Act_List && checkDuplicateSQLite(MainActivity.mo.getTitle())) {
                Log.d("4", "sqlite_update");
                SqliteModel mod = new SqliteModel();

                try {
                    mod.setTITLE(MainActivity.mo.getTitle());
                    mod.setIMAGE_URL(MainActivity.mo.getPoster());
                    mod.setRATING(Float.parseFloat(MainActivity.mo.getImdbRating()));
                    mod.setYEAR(Integer.parseInt(MainActivity.mo.getYear()));

                    Log.d("et", mod.getRATING() + mod.getTITLE() + mod.getIMAGE_URL() + Integer.parseInt(MainActivity.mo.getYear()) + "");
                    new DataSource(MainActivity.context).create(mod);
                } catch (Exception e) {
                    Log.e("Sqlite In Main Error", e.toString());
                }

                Log.d("6", "mo!=null return mo");
                return MainActivity.mo;
            } else if (MainActivity.mo == null) {
                Log.d("5", "mo==null");
                MainActivity.pb.setVisibility(View.INVISIBLE);
                return null;

            } else {
                return MainActivity.mo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkDuplicateSQLite(String title) {
        Log.e("Dup Check", "Grader");
        DataSource ds = new DataSource(MainActivity.context);
        List<SqliteModel> lert = new ArrayList<>();
        lert = ds.getAllMovies();
        Log.e("Dup Check 34", lert.size() + "");
        for (SqliteModel sw : lert) {

            if (sw.getTITLE().toString().equals(title)) {
                Log.e("Dup Check", sw.getTITLE() + "" + title + "\n");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Model result) {
        if (result == null && MainActivity.mo == null && Act_List) {
            Log.d("6", "result==null show dialog");
            String MS = "Check the Name  For Spelling Mistake or Wrong Year  ";
            final Dialog dialog = new Dialog(MainActivity.context, "Movie Not Found ! ", MS);
            dialog.show();
            ButtonFlat acceptButton = dialog.getButtonAccept();
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.met.setText("");
                    MainActivity.year.setText("");
                    dialog.hide();
                }
            });
            ButtonFlat cancelButton = dialog.getButtonCancel();
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                }
            });
            MainActivity.met.setVisibility(View.VISIBLE);
            MainActivity.year.setVisibility(View.VISIBLE);
            MainActivity.b.setVisibility(View.VISIBLE);
            MainActivity.buttonclick.setVisibility(ViewGroup.VISIBLE);
            MainActivity.buttonclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.fragmentManager.beginTransaction().add(R.id.start, new web(MainActivity.GOOG + "\t imdb")).addToBackStack(null).commit();
                    MainActivity.buttonclick.setVisibility(ViewGroup.GONE);
                    MainActivity.pb.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("7", "result!=null ");
            if (Act_List)
                MainActivity.pb.setVisibility(View.INVISIBLE);
            final String bmp = result.getPoster();
            if (bmp == null)
                Toast.makeText(MainActivity.context, "BAKHCHODIII 23", Toast.LENGTH_LONG).show();
            String film = "";
            String shhootingllov, intern_tr, OTHER_q = "";
            if (MainActivity.MYAPIWORKING) {
                if (!result.getFilmingLoc().equals("")) {
                    for (String a : result.getFilmingLoc()) {
                        film = film + "\t" + a + "\t" + ",";
                    }
                } else film = "   N/A  ";

                if (film.length() == 0)
                    film = "    N/A      ";
                intern_tr = "\t\t\t" + Actor_Detail_PARSER.title + "\n" + "\t\t" + Actor_Detail_PARSER.videoURL + "\t";
                if (intern_tr.length() == 0)
                    intern_tr = " N/A      ";

                OTHER_q = "";
                for (Trailor_Model t : Actor_Detail_PARSER.list_te) {
                    OTHER_q = OTHER_q + "\t \t" + t.getQuality() + "\t:\t" + t.getVideoURL() + "\t\n\n";
                }

                if (OTHER_q.length() == 0)
                    OTHER_q = "\t\t\t\t Other Qualities :\t\t\t\t\n\n" + "\t\t\t\t N/A      ";
                else OTHER_q = "\t\t\t\t Other Qualities :\t\t\t\t\n" +
                        "\t\t\t\t\n" + OTHER_q;
                shhootingllov = "Shooting Locations \t: \t " + film.substring(0, film.length() - 1);
            } else {
                shhootingllov = OTHER_q = intern_tr = "N/A";
            }
            final String[] details = {result.getTitle() + " (" + result.getYear() + ")", "IMDB Rating : " + result.getImdbRating(),
                    "Released Date : " + result.getReleased(), "Runtime : " + result.getRuntime(),
                    "Language :" + result.getLanguage(), "Genre : " + result.getGenre()
                    , "Director : " + result.getDirector(), "Writer : \t" + result.getWriter(),
                    "\tActors :\t\t" + result.getActors(), "Awards : \t" + result.getAwards(),
                    result.getPlot(),
                    "Link :\t\thttp://www.imdb.com/title/" + result.getImdbID() + "/",
                    "IMDB Votes \t\t" + result.getImdbVotes()
                    , shhootingllov, intern_tr, OTHER_q};
            Log.d("8", "Setbit");
            setBit(bmp, details);
        }
    }

    public void setBit(String bbit, String[] det) {
        final String bit = bbit;
        final String[] dete = det;

        if (bit == null)
            Toast.makeText(MainActivity.context, "BAKHCHODIII 232", Toast.LENGTH_LONG).show();
        Intent xxx = new Intent(MainActivity.context, Main_Content.class);
        Log.d("9", "intent done");
        xxx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        xxx.putExtra("gtr", bit);
        xxx.putExtra("detail", dete);
        Log.d("10", "start acti");
        if (!MyTask.Act_List)
            Search.pbci.setVisibility(View.GONE);
        MainActivity.context.startActivity(xxx);

        Log.d("11", "startedact");
    }
}

