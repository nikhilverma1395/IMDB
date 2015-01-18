package com.example.nikhilverma.imdb.Activites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhilverma.imdb.Adapters.mCursorAdapter;
import com.example.nikhilverma.imdb.Models.SqliteModel;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Views.BounceListView;
import com.example.nikhilverma.imdb.Views.swipeDetector;
import com.example.nikhilverma.imdb.sqlite.DataSource;
import com.example.nikhilverma.imdb.sqlite.SqliteHelper;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class Search extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String logTag = "ListSwipeDetector";
    static BounceListView list;
    static ProgressBarCircularIndetermininate pbci;
    static String myapifilms1 = "http://www.myapifilms.com/imdb?title=";
    static String MYAPI = "";
    static String myapifilms3 = "&format=JSON&aka=0&business=1&seasons=0&seasonYear=0&te" +
            "chnical=0&filter=N&exactFilter=0&limit=1&year=";
    static String myapifilms5 = "&lang=en-us&actors=S&biography=0&trailer=1&uniqueName=0&filmography" +
            "=0&bornDied=0&starSign=0&actorActress=0&actorTrivia=0&movieTrivia=0&awards=0";
    private Context context;
    private float downX, downY, upX, upY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search);
        list = (BounceListView) findViewById(R.id.list);
        List<SqliteModel> sqlist = new ArrayList<SqliteModel>();
        pbci = (ProgressBarCircularIndetermininate) findViewById(R.id.progressbar_search);
        context = getApplicationContext();
        TextView rt = (TextView) findViewById(R.id.error_inlist);
        rt.setVisibility(View.GONE);
        try {
            DataSource source = new DataSource(MainActivity.context);
            source.open();
            SQLiteDatabase sql = source.sqLiteDatabase;
            Cursor query = sql.rawQuery("select rowid _id,* from " + SqliteHelper.TABLE_MOVIE, null);//only god and i know what i am doing
            mCursorAdapter adapter = new mCursorAdapter(MainActivity.context, query);
            list.setAdapter(adapter);
//            rt.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.e("Conecting to Database Error  : ", e.toString());
            rt.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        }

        list.setOnItemClickListener(this);
        list.setOnTouchListener(new swipeDetector());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (new swipeDetector().swipeDetected() && new swipeDetector().getAction() == swipeDetector.Action.RL) {

        } else {
            String lik = "";
            DataSource sql = new DataSource(MainActivity.context);
            try {
                List<SqliteModel> sq = sql.getAllMovies();
                String wer = sq.get(position).getTITLE();
                wer = wer.trim();
                String temp = "";
                for (int i = 0; i < wer.length(); i++) {
                    if (wer.charAt(i) == ' ') {
                        temp = temp + "%20";
                    } else {
                        temp = temp + wer.charAt(i);
                    }

                }
                lik = "http://www.omdbapi.com/?t=" + temp + "&y=" + sq.get(position).getYEAR() + "&plot=full&r=json";
                MYAPI = myapifilms1 + temp + myapifilms3 + sq.get(position).getYEAR() + myapifilms5;
                Log.e("lik", lik + "\n" + MYAPI);
            } catch (Exception e) {
                Log.e("GET ON item CLick", e.toString());
            }
            if (isOnline()) {
                new MyTask(false).execute(lik, MYAPI);
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }

        }
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
        list.setVisibility(View.VISIBLE);
    }


}
