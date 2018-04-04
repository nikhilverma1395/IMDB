package com.nikhilvermavit.vlog.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Http;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.TabActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class MainLogin extends Fragment implements View.OnClickListener {
    public static final String LOGIN_LINK = "http://phc.prontonetworks.com/cgi-bin/authlogin";
    private static final String LOGOUT_LINK = "http://phc.prontonetworks.com/cgi-bin/authlogout";
    public static Context context;
    public static String session = "";
    private int datern_day;
    private static String username, pass;
    private ButtonRectangle getusage;
    private TextView usage, appStartTv;
    private ButtonRectangle login_b, logout_b;
    private Button butn;
    private ProgressBarCircularIndeterminate progressBar_vlog;
    private CheckBox appStartLogin;

    public MainLogin() {
    }

    public static MainLogin newInstance(String text) {
        MainLogin f = new MainLogin();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    private void makeUsageVis() {
        getusage.setVisibility(View.VISIBLE);
        usage.setVisibility(View.VISIBLE);
        usage.setText(" - ");
    }

    private void makeUsageInVis() {
        getusage.setVisibility(View.INVISIBLE);
        usage.setVisibility(View.INVISIBLE);
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
        context = getActivity();
        usage = (TextView) v.findViewById(R.id.usage);
        appStartTv = (TextView) v.findViewById(R.id.appStartLoginCtv);
        appStartTv.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        usage.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        appStartLogin = (CheckBox) v.findViewById(R.id.appStartLoginCheckBox);
        appStartLogin.setChecked(TabActivity.sharedPrefs.getBoolValue(Config.loginOnStartUp));
        butn = (Button) v.findViewById(R.id.wifi_not_connected);
        getusage = (ButtonRectangle) v.findViewById(R.id.getusage);
        butn.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        getusage.getTextView().setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        getusage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Usage().execute(session);
            }
        });
        appStartLogin.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(CheckBox view, boolean check) {
                TabActivity.sharedPrefs.storeBoolValue(Config.loginOnStartUp, check);
            }
        });
        if (isWifiConnected()) {
            butn.setVisibility(View.INVISIBLE);
        } else {
            butn.setVisibility(View.VISIBLE);
        }
        context = getActivity();
        progressBar_vlog = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressDeterminate);
        try {
            progressBar_vlog.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        login_b = (ButtonRectangle) v.findViewById(R.id.login_b);
        logout_b = (ButtonRectangle) v.findViewById(R.id.logout_b);
        login_b.getTextView().setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        logout_b.getTextView().setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        login_b.setOnClickListener(this);
        logout_b.setOnClickListener(this);
        butn.setOnClickListener(this);
        boolean ver = TabActivity.sharedPrefs.getBoolValue(Config.loginOnStartUp);
        if (ver) {
            nextloginbaby();
        }
        return v;
    }

    public void logoutbaby() {
        login_b.setEnabled(false);
        logout_b.setEnabled(false);
        if (isWifiConnected()) {
            new LoginLogout().execute(LOGOUT_LINK, "logout_volsbb_7765", "logout_volsbb_7765");
            butn.setVisibility(View.GONE);
        } else {
            float tr = ViewCompat.getTranslationY(butn);
            butn.setVisibility(View.VISIBLE);
            ViewCompat.setTranslationY(butn, 2000);
            ViewCompat.animate(butn).translationY(tr).setDuration(500).setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {

                }

                @Override
                public void onAnimationEnd(View view) {
                    ViewCompat.setAlpha(butn, 1f);
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
            enableSpecifiedWifi("VIT2.4G", wifiManager);
            if (!isWifiConnected())
                enableSpecifiedWifi("VIT5G", wifiManager);
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
                        Toast.makeText(context, "Trying to log in", Toast.LENGTH_SHORT).show();
                        login_main(true);
                    } else {
                        Toast.makeText(context, "Not able To connect To Wifi, is it past 12:30 am ?, or try connecting manually from settings and then login from app.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar_vlog.setVisibility(View.INVISIBLE);

                }
            }.start();
        }

    }

    private void enableSpecifiedWifi(String name, WifiManager wifiManager) {
        try {
            Toast.makeText(context, "Enabling Wifi for " + name, Toast.LENGTH_SHORT).show();
            WifiConfiguration wc = new WifiConfiguration();
            String net = TabActivity.sharedPrefs.getValue(Config.networkNamePREF, name);
            net = "\"" + net + "\"";
            wc.SSID = net;
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
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
        Class iConnectivityManagerClass = null;
        Object iConnectivityManager = null;
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            iConnectivityManager = iConnectivityManagerField.get(conman);
            iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
            Class[] cArg = new Class[2];
            cArg[0] = String.class;
            cArg[1] = Boolean.TYPE;
            Method setMobileDataEnabledMethod;
            try {
                setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", cArg);
                Object[] pArg = new Object[2];
                pArg[0] = getContext().getPackageName();
                pArg[1] = true;
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, pArg);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }

    public void nextloginbaby() {
        username = TabActivity.sharedPrefs.getValue(Config.unamePREF, "null");
        pass = TabActivity.sharedPrefs.getValue(Config.passPREF, "null");
        datern_day = TabActivity.sharedPrefs.getIntValue(Config.dateReneWPREF, -1);
        login_b.setEnabled(false);
        logout_b.setEnabled(false);

        if (isWifiConnected()) {
            login_main(true);

        } else {
            float tr = ViewCompat.getTranslationY(butn);
            butn.setVisibility(View.VISIBLE);
            butn.setTranslationY(2000);
            ViewCompat.animate(butn);
            butn.animate().translationY(tr).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    butn.setAlpha(1f);

                }
            });
        }
        login_b.setEnabled(true);
        logout_b.setEnabled(true);

    }

    public void login_main(boolean ehat) {
        username = TabActivity.sharedPrefs.getValue(Config.unamePREF, "null");
        pass = TabActivity.sharedPrefs.getValue(Config.passPREF, "null");
        new LoginLogout().execute(LOGIN_LINK, username, pass);
        butn.setVisibility(View.GONE);
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
        }
    }


    private class LoginLogout extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            try {
                progressBar_vlog.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            String op = null;
            if (!params[1].equals("logout_volsbb_7765")) {
                try {
                    op = Http.post(params[0], params[1], params[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Config.NULL_CATCH;
                }
            } else {
                try {
                    op = Http.getData(LOGOUT_LINK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (op.contains("Successful Pronto Authentication")) {
                    final String finalOp = op;
                    Runnable runnable = new Runnable() {
                        public void run() {
                            if (finalOp.contains("<a href=\"http://115.248.50.60/registration")) {
                                session = getLink(finalOp);
                                Log.d("Got Main Link", session);
                            }
                        }
                    };
                    new Thread(runnable).start();
                    return Config.SUCCESS_LOGIN;
                } else if (op.contains(("free access quota is over"))) {
                    return Config.QOVER;
                } else if (op.contains("Sorry, please check your username and password ")) {
                    return Config.INVALID;
                } else if (op.contains("Sorry, that account does not exist")) {
                    return Config.NOTEXIST;
                } else if (op.contains("Logout successful")) {
                    return Config.SUCCESS_LOGOUT;
                } else if (op.contains("There is no active session to logout.")) {
                    return Config.ALREADY_LOGGED_OUT;
                } else if (op.contains("Sorry, that password was not accepted")) {
                    return Config.PASS_NOT_ACC;
                } else if (Http.getData("http://www.wikipedia.org/").contains("<title>Wikipedia</title>")) {
                    return Config.ALREADY_LOGGED_IN;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Config.NULL_CATCH;
        }


        private String getLink(String op) {
            Document document = Jsoup.parse(op);
            Elements elements = document.getElementsByClass("orangeText10");
            Element element1 = elements.get(1);
            return element1.attr("href");
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            String message = "";
            if (s != null) {
                progressBar_vlog.setVisibility(View.INVISIBLE);
                makeUsageInVis();
                try {
                    switch (s) {
                        case Config.SUCCESS_LOGIN:
                            message = "Logged In Successfully!";
                            makeUsageVis();
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.QOVER:
                            message = "Your free access quota is over.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.INVALID:
                            message = "Invalid Credentials";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.NOTEXIST:
                            message = "That account does not exist.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.SUCCESS_LOGOUT:
                            message = "Logout successful.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.ALREADY_LOGGED_OUT:
                            message = "You are already logged out.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.PASS_NOT_ACC:
                            message = "Sorry, that password was not accepted.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.ALREADY_LOGGED_IN:
                            message = "You are already logged in.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                        case Config.NULL_CATCH:
                            message = "Socket Timeout, Wifi is down or It is past 12:30am.";
                            (new SnackBar(getActivity(), message)).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            String sessionLink = "";
            try {
                try {
                    String details = Http.getData(session);
                    sessionLink = getUsage(details);
                    sessionLink = "http://115.248.50.60" + sessionLink;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String cus = null;
                try {
                    cus = postUsage(sessionLink);
                    if (cus == null) return null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return cus;
            } catch (Exception w) {
                w.printStackTrace();
            }
            return null;
        }

        private String getUsage(String details) {
            Document document = Jsoup.parse(details);
            Elements el = document.getElementsByTag("form");
            String link = el.get(0).attr("action");
            return link;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar_vlog.setVisibility(View.INVISIBLE);
            String element = "Error, Try again!";
            try {
                Elements elements = Jsoup.parse(s).getElementsByClass("subTextRight");
                Element elementw = elements.last();
                String sr = elementw.html().trim();
                Document document1 = Jsoup.parse(sr);
                element = document1.getElementsByTag("b").first().html();
            } catch (Exception e) {
                e.printStackTrace();
            }
            usage.setText(element);
        }
    }


    private String postUsage(String link) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH); // zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int renew_mon = month;
        int renew_y = year;
        if (datern_day > day) {
            if (month == 0) renew_mon = 11;
            else
                renew_mon = month - 1;

        }
        if (month == 0 && datern_day > day) {
            renew_y = year - 1;
        }
        String op = null;
        try {
            RequestBody formBody = new FormBody.Builder().
                    add("location", "allLocations").
                    add("parameter", "custom").
                    add("customStartMonth", renew_mon + "").
                    add("customStartDay", datern_day + "").
                    add("customStartYear", renew_y + "").
                    add("customEndMonth", month + "").
                    add("customEndDay", day + "").
                    add("customEndYear", year + "").build();

            OkHttpClient client = new OkHttpClient();
            client.retryOnConnectionFailure();
            Request request = new Request.Builder()
                    .url(HttpUrl.parse(link))
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            return op = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return op;
    }
}
