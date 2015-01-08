package com.example.nikhilverma.imdb.Activites;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhilverma.imdb.Fragments.FirstRunDialog;
import com.example.nikhilverma.imdb.Fragments.Rate_App_Dialog;
import com.example.nikhilverma.imdb.Fragments.web;
import com.example.nikhilverma.imdb.Models.ActorDetailModel;
import com.example.nikhilverma.imdb.Models.Model;
import com.example.nikhilverma.imdb.Models.SqliteModel;
import com.example.nikhilverma.imdb.Models.Trailor_Model;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.parser.Actor_Detail_PARSER;
import com.example.nikhilverma.imdb.parser.Http;
import com.example.nikhilverma.imdb.parser.main_parser;
import com.example.nikhilverma.imdb.sqlite.DataSource;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;
import com.gc.materialdesign.widgets.Dialog;

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialogFragment;

public class MainActivity extends ActionBarActivity {

    TextView output;
    private static List<ActorDetailModel> list = new ArrayList<>();
    private static List<SqliteModel> model_dick;
    public static int Constant_Shit;
    MyTask task = new MyTask();
    static boolean MYAPIWORKING;

    public static List<ActorDetailModel> getList() {
        return list;
    }

    static String GOOGLE = "https://www.google.co.in/#q=";
    static String GOOG = "";
    static String myapifilms1 = "http://www.myapifilms.com/imdb?title=";
    static String MYAPI = "";
    static String myapifilms3 = "&format=JSON&aka=0&business=1&seasons=0&seasonYear=0&te" +
            "chnical=0&filter=N&exactFilter=0&limit=1&year=";
    static String myapifilms5 = "&lang=en-us&actors=S&biography=0&trailer=1&uniqueName=0&filmography" +
            "=0&bornDied=0&starSign=0&actorActress=0&actorTrivia=0&movieTrivia=0&awards=0";
    static String finall = null;
    static String newJson = "";
    ButtonRectangle buttonclick;
    public static Context context = null;
    ProgressBarCircularIndetermininate pb;
    ButtonFloat b;
    String title = "", yes = "";
    static EditText met, year;
    static ButtonRectangle bt;
    public static Model mo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_enter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        b = (ButtonFloat) findViewById(R.id.buttonflat);
        buttonclick = (ButtonRectangle) findViewById(R.id.buttonclick);


