package com.nikhilvermavit.vlog.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Http;
import com.nikhilvermavit.vlog.SharedPrefs;

import java.util.HashMap;

/**
 * Created by Nikhil Verma on 2/21/2015.
 */
public class receiver extends BroadcastReceiver {
    private String u;
    private String p;

    @Override
    public void onReceive(final Context context, Intent intent) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String str = null;
                try {

                    str = Http.getData("http://www.wikipedia.org/");
                    if (str.contains("<title>Wikipedia</title>")) {
                    } else {
                        String op = login();
                        return "catch" + op;
                    }
                    return "connected";
                } catch (Exception e) {
                    return "exception";
                }

            }

            private String login() {
                String whattoret;
                u = new SharedPrefs(context).getValue(Config.unamePREF, "nuller");
                p = new SharedPrefs(context).getValue(Config.passPREF, "nuller");
                String op;
                try {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Config.unamePOST, u);
                    map.put(Config.passPOST, p);
                    op = Http.post(MainLogin.LOGIN_LINK, u, p);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    op = null;
                }
                if (op.contains("Successful Pronto Authentication")) {
                    whattoret = "true";
                } else if (op.contains("Sorry, please check your username and password ")) {
                    whattoret = "invalid";
                } else if (op.contains("Sorry, that account does not exist")) {
                    whattoret = "noexist";
                } else if (op.contains("access quota is over.")) {
                    whattoret = "qover";
                } else {
                    return "loggedinprev";
                }
                return whattoret;
            }


            @Override
            protected void onPostExecute(String s) {
                if (s.equals("exception")) {
                }
                if (s.equals("off"))
                    Toast.makeText(context, "Wifi Off", Toast.LENGTH_SHORT).show();
                if (s.contains("catch")) {
                    if (s.contains("true"))
                        Toast.makeText(context, "Logged In .", Toast.LENGTH_SHORT).show();
                    if (s.contains("invalid"))
                        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    if (s.contains("qover"))
                        Toast.makeText(context, "Free Access Quota Over", Toast.LENGTH_SHORT).show();
                    if (s.contains("loggedinprev"))
                        Toast.makeText(context, "Already Logged In .", Toast.LENGTH_SHORT).show();
                }
                if (s.equals("connected"))
                    Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();


            }
        }.execute();
    }
}
