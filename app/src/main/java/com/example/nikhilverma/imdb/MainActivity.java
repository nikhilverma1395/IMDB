package com.example.nikhilverma.imdb;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;
import com.gc.materialdesign.widgets.Dialog;

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialogFragment;

public class MainActivity extends ActionBarActivity {

    TextView output, buttontext;
    private static List<newModel> list = new ArrayList<>();

    public static List<newModel> getList() {
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
    ProgressBarCircularIndetermininate pb;
    ButtonFloat b;
    String title = "", yes = "";
    static EditText met, year;
    static Model mo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_enter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        b = (ButtonFloat) findViewById(R.id.buttonflat);
        buttonclick = (ButtonRectangle) findViewById(R.id.buttonclick);
        buttontext = (TextView) findViewById(R.id.buttontext);


        met = (EditText) findViewById(R.id.name);
        year = (EditText) findViewById(R.id.year);
        pb = (ProgressBarCircularIndetermininate) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        met.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonclick.setVisibility(ViewGroup.GONE);
                buttontext.setVisibility(ViewGroup.GONE);

            }
        });
    }

    public void startFrag(View v) {
        if (isOnline()) {
            title = met.getText().toString();
            yes = year.getText().toString();
            title = title.trim();

            //MYAPI = myapifilms1 + title + myapifilms3;
            //if (year.length() == 4)
            //  MYAPI = myapifilms1 + title + myapifilms3 + year + myapifilms5;

            //else MYAPI = myapifilms1 + title + myapifilms3 + 0 + myapifilms5;
            Log.e("API", MYAPI);
            int sp = title.length() - title.replaceAll(" ", "").length();
            String[] splited = title.split("\\s+");
            GOOG = "";
            for (int i = 0; i < splited.length; i++) {
                GOOG = GOOG + splited[i] + "+";
            }
            Log.e("LOGCAT", (GOOGLE + GOOG).substring(0, (GOOGLE + GOOG).length() - 1));
            String d = "";
            finall = "";
            GOOGLE = "https://www.google.co.in/#q=";
            GOOGLE = GOOGLE + GOOG;
            finall = GOOGLE;
            if (!(yes.length() == 4 || yes.length() == 0)) {
                Toast.makeText(getApplicationContext(), "Invalid Year Enter Between 1900-2016", Toast.LENGTH_LONG).show();
                year.setText("");
            }
            if (sp == 0) {
                d = "http://www.omdbapi.com/?t=" + title + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + title + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + title + myapifilms3 + 0 + myapifilms5;
            }
            if (sp == 1) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + myapifilms3 + 0 + myapifilms5;

            }
            if (sp == 2) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + myapifilms3 + 0 + myapifilms5;

            }
            if (sp == 3) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + myapifilms3 + 0 + myapifilms5;


            }
            if (sp == 4) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + myapifilms3 + 0 + myapifilms5;

            }

            if (sp == 5) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + myapifilms3 + 0 + myapifilms5;

            }

            if (sp == 6) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + myapifilms3 + 0 + myapifilms5;

            }

            if (sp == 7) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + myapifilms3 + 0 + myapifilms5;

            }

            if (sp == 8) {
                d = "http://www.omdbapi.com/?t=" + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + "%20" + splited[8] + "&y=" + yes + "&plot=full&r=json";
                if (yes.length() == 4)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + "%20" + splited[8] + myapifilms3 + year + myapifilms5;
                if (yes.length() == 0)
                    MYAPI = myapifilms1 + splited[0] + "%20" + splited[1] + "%20" + splited[2] + "%20" + splited[3] + "%20" + splited[4] + "%20" + splited[5] + "%20" + splited[6] + "%20" + splited[7] + "%20" + splited[8] + myapifilms3 + 0 + myapifilms5;

            }
            Log.d("Final Link", d);
            requestData(d);