        met = (EditText) findViewById(R.id.name);
        year = (EditText) findViewById(R.id.year);
        pb = (ProgressBarCircularIndetermininate) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        bt = (ButtonRectangle) findViewById(R.id.search_button);
        met.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonclick.setVisibility(ViewGroup.GONE);

            }
        });
        makeFirstRunDialog();
        Rate();
        context = getApplicationContext();
        model_dick = new ArrayList<>();
        Constant_Shit = 0;
    }

    private void Rate() {
        int pr = getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getInt("rate.Nikhil", 0);
        if ((pr > 0) && (pr % 9 == 0)) {
            Rate_App_Dialog.newInstance("\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "TAG.RATE");
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", ++pr).commit();
        } else {
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", ++pr).commit();
        }
        if ((pr > 0) && (pr % 6 == 0) && (getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getBoolean("remindlater", false))) {
            Rate_App_Dialog.newInstance("\t\t\t\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "TAG.RATE");

        }
    }


    private void makeFirstRunDialog() {
        boolean firstrun = getSharedPreferences("PREFERENCE.IMDB", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", 1).commit();
            showDialogFragment();
            getSharedPreferences("PREFERENCE.IMDB", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }
    }

    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FirstRunDialog df = FirstRunDialog.newInstance("\t\t\t\t Details Abput IMDB!");
        df.show(fm, "dialo");
    }


    public void startFragDup(String a, String b) {
        try {
            requestData(a, b);
        } catch (Exception e) {
            Log.d("LOGCAT_SERACH_CALL", e.toString());
        }
    }

    public void startFragDup() {
        if (isOnline()) {
            Constant_Shit++;
            title = met.getText().toString();
            yes = year.getText().toString();
            title = title.trim();
            String d = "";

            if (!(yes.length() == 4 || yes.length() == 0)) {
                Toast.makeText(getApplicationContext(), "Invalid Year Enter Between 1900-2016", Toast.LENGTH_LONG).show();
                year.setText("");
                yes = "";
            }
            for (int i = 0; i < title.length(); i++) {
                if (title.charAt(i) == ' ') {
                    d = d + "%20";
                    if (i != title.length() - 1)
                        GOOG = GOOG + "+";
                } else {
                    d = d + title.charAt(i);
                    GOOG = GOOG + title.charAt(i);
                }
            }
            GOOG = GOOGLE + GOOG;
            String lik = "http://www.omdbapi.com/?t=" + d + "&y=" + yes + "&plot=full&r=json";
            MYAPI = myapifilms1 + d + myapifilms3 + yes + myapifilms5;
            Log.d("Final Link", d + "/n" + MYAPI + "\n" + GOOG);
            requestData(lik, MYAPI);
            Log.e("1", "requested data");


//            Toast.makeText(getApplicationContext(), "Im here", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }

    public void startFrag(View v) {
        startFragDup();
    }


    private void requestData(String uri, String MYAPI) {
        Log.d("2", "executed data");
        task.execute(uri, MYAPI);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.license) {
            final LicensesDialogFragment fragment = LicensesDialogFragment.newInstance(R.raw.notices, false, true, R.style.custom_theme, R.color.custom_divider_color, this);
            fragment.show(getSupportFragmentManager(), null);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        met.setVisibility(View.VISIBLE);
        year.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);


    }

    public void makemylist(final View fre) {
        startActivity(new Intent(MainActivity.this, Search.class));
    }

    public void setBit(String bbit, String[] det) {
        final String bit = bbit;
        final String[] dete = det;

        if (bit == null)
            Toast.makeText(getApplicationContext(), "BAKHCHODIII 232", Toast.LENGTH_LONG).show();
        Intent xxx = new Intent(getApplicationContext(), Main_Content.class);
        Log.d("9", "intent done");
        xxx.putExtra("gtr", bit);
        xxx.putExtra("detail", dete);
        Log.d("10", "start acti");
        startActivity(xxx);
        Log.d("11", "startedact");
    }


    private class MyTask extends AsyncTask<String, String, Model> {
        @Override
        protected void onPreExecute() {
            Log.d("3", "preexecuted");
            met.setVisibility(View.INVISIBLE);
            year.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);

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
                    newJson = Http.getData(params[1]);
                    list = Actor_Detail_PARSER.parseFeednew(newJson);
                    Log.e("json 2", "SUCCESS");
                    MainActivity.MYAPIWORKING = true;
                } catch (Exception ee) {
                    MainActivity.MYAPIWORKING = false;
                    Log.e("json 2", "Error j2 \n" + ee.toString());
                }
                if (MainActivity.mo != null) {
                    Log.d("4", "sqlite_update");
                    SqliteModel mod = new SqliteModel();

                    try {
                        mod.setTITLE(mo.getTitle());
                        mod.setIMAGE_URL(mo.getPoster());
                        mod.setRATING(Float.parseFloat(mo.getImdbRating()));
                        mod.setYEAR(Integer.parseInt(mo.getYear()));

                        Log.d("et", mod.getRATING() + mod.getTITLE() + mod.getIMAGE_URL() + Integer.parseInt(mo.getYear()) + "");
                        new DataSource(context).create(mod);
                    } catch (Exception e) {
                        Log.e("Sqlite In Main Error", e.toString());
                    }

                    Log.d("6", "mo!=null return mo");
                    return MainActivity.mo;
                } else {
                    Log.d("5", "mo==null");
                    pb.setVisibility(View.INVISIBLE);
                    return null;

                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Model result) {
            if (result == null && MainActivity.mo == null) {
                Log.d("6", "result==null show dialog");
                String MS = "Check the Name  For Spelling Mistake or Wrong Year  ";
                final Dialog dialog = new Dialog(MainActivity.this, "Movie Not Found ! ", MS);
                dialog.show();
                ButtonFlat acceptButton = dialog.getButtonAccept();
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        met.setText("");
                        year.setText("");
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
                met.setVisibility(View.VISIBLE);
                year.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                buttonclick.setVisibility(ViewGroup.VISIBLE);
                buttonclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction().add(R.id.start, new web(finall + "\t imdb")).addToBackStack(null).commit();
                        buttonclick.setVisibility(ViewGroup.GONE);
                        pb.setVisibility(View.GONE);
                    }
                });
            } else {
                Log.d("7", "result!=null ");
                pb.setVisibility(View.INVISIBLE);
                final String bmp = result.getPoster();
                if (bmp == null)
                    Toast.makeText(getApplicationContext(), "BAKHCHODIII 23", Toast.LENGTH_LONG).show();
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
                        , shhootingllov , intern_tr, OTHER_q};
                Log.d("8", "Setbit");
                setBit(bmp, details);
            }
        }

    }

}


