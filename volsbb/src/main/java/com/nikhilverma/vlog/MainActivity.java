package com.nikhilverma.vlog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    private static final String url_ = "http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://google.com";
    private static final String url_out = "http://phc.prontonetworks.com/cgi-bin/authlogout";
    static boolean scan;
    private static String username, pass;
    CountDownTimer cdt;
    int conn_c;
    TextView success_log;
    private Button login_b, logout_b, reset_b, butn;
    private LinearLayout _error;
    private EditText username_, password_;
    private ProgressBarCircularIndetermininate progressBar_vlog;
    private List<ScanResult> scanResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_main);
        //username_ = (EditText) findViewById(R.id.username_);
        //password_ = (EditText) findViewById(R.id.password_);
        scan = true;
        success_log = (TextView) findViewById(R.id.success_logged_in);
        butn = (Button) findViewById(R.id.turn_on_wifi);
        progressBar_vlog = (ProgressBarCircularIndetermininate) findViewById(R.id.progressBar_vlog);
        login_b = (Button) findViewById(R.id.login_b);
        //reset_b = (Button) findViewById(R.id.reset_);
        logout_b = (Button) findViewById(R.id.logout_b);
        _error = (LinearLayout) findViewById(R.id.wifi_not_connected);
        conn_c = 0;
        makeFirstRunDialog();
        Rate();

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
        if ((pr > 0) && (pr % 8 == 0) && (getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).getBoolean("remindlater", false))) {
            Rate_App_Dialog.newInstance("\t\t\t\tRate : \t" + getResources()
                    .getString(R.string.app_name) + "\t.")
                    .show(getSupportFragmentManager(), "TAG.RATE");

        }
    }

    public void resetsprefs(final View v) {

        showDialogFragment();
    }

    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FirstRunDialog df = FirstRunDialog.newInstance("");
        df.show(fm, "dialoger");
    }

    private void makeFirstRunDialog() {
        boolean firstrun = getSharedPreferences("PREFERENCE.VOLSBB", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {
            getSharedPreferences("PREFERENCE.IMDB.RATE", MODE_PRIVATE).edit().putInt("rate.Nikhil", 1).commit();
            showDialogFragment();
            getSharedPreferences("PREFERENCE.VOLSBB", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginbaby(final View v) {
        success_log.setVisibility(View.INVISIBLE);
        username = getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", "null");
        pass = getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", "null");
        Log.d("dsdsd", username + "\t\t" + pass);

        login_b.setEnabled(false);
        logout_b.setEnabled(false);

        if (isWifiConnected()) {
            new poster().execute(url_, username, pass);
            _error.setVisibility(View.GONE);

        } else {
            Toast.makeText(getApplicationContext(), "Click the below button to turn on Wifi and connect to Volsbb and then press login again .", Toast.LENGTH_LONG).show();
            float tr = _error.getTranslationY();
            _error.setVisibility(View.VISIBLE);
            _error.setTranslationY(2000);
            _error.animate().translationY(tr).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    _error.setAlpha(1f);

                }
            });
        }
        login_b.setEnabled(true);
        logout_b.setEnabled(true);

    }

    public void logoutbaby(final View viewer) {
        login_b.setEnabled(false);
        logout_b.setEnabled(false);
        if (isWifiConnected()) {
            new poster().execute(url_out, "logout_volsbb_7765", "logout_volsbb_7765");
            _error.setVisibility(View.GONE);
        } else {
            float tr = _error.getTranslationY();
            _error.setVisibility(View.VISIBLE);
            //_error.setAlpha(0f);
            _error.setTranslationY(2000);
            _error.animate().translationY(tr).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    _error.setAlpha(1f);

                }
            });
        }
        login_b.setEnabled(true);
        logout_b.setEnabled(true);

    }

    private boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    public void turnonwifi(final View view) {
        try {

            Toast.makeText(getApplicationContext(), "Enabling Wifi ..", Toast.LENGTH_SHORT).show();
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (!isWifiConnected()) {
                wifiManager.setWifiEnabled(true);
                setMobileDataEnabled(this, false);
            }
            Log.d("sds", "HERE baby");
            try {
                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = "\"VOLSBB\"";
                wc.status = WifiConfiguration.Status.ENABLED;
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                int netId = wifiManager.addNetwork(wc);
                wifiManager.enableNetwork(netId, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isWifiConnected()) {

                butn.setText("Wifi Turned On");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    private class poster extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar_vlog.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String whattoret = "";
            try {

                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 10000);
                    HttpClient httpClient = new DefaultHttpClient(httpParameters);
                    HttpPost httpPost = new HttpPost(params[0]);
                    httpPost.setHeader("User-Agent", "MySuperUserAgent");
                    if (!params[1].equals("logout_volsbb_7765")) {
                        List<NameValuePair> nameValuePair = new ArrayList<>(2);
                        nameValuePair.add(new BasicNameValuePair("userId", params[1]));
                        nameValuePair.add(new BasicNameValuePair("password", params[2]));
                        nameValuePair.add(new BasicNameValuePair("serviceName", "ProntoAuthentication"));
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    try {

                        HttpResponse response = httpClient.execute(httpPost);
                        String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                        Log.d("Response:", op);
                        if (op.contains("Successful Pronto Authentication")) {
                            Log.d("Http Post Response:", "success");
                            whattoret = "true";
                        }
                        if (op.contains(("Sorry, your free access quota is over".toLowerCase(Locale.getDefault())))) {
                            Log.d("Http Post Response:", "over");
                        }
                        if (op.contains("Sorry, please check your username and password ")) {
                            Log.d("Http Post Response:", "over");
                            whattoret = "invalid";
                        }
                        if (op.contains("Sorry, that account does not exist")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "noexist";
                        }
                        if (op.contains("Logout successful")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "logout";
                        }
                        if (op.contains("access quota is over.")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "qover";
                        }

                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //return whattoret;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("returning from Asynctask", whattoret);
            return whattoret;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("dsddsds", s);
            if (s.equalsIgnoreCase("true")) {
                success_log.setVisibility(View.VISIBLE);
                float trY = success_log.getTranslationY();
                success_log.setText("Logged In Successfully ! ");
                success_log.setTranslationY(0);
                success_log.setBackgroundColor(Color.parseColor("#009688"));
                success_log.setTextSize(22);
                success_log.setTextColor(Color.DKGRAY);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }
            if (s.equalsIgnoreCase("logout")) {
                success_log.setVisibility(View.VISIBLE);
                success_log.setText("Logout successful");
                success_log.setTextSize(22);
                success_log.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.RED);
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }//invalid
            if (s.equalsIgnoreCase("invalid")) {
                success_log.setVisibility(View.VISIBLE);
                success_log.setText("Invalid Credentials");
                success_log.setTextSize(22);
                success_log.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.RED);
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }
            if (s.equalsIgnoreCase("noexist")) {
                success_log.setVisibility(View.VISIBLE);
                success_log.setText("That Account Does Not Exist .");
                success_log.setTextSize(22);
                success_log.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.RED);
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }
            if (s.equalsIgnoreCase("qover")) {
                success_log.setVisibility(View.VISIBLE);
                success_log.setText("Your Free Access Quota Is Over bud . ");
                success_log.setTextSize(22);
                success_log.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.RED);
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }
            progressBar_vlog.setVisibility(View.GONE);
        }
    }
}
