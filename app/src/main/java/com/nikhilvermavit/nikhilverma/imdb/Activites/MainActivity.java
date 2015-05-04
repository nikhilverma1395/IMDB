package com.nikhilvermavit.nikhilverma.imdb.Activites;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nikhilvermavit.nikhilverma.imdb.Fragments.FirstRunDialog;
import com.nikhilvermavit.nikhilverma.imdb.Fragments.Rate_App_Dialog;
import com.nikhilvermavit.nikhilverma.imdb.Models.SqliteModel;
import com.nikhilvermavit.nikhilverma.imdb.R;

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialogFragment;

public class MainActivity extends ActionBarActivity {

    public static int Constant_Shit;
    public static Context context = null;
    static List<SqliteModel> model_dick;
    static boolean MYAPIWORKING;
    static FragmentManager fragmentManager;
    static String GOOGLE = "https://www.google.co.in/#q=";
    static String GOOG = "";
    static String myapifilms1 = "http://www.myapifilms.com/imdb?title=";
    static String MYAPI = "";
    static String myapifilms3 = "&format=JSON&aka=0&business=1&seasons=0&seasonYear=0&te" +
            "chnical=0&filter=N&exactFilter=0&limit=1&year=";
    static String myapifilms5 = "&lang=en-us&actors=S&biography=0&trailer=1&uniqueName=0&filmography" +
            "=0&bornDied=0&starSign=0&actorActress=0&actorTrivia=0&movieTrivia=0&awards=0";
    static String finall = null;
    static ButtonRectangle buttonclick;
    static ProgressBarCircularIndeterminate pb;
    static ButtonFloat b;
    static EditText met, year;
    static ButtonRectangle bt, buttonclick_reset;
    int anim_time;
    boolean nope = false;
    TextView output;
    float trans_noi;
    String title = "", yes = "";
    TextView tv;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.start_enter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        tv = (TextView) findViewById(R.id.no_internet_text);
        trans_noi = tv.getTranslationY();
        tv.setVisibility(View.GONE);
        final ImageView iv = (ImageView) findViewById(R.id.imdb_image);
        float yTr = iv.getTranslationY();
        float xTr = iv.getTranslationY();
        iv.setTranslationY(1920);
        iv.setTranslationX(1080);
        anim_time = getResources().getInteger(android.R.integer.config_longAnimTime);
        iv.animate().translationY(yTr).translationX(xTr).setDuration(anim_time).setListener(null);
        b = (ButtonFloat) findViewById(R.id.buttonflat);
        buttonclick = (ButtonRectangle) findViewById(R.id.buttonclick);
        buttonclick_reset = (ButtonRectangle) findViewById(R.id.buttonclick_reset);

        met = (EditText) findViewById(R.id.name);
        year = (EditText) findViewById(R.id.year);
        pb = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar1);
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
            tv.setVisibility(View.GONE);
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
            Log.d("Final Link", lik + "\n" + MYAPI + "\n" + GOOG);
            requestData(lik, MYAPI);
            Log.e("1", "requested data");
        } else {
            if (!nope) {
                tv.setTranslationY(0);
                nope = true;
                Log.d("Animate", "sexO");
                tv.setVisibility(View.VISIBLE);
                float transX = tv.getTranslationX();
                tv.animate().translationY(trans_noi)
                        .setDuration(1000).setListener(null);
            }
        }
    }


    private void requestData(String uri, String MYAPI) {
        Log.d("2", "executed data");
        new MyTask(true, this).execute(uri, MYAPI);
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


