package com.example.nikhilverma.imdb.Activites;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nikhilverma.imdb.Models.ActorDetailModel;
import com.example.nikhilverma.imdb.Models.Model;
import com.example.nikhilverma.imdb.Models.SqliteModel;
import com.example.nikhilverma.imdb.Models.Trailor_Model;
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
    public static Model mo;
    static boolean Act_List;
    static boolean OMD;
    static List<ActorDetailModel> list = new ArrayList<>();
    static Context contextw;
    static List<SqliteModel> lert;
    static SqliteModel mod;
    public int anim_time;
    String content = " ", content2 = " ";

    MyTask(boolean what, Context context) {
        contextw = context;
        Act_List = what;
    }

    public static List<ActorDetailModel> getList() {
        return list;
    }

    @Override
    protected void onPreExecute() {
        OMD = false;
        mo = null;
        anim_time = contextw.getResources().getInteger(android.R.integer.config_longAnimTime);
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

            try {
                content = Http.getData(params[0]);
                Log.e("json 1", content);
                mo = main_parser.parseFeed(content);
                Log.e("json 1", "SUCCESS");
                OMD = true;
            } catch (Exception ee) {
                mo = null;
                Log.e("json 1", "Error j1 \n" + ee.toString());
                OMD = false;
            }
            try {
                content2 = Http.getData(params[1]);
                list = Actor_Detail_PARSER.parseFeednew(content2);
                Log.e("json 2", "SUCCESS");
                MainActivity.MYAPIWORKING = true;
            } catch (Exception ee) {
                list = null;
                Log.e("json 2", "Error j2 \n" + ee.toString());
                MainActivity.MYAPIWORKING = false;
            }
            Log.d("title", mo.getTitle());
            if (mo != null && Act_List && checkDuplicateSQLite(mo.getTitle())) {
                Log.d("4", "sqlite_update");
                mod = new SqliteModel();

                try {
                    mod.setTITLE(mo.getTitle());
                    mod.setIMAGE_URL(mo.getPoster());
                    mod.setRATING(Float.parseFloat(mo.getImdbRating()));
                    mod.setYEAR(mo.getYear());
                    Log.d("et", mod.getRATING() + mod.getTITLE() + mod.getIMAGE_URL() + mo.getYear() + "");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new DataSource(MainActivity.context).create(mod);
                        }
                    }).start();

                } catch (Exception e) {
                    Log.e("Sqlite In Main Error", e.toString());
                }

                Log.d("6", "mo!=null return mo");
                return mo;
            }
            if (mo == null) {
                Log.d("5", "mo==null");
                MainActivity.pb.setVisibility(View.INVISIBLE);
                return null;
            } else {
                return mo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkDuplicateSQLite(String title) {
        Log.e("Dup Check", "dup check Grader" + title);
        final DataSource ds_babe = new DataSource(MainActivity.context);
        lert = new ArrayList<>();
        lert = ds_babe.getAllMovies();
        for (SqliteModel sw : lert) {
            if (isCancelled())
                break;
            if (sw.getTITLE().toString().equals(title)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Model result) {
        if (result == null && Act_List) {
        MainActivity.pb.setVisibility(View.GONE);
            Log.d("6", "result==null show dialog");
            String MS = "Check the Name  For Spelling Mistake or Wrong Year  ";
            final Dialog dialog = new Dialog(contextw, "Movie Not Found ! ", MS);
            dialog.show();

            ButtonFlat acceptButton = dialog.getButtonAccept();
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MainActivity.met.setText("");
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
            MainActivity.met.setVisibility(View.INVISIBLE);
            MainActivity.year.setVisibility(View.INVISIBLE);
            MainActivity.b.setVisibility(View.INVISIBLE);
            MainActivity.buttonclick.setVisibility(ViewGroup.VISIBLE);
            MainActivity.buttonclick_reset.setVisibility(ViewGroup.VISIBLE);
            MainActivity.buttonclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    String query = MainActivity.met.getText().toString();
                    intent.putExtra(SearchManager.QUERY, query + "\t Imdb");
                    contextw.startActivity(intent);
                    MainActivity.buttonclick.setVisibility(ViewGroup.GONE);
                    MainActivity.pb.setVisibility(View.GONE);
                    MainActivity.buttonclick_reset.setVisibility(ViewGroup.GONE);
                }
            });
            MainActivity.buttonclick_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Name Field
                    MainActivity.pb.setVisibility(View.GONE);
                    MainActivity.met.setVisibility(View.VISIBLE);
                    float gy = MainActivity.met.getTranslationX();
                    MainActivity.met.setTranslationX(1080);
                    Log.d("sdsd", gy + "");
                    MainActivity.met.animate().translationX(gy).setDuration(anim_time).setListener(null);
//Year Field

                    MainActivity.year.setVisibility(View.VISIBLE);
                    float gey = MainActivity.year.getTranslationX();
                    MainActivity.year.setTranslationX(1100);
                    Log.d("sdsd", gy + "");
                    MainActivity.year.animate().translationX(gey).setDuration(anim_time).setListener(null);
                    //Button
                    MainActivity.b.setVisibility(View.VISIBLE);
                    float trYButton = MainActivity.b.getTranslationY();
                    MainActivity.b.setTranslationY(1950);
                    MainActivity.b.animate().translationY(trYButton).setDuration(anim_time).setListener(null);

                    MainActivity.buttonclick.setVisibility(ViewGroup.GONE);
                    MainActivity.buttonclick_reset.setVisibility(ViewGroup.GONE);
                    MainActivity.met.setText("");
                    MainActivity.year.setText("");

                }
            });

        } else {
            Log.d("7", "result!=null ");
            if (Act_List)
                MainActivity.pb.setVisibility(View.INVISIBLE);
            String bmp = "";
            try {
                bmp = result.getPoster();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String film = "";
            String shhootingllov, intern_tr, OTHER_q = "";
            if (OMD) {
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
                    , shhootingllov, intern_tr, OTHER_q, result.getImdbRating()};
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
        contextw.startActivity(xxx);

    }
}