//            Toast.makeText(getApplicationContext(), "Im here", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }


    }


    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
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


    public void setBit(String bbit, String[] det) {
        final String bit = bbit;
        final String[] dete = det;

        if (bit == null)
            Toast.makeText(getApplicationContext(), "BAKHCHODIII 232", Toast.LENGTH_LONG).show();
        Intent xxx = new Intent(getApplicationContext(), Frag_FAB.class);
        xxx.putExtra("gtr", bit);
        xxx.putExtra("detail", dete);
        startActivity(xxx);
    }

    private class MyTask extends AsyncTask<String, String, Model> {
        @Override
        protected void onPreExecute() {
//			updateDisplay("Starting task");
            met.setVisibility(View.INVISIBLE);
            year.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);

            //pb.set
        }

        @Override
        protected Model doInBackground(String... params) {
            try {
                String content = Http.getData(params[0]);
                newJson = Http.getData(MYAPI);
                MainActivity.mo = parser.parseFeed(content);
                list = Trailor_else.parseFeednew(newJson);
                if (mo == null) {
                    Log.e("eroorrr", "I am Here");
                    pb.setVisibility(View.INVISIBLE);
                    return null;
                } else {
                    return mo;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Model result) {

            // flowerList = FlowerXMLParser.parseFeed(result);
            // updateDisplay();
            Log.e("start", "start");
            //        getActorDetails(list);
            Log.e("end", "end");
            if (result == null) {
                String MS = "Check the Name  For Spelling Mistake or Wrong Year  ";
                final Dialog dialog = new Dialog(MainActivity.this, "Movie Not Found ! ", MS);
                dialog.show();

                Log.e("eroorrr", "I am Here 2");
                // Acces to accept button
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
                buttontext.setVisibility(ViewGroup.VISIBLE);

                buttonclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction().add(R.id.start, new web(finall + "\t imdb")).addToBackStack(null).commit();
                        buttonclick.setVisibility(ViewGroup.GONE);
                        buttontext.setVisibility(ViewGroup.GONE);
                        pb.setVisibility(View.GONE);
                    }
                });


            } else {
                pb.setVisibility(View.INVISIBLE);

                //          Toast.makeText(getApplicationContext(), result.getImdbRating(), Toast.LENGTH_LONG).show();
                final String bmp = result.getPoster();
                if (bmp == null)
                    Toast.makeText(getApplicationContext(), "BAKHCHODIII 23", Toast.LENGTH_LONG).show();
                String film = "";
                if (!result.getFilmingLoc().equals("")) {
                    for (String a : result.getFilmingLoc()) {
                        film = film + "\t" + a + "\t" + ",";
                    }
                } else film = "   N/A  ";

                if (film.length() == 0)
                    film = "    N/A      ";
                String intern_tr = "\t\t\t" + Trailor_else.title + "\n" + "\t\t" + Trailor_else.videoURL + "\t";
                if (intern_tr.length() == 0)
                    intern_tr = " N/A      ";

                String OTHER_q = "";
                for (Trailor_Model t : Trailor_else.list_te) {
                    OTHER_q = OTHER_q + "\t \t" + t.getQuality() + "\t:\t" + t.getVideoURL() + "\t\n\n";

                }
                if (OTHER_q.length() == 0)
                    OTHER_q = "\t\t\t\t Other Qualities :\t\t\t\t\n\n" + "\t\t\t\t N/A      ";
                else OTHER_q = "\t\t\t\t Other Qualities :\t\t\t\t\n" +
                        "\t\t\t\t\n" + OTHER_q;
                final String[] details = {result.getTitle() + " (" + result.getYear() + ")", "IMDB Rating : " + result.getImdbRating(),
                        "Released Date : " + result.getReleased(), "Runtime : " + result.getRuntime(),
                        "Language :" + result.getLanguage(), "Genre : " + result.getGenre()
                        , "Director : " + result.getDirector(), "Writer : \t" + result.getWriter(),
                        "\tActors :\t\t" + result.getActors(), "Awards : \t" + result.getAwards(),
                        result.getPlot(),
                        "Link :\t\thttp://www.imdb.com/title/" + result.getImdbID() + "/",
                        "IMDB Votes \t\t" + result.getImdbVotes()
                        , "Shooting Locations \t: \t " + film.substring(0, film.length() - 1), intern_tr, OTHER_q};

                setBit(bmp, details);


            }
        }

    }

}


