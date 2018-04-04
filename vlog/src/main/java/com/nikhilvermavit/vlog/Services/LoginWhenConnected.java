package com.nikhilvermavit.vlog.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Http;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.SharedPrefs;
import com.nikhilvermavit.vlog.TabActivity;

/**
 * Created by Nikhil Verma on 4/15/2015.
 */
public class LoginWhenConnected extends BroadcastReceiver {
    private Context context;
    private String networkName = " ";
    long in, out;

    @Override
    public void onReceive(final Context context, Intent intent) {
        networkName = new SharedPrefs(context).getValue(Config.networkNamePREF, "VIT2.4G").trim();
        if (!TabActivity.isActivityVisible()) {
            if (new SharedPrefs(context).getBoolValue(Config.autoLogin, true)) {
                try {
                    this.context = context;
                    if (checkConnection(intent)) {
                        doAsync(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean checkConnection(Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if ((wifiInfo.getSSID().toLowerCase().contains("volsbb") || wifiInfo.getSSID().toLowerCase().contains("\"VOLS\"") || wifiInfo.getSSID().toLowerCase().contains("vit2.4g") || wifiInfo.getSSID().toLowerCase().contains("vit5g") || wifiInfo.getSSID().toLowerCase().contains(networkName)) && netInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void doAsync(final Context context) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    return login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Config.NULL_CATCH;
            }

            private int login() {
                String u, p;
                u = new SharedPrefs(context).getValue(Config.unamePREF, "<null>");
                p = new SharedPrefs(context).getValue(Config.passPREF, "<null>");
                String op;
                try {
                    op = Http.post(MainLogin.LOGIN_LINK, u, p);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Config.NULL_CATCH;
                }
                if (op.contains("Successful Pronto Authentication")) {
                    return Config.SUCCESS_LOGIN;
                } else if (op.contains("check your username and password") || op.contains("Sorry, that password was not accepted")) {
                    return Config.INVALID;
                } else if (op.contains("that account does not exist")) {
                    return Config.NOTEXIST;
                } else if (op.contains("access quota is over.")) {
                    return Config.QOVER;
                } else {
                    return Config.ALREADY_LOGGED_IN;
                }
            }

            @Override
            protected void onPostExecute(Integer resp) {
                if (resp == Config.NULL_CATCH) {
                    return;
                }
                if (resp == Config.SUCCESS_LOGIN) {
                    notifyUser("Logged In.");
                }
                if (resp == Config.INVALID) {
                    notifyUser("Invalid Credentials for Auto-Login.");
                }
                if (resp == Config.QOVER) {
                    notifyUser("Free Access Quota Over.");
                }
                if (resp == Config.NOTEXIST) {
                    notifyUser(" That account does not exist.");
                }
            }
        }.execute();
    }

    private void notifyUser(String what) {
        if (what == null || what.equals(""))
            return;
        Notification notification = new NotificationCompat.Builder(context)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("Vit-Wifi Login")
                .setContentText(what)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.wifinot)
                .setAutoCancel(true).build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(12, notification);
    }
}



