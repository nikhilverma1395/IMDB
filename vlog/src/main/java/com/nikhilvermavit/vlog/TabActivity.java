package com.nikhilvermavit.vlog;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.nikhilvermavit.vlog.Dialog.AboutApp;
import com.nikhilvermavit.vlog.Dialog.GetDetails;
import com.nikhilvermavit.vlog.Dialog.RateAPP;
import com.nikhilvermavit.vlog.Fragment.License;
import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Fragment.schedule;
import com.nikhilvermavit.vlog.Services.LoginWhenConnected;
import com.nikhilvermavit.vlog.view.SlidingTabLayout;

import java.util.Random;

/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class TabActivity extends ActionBarActivity implements SlidingTabLayout.TabColorizer {
    private static Context context;
    private Random rand = new Random();
    private float r = rand.nextFloat();
    private float g = rand.nextFloat();
    private float b = rand.nextFloat();
    private int randomColor = Color.rgb((int) r, (int) g, (int) b);
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(new LoginWhenConnected(), intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager_custom);
        String array[] = new String[4];
        array[0] = "Login";
        array[1] = "Accounts";
        array[2] = "Login Timer";
        array[3] = "Licenses";
        context = getApplicationContext();
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), array));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs_custom);
        mSlidingTabLayout.setViewPager(mViewPager);
        makeFirstRunDialog();
        Rate();


    }


    private void Rate() {
        int pr = getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getInt("rate.Nikhil", 0);
        int disable = getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getInt("disable", -1);
        Log.d("disable", disable + "");
        if ((pr > 0) && (pr % 9 == 0) && (disable < 1)) {
            RateAPP.newInstance("\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "TAG.RATE");
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", ++pr).commit();

        } else {
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", ++pr).commit();
        }
        if ((pr > 0) && (pr % 8 == 0) && (disable < 1) && (getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getBoolean("remindlater", false))) {
            RateAPP.newInstance("\t\t\t\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "TAG.RATE");

        }
    }


    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        GetDetails df = GetDetails.newInstance("");
        df.show(fm, "dialoger");
    }

    private void showDialogFragment_Info() {
        FragmentManager fm = getSupportFragmentManager();
        AboutApp info = AboutApp.newInstance("");
        info.show(fm, "dialoger");
    }

    private void makeFirstRunDialog() {
        boolean firstrun = getSharedPreferences("PREFERENCE.VOLSBB", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {

            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", 1).commit();
            showDialogFragment();
            showDialogFragment_Info();
            getSharedPreferences("PREFERENCE.VOLSBB", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }
    }

    @Override
    public int getIndicatorColor(int position) {
        Log.d("here", randomColor + "");
        return 0x00ffffff;
    }

    @Override
    public int getDividerColor(int position) {
        return Color.parseColor("#00FFFFFF");
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        String[] arr;

        public MyPagerAdapter(FragmentManager fm, String[] a) {
            super(fm);
            this.arr = a;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return position + "";
            return arr[position];
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return MainLogin.newInstance("FirstFragment, Instance 1");
                case 1:
                    return ListAccounts.newInstance("ThirdFragment, Instance 1");
                case 2:
                    return schedule.newInstance("");
                case 3:
                    return License.newInstance("");
                default:
                    return MainLogin.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
