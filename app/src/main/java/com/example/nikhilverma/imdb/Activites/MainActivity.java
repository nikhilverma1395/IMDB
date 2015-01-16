package com.example.nikhilverma.imdb.Activites;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.nikhilverma.imdb.Models.ActorDetailModel;
import com.example.nikhilverma.imdb.Models.Model;
import com.example.nikhilverma.imdb.Models.SqliteModel;
import com.example.nikhilverma.imdb.R;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialogFragment;

public class MainActivity extends ActionBarActivity {

    TextView output;
    static List<ActorDetailModel> list = new ArrayList<ActorDetailModel>();
    static List<SqliteModel> model_dick;
    public static int Constant_Shit;
    static boolean MYAPIWORKING;
    static FragmentManager fragmentManager;

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
    static ButtonRectangle buttonclick;
    public static Context context = null;
    static ProgressBarCircularIndetermininate pb;
    static ButtonFloat b;
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
        model_dick = new ArrayList<SqliteModel>();
        Constant_Shit = 0;
        fragmentManager = getSupportFragmentManager();
    }


    public void makemylist(final View fre) {
        startActivity(new Intent(MainActivity.this, Search.class));
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

    public void startFrag(View v) {
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


    private void requestData(String uri, String MYAPI) {
        Log.d("2", "executed data");
        new MyTask(true).execute(uri, MYAPI);
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


}


