package com.nikhilvermavit.vlog;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.nikhilvermavit.vlog.Dialog.AboutApp;
import com.nikhilvermavit.vlog.Dialog.GetDetails;
import com.nikhilvermavit.vlog.Dialog.RateAPP;
import com.nikhilvermavit.vlog.Fragment.License;
import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Fragment.schedule;
import com.nikhilvermavit.vlog.Services.LoginWhenConnected;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.view.SlidingTabLayout;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class TabActivity extends AppCompatActivity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    public static Context context;
    private static AssetManager assetManager;
    public static SharedPrefs sharedPrefs;

    public static Typeface getRaleway(int type) {
        switch (type) {
            case Config.RALEWAY_BOLD:
                return Typeface.createFromAsset(assetManager, "Raleway-Bold.ttf");
            case Config.RALEWAY_LIGHT:
                return Typeface.createFromAsset(assetManager, "Raleway-Light.ttf");
            case Config.RALEWAY_REG:
                return Typeface.createFromAsset(assetManager, "Raleway-Regular.ttf");
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        activityResumed();
        sharedPrefs = new SharedPrefs(this);
        assetManager = getAssets();
        setContentView(R.layout.custom);
        if (TabActivity.sharedPrefs.getBoolValue(Config.autoLogin, true)) {
            try {
                final IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                registerReceiver(new LoginWhenConnected(), filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#263238");
            }
        });
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        makeFirstRunDialog();
        Rate();


    }


    private void Rate() {
        int pr = sharedPrefs.getIntValue(Config.rateValPREF, 0);
        int disable = sharedPrefs.getIntValue(Config.disable_ratePREF, -1);
        if ((pr > 0) && (pr % 9 == 0) && (disable < 1)) {
            RateAPP.newInstance("\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "rate");
            sharedPrefs.storeIntValue(Config.rateValPREF, ++pr);

        } else {
            sharedPrefs.storeIntValue(Config.rateValPREF, ++pr);
        }
        if ((pr > 0) && (pr % 8 == 0) && (disable < 1) && (sharedPrefs.getBoolValue(Config.remindLaterPREF))) {
            RateAPP.newInstance("\t\t\t\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "tag1");

        }
    }


    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        GetDetails df = GetDetails.newInstance("");
        df.show(fm, "tag3");
    }

    private void showDialogFragment_Info() {
        FragmentManager fm = getSupportFragmentManager();
        AboutApp info = AboutApp.newInstance("");
        info.show(fm, "tag2");
    }

    private void makeFirstRunDialog() {
        boolean firstrun = sharedPrefs.getBoolValue(Config.firstRunPREF);
        if (!firstrun) {
            new DataSource(getApplicationContext()).Delete();
            sharedPrefs.storeIntValue(Config.rateValPREF, 1);
            showDialogFragment();
            showDialogFragment_Info();
            sharedPrefs.storeBoolValue(Config.firstRunPREF, true);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        String[] arr;

        public MyPagerAdapter(FragmentManager fm, String[] a) {
            super(fm);
            this.arr = a;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arr[position];
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return MainLogin.newInstance("main");
                case 1:
                    return ListAccounts.newInstance("list");
                case 2:
                    return schedule.newInstance("sch");
                case 3:
                    return License.newInstance("lic");
                default:
                    return MainLogin.newInstance("main");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible = false;

    @Override
    protected void onStop() {
        super.onStop();
        activityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityPaused();
    }
}