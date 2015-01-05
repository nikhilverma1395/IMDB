package com.example.nikhilverma.imdb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//import com.manuelpein ado.fadingactionbar.FadingActionBarHelper;

public class Frag_FAB extends ActionBarActivity {
    private FadingActionBarHelper mFadingHelper;
    static Bitmap _bitmapScaled, bitmap;
    public static Bitmap bmp = null;
    float currentZoom = 1;
    static Fragment actor_detail = null;
    String byteArray;
    ZoomControls zc;
    static Fragment f = null;
    ImageView pid1;
    TextView oTHERQ, movie_name, internationalTrailor, movie_imdbrating, movie_year, movie_runtime, filmingloc,
            movie_language, movie_genre, movie_director, movie_writer, movie_actor, movie_awards, movie_plot, movie_link, movie_votes;
    ScaleAnimation scaleAnim;
    ImageView iv;
    static String[] c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        //getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.all_det);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(toolbar);


        Intent d = getIntent();
        Bundle extras = d.getExtras();
        c = extras.getStringArray("detail");
        byteArray = extras.getString("gtr");
        f = new web(byteArray);
        iv = (ImageView) findViewById(R.id.pid1);
        Picasso.with(this)
                .load(byteArray)
                .error(R.drawable.images)
                .into(iv);
        movie_name = (TextView) findViewById(R.id.movie_name);
        movie_imdbrating = (TextView) findViewById(R.id.movie_imdbrating);
        movie_year = (TextView) findViewById(R.id.movie_year);
        movie_runtime = (TextView) findViewById(R.id.movie_runtime);
        movie_language = (TextView) findViewById(R.id.movie_language);
        movie_genre = (TextView) findViewById(R.id.movie_genre);
        movie_director = (TextView) findViewById(R.id.movie_director);
        movie_writer = (TextView) findViewById(R.id.movie_writer);
        movie_actor = (TextView) findViewById(R.id.movie_actors);
        movie_awards = (TextView) findViewById(R.id.movie_awards);
        movie_plot = (TextView) findViewById(R.id.movie_plot);
        internationalTrailor = (TextView) findViewById(R.id.internationalTrailor);
        oTHERQ = (TextView) findViewById(R.id.other_Qualities);
        movie_link = (TextView) findViewById(R.id.movie_link);
        filmingloc = (TextView) findViewById(R.id.filmingloc);
        movie_votes = (TextView) findViewById(R.id.movie_votes);
        movie_name.setText(c[0]);
        movie_imdbrating.setText(c[1]);
        movie_year.setText(c[2]);
        movie_runtime.setText(c[3]);
        movie_language.setText(c[4]);
        movie_genre.setText(c[5]);
        movie_director.setText(c[6]);
        movie_writer.setText(c[7]);
        movie_actor.setText(c[8]);
        movie_awards.setText(c[9]);
        movie_plot.setText(Html.fromHtml("     <bold>Plot   :</bold> <br /> <p>" + c[10] + "</p>"));
        movie_link.setText(c[11]);
        movie_votes.setText(c[12]);
        filmingloc.setText(c[13]);
        internationalTrailor.setText(c[14]);
        oTHERQ.setText(c[15]);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            try {
                InputStream in = (InputStream) new URL(byteArray).getContent();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                if (bitmap == null)
                    Log.d("First NULL", "First NULL");
                FileOutputStream fo = null;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/actress_wallpaper.jpg");
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
                Toast.makeText(getApplicationContext(), "Image Saved ", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openActorDetailFragment(final View v) {
        if (MainActivity.getList() != null) {
            Intent d = new Intent(Frag_FAB.this, ActorDetailFragment.class);
            startActivity(d);
        } else
            Toast.makeText(this, "Not Avaible", Toast.LENGTH_LONG).show();

    }

    public void openWebview(final View v) {
        getSupportFragmentManager().beginTransaction().add(R.id.contain, f).addToBackStack(null).commit();
    }

    public void openwithWeb(final View v) {
        Log.e("LPGCAY", "hre");
        getSupportFragmentManager().beginTransaction().add(R.id.contain, new web(c[11])).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (f.isAdded())
            getSupportFragmentManager().beginTransaction().remove(f).commit();
    }
}

