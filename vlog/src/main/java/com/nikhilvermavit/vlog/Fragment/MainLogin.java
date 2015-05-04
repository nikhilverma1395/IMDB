package com.nikhilvermavit.vlog.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nikhilvermavit.vlog.Http;
import com.nikhilvermavit.vlog.R;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class MainLogin extends Fragment implements View.OnClickListener {
    public static final String url_ = "http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://google.com";
    private static final String url_out = "http://phc.prontonetworks.com/cgi-bin/authlogout";
    public static Context context;
    public static String usagee = "";
    public static String session = "";
    static boolean scan;
    private static String username, pass;
    CountDownTimer cdt;
    int conn_c;
    LinearLayout success_log, usagebut;
    ImageButton dis;
    ButtonRectangle butn, getusage;
    TextView successer, usage;
    private ButtonRectangle login_b, logout_b;
    private RelativeLayout _error;
    private EditText username_, password_;
    private ProgressBarCircularIndeterminate progressBar_vlog;

    public MainLogin() {
    }

    public static MainLogin newInstance(String text) {
        MainLogin f = new MainLogin();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_login, container, false);
        scan = true;
        context = getActivity();
        successer = (TextView) v.findViewById(R.id.successer);
        usage = (TextView) v.findViewById(R.id.usage);
        usagebut = (LinearLayout) v.findViewById(R.id.usagebut);
        success_log = (LinearLayout) v.findViewById(R.id.success_logged_in);
        butn = (ButtonRectangle) v.findViewById(R.id.wifi_not_connected);
        getusage = (ButtonRectangle) v.findViewById(R.id.getusage);
        getusage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Usage().execute(session);
            }
        });
        if (isWifiConnected()) {
            butn.setVisibility(View.INVISIBLE);
        } else {
            butn.setVisibility(View.VISIBLE);
        }
        context = getActivity();
        dis = (ImageButton) v.findViewById(R.id.dismiss_success);
        dis.setOnClickListener(this);
        progressBar_vlog = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressDeterminate);
        try {
            progressBar_vlog.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        login_b = (ButtonRectangle) v.findViewById(R.id.login_b);
        logout_b = (ButtonRectangle) v.findViewById(R.id.logout_b);
        login_b.setOnClickListener(this);
        logout_b.setOnClickListener(this);
        butn.setOnClickListener(this);
        _error = (RelativeLayout) v.findViewById(R.id.wifi_not_connected);
        conn_c = 0;
        boolean ver = getActivity().getSharedPreferences("jack", Context.MODE_PRIVATE).getBoolean("ftiy", false);
        if (ver) {
            nextloginbaby();
        }
        return v;
    }

    public void logoutbaby() {
        login_b.setEnabled(false);
        logout_b.setEnabled(false);
        if (isWifiConnected()) {
            new poster().execute(url_out, "logout_volsbb_7765", "logout_volsbb_7765");
            _error.setVisibility(View.GONE);
        } else {
            float tr = ViewCompat.getTranslationY(_error);
            _error.setVisibility(View.VISIBLE);
            //_error.setAlpha(0f);
            ViewCompat.setTranslationY(_error, 2000);
            ViewCompat.animate(_error).translationY(tr).setDuration(500).setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {

                }

                @Override
                public void onAnimationEnd(View view) {
                    ViewCompat.setAlpha(_error, 1f);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            });
        }
        login_b.setEnabled(true);
        logout_b.setEnabled(true);

    }

    public void turnonwifi() {
        try {
            progressBar_vlog.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Enabling Wifi ..", Toast.LENGTH_SHORT).show();
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (!isWifiConnected()) {
                wifiManager.setWifiEnabled(true);
                setMobileDataEnabled(context, false);
            }
            try {
                Toast.makeText(context, "Enabling Wifi ..", Toast.LENGTH_SHORT).show();
                WifiConfiguration wc = new WifiConfiguration();
                String net = context.getSharedPreferences("prefs.Volsbb", Context.MODE_PRIVATE).getString("networkName", "VOLSBB");
                net = "\"" + net + "\"";
                wc.SSID = net;
                //   Toast.makeText(getApplicationContext(), wc.SSID, Toast.LENGTH_SHORT).show();
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
        if (!isWifiConnected()) {

            new CountDownTimer(6000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (isWifiConnected()) {
                        Toast.makeText(context, "Trying To Log In", Toast.LENGTH_SHORT).show();
                        login_main(true);
                    } else {
                        Toast.makeText(context, "Not Able To Connect To Volsbb ,Is it past 12:30 am ? that might be reason or try Manually Connecting  From Settings and Then login from App . ", Toast.LENGTH_SHORT).show();
                    }
                    progressBar_vlog.setVisibility(View.INVISIBLE);

                }
            }.start();
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

    public void nextloginbaby() {
        getActivity().getSharedPreferences("jack", Context.MODE_PRIVATE).edit().putBoolean("ftiy", true).commit();
        success_log.setVisibility(View.INVISIBLE);

        username = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", "null");
        pass = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", "null");
        Log.d("dsdsd", username + "\t\t" + pass);

        login_b.setEnabled(false);
        logout_b.setEnabled(false);

        if (isWifiConnected()) {
            login_main(true);

        } else {
            Toast.makeText(context, "Click below to enable WiFi (if redirected to browser , click back button to get back to the app. ) .", Toast.LENGTH_SHORT).show();
            float tr = ViewCompat.getTranslationY(_error);
            _error.setVisibility(View.VISIBLE);
            _error.setTranslationY(2000);
            ViewCompat.animate(_error);
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

    public void login_main(boolean ehat) {
        new poster().execute(url_, username, pass);
        _error.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_b:
                nextloginbaby();
                break;
            case R.id.logout_b:
                logoutbaby();
                break;
            case R.id.wifi_not_connected:
                turnonwifi();
                break;
            case R.id.dismiss_success:

                success_log.setVisibility(View.INVISIBLE);
                break;
        }
    }


    private class poster extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            try {
                progressBar_vlog.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String whattoret = "";
            try {

                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 15000);
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
                            if (op.contains("<a href=\"http://115.248.50.60/registration")) {
                                Log.d("yes", "yes");
                                String reg = getLink(op);
                                session = reg;
                            }
                        } else if (op.contains(("Sorry, your free access quota is over".toLowerCase(Locale.getDefault())))) {
                            Log.d("Http Post Response:", "over");
                        } else if (op.contains("Sorry, please check your username and password ")) {
                            Log.d("Http Post Response:", "over");
                            whattoret = "invalid";
                        } else if (op.contains("Sorry, that account does not exist")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "noexist";
                        } else if (op.contains("Logout successful")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "logout";
                        } else if (op.contains("access quota is over.")) {
                            Log.d("Http Post Response:", "loggedsuccess");
                            whattoret = "qover";
                        } else if (op.contains("There is no active session to logout.")) {
                            return "already logged out";
                        } else if (Http.getData("http://www.wikipedia.org/").contains("<title>Wikipedia</title>")) {
                            return "loggedinprev";
                        } else if (op.contains("Sorry, that password was not accepted")) {
                            return "pna";
                        }
                        Log.wtf("wtf", "die");


                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        Log.d("error", "c1");
                    } catch (IOException e) {
                        e.printStackTrace();
                        whattoret = "timeout";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("error", "c3");
                }
                //return whattoret;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error", "c4");

            }
            Log.d("returning f at", whattoret);
            return whattoret;
        }


        private String getLink(String op) {
            Document document = Jsoup.parse(op);
            Elements elements = document.getElementsByClass("orangeText10");
            Element element1 = elements.get(1);
            String links = element1.attr("href");
            Log.d("usage", links);
            return links;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("dsddsds", s);

            if (s.equalsIgnoreCase("true")) {
                success_log.setVisibility(View.VISIBLE);
                float trY = success_log.getTranslationY();
                successer.setText("Logged In Successfully ! ");
                success_log.setTranslationY(0);
                success_log.setBackgroundColor(Color.parseColor("#009688"));
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.VISIBLE);

            }//already logged out
            if (s.equalsIgnoreCase("pna")) {
                success_log.setVisibility(View.VISIBLE);
                float trY = success_log.getTranslationY();
                successer.setText("Sorry, that password was not accepted.");
                success_log.setTranslationY(0);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                // successer.setTextSize(18);
                successer.setTextColor(Color.DKGRAY);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.VISIBLE);

            }
            if (s.equalsIgnoreCase("already logged out")) {
                success_log.setVisibility(View.VISIBLE);
                float trY = success_log.getTranslationY();
                successer.setText(" You Are Already Logged Out .");
                success_log.setTranslationY(0);
                success_log.setBackgroundColor(Color.parseColor("#009688"));
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }
            if (s.equalsIgnoreCase("loggedinprev")) {
                success_log.setVisibility(View.VISIBLE);
                float trY = success_log.getTranslationY();
                successer.setText("You Are Already Logged In .");
                success_log.setTranslationY(0);
                success_log.setBackgroundColor(Color.parseColor("#009688"));
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
            }
            if (s.equalsIgnoreCase("timeout")) {
                success_log.setVisibility(View.VISIBLE);
                successer.setText("Socket Timeout  , Volsbb Is Down or is it past 12:30 am ");
                //  successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }
            if (s.equalsIgnoreCase("logout")) {
                success_log.setVisibility(View.VISIBLE);
                successer.setText("Logout successful");
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }//invalid
            if (s.equalsIgnoreCase("invalid")) {
                success_log.setVisibility(View.VISIBLE);
                successer.setText("Invalid Credentials");
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }
            if (s.equalsIgnoreCase("noexist")) {
                success_log.setVisibility(View.VISIBLE);
                successer.setText("That Account Does Not Exist .");
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }
            if (s.equalsIgnoreCase("qover")) {
                success_log.setVisibility(View.VISIBLE);
                successer.setText("Your Free Access Quota Is Over Buddy... ");
                //successer.setTextSize(22);
                successer.setTextColor(Color.DKGRAY);
                success_log.setBackgroundColor(Color.parseColor("#8bff354b"));
                float trY = success_log.getTranslationY();
                success_log.setTranslationY(0);
                success_log.animate().translationY(trY).setDuration(1500).setListener(null);
                usagebut.setVisibility(View.INVISIBLE);
            }
            try {
                progressBar_vlog.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Usage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar_vlog.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String details = Http.getData(params[0]);
                try {
                    Log.d("Details", details);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String usage = getUsage(details);
                usagee = usage;
                return usage;
            } catch (Exception w) {
                w.printStackTrace();
            }
            return "Not Available , Try Again.";
        }

        private String getUsage(String details) {
            Document document = Jsoup.parse(details);
            Elements elements = document.getElementsByClass("subTextRight");
            Element element = elements.last();
            String sr = element.html().trim();
            Document document1 = Jsoup.parse(sr);
            String element1 = document1.getElementsByTag("b").first().html();
            return element1;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            usagebut.setVisibility(View.VISIBLE);
            progressBar_vlog.setVisibility(View.INVISIBLE);
            usage.setText(s);
        }
    }
}
