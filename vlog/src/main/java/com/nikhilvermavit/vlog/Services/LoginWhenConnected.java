package com.nikhilvermavit.vlog.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Http;
import com.nikhilvermavit.vlog.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 4/15/2015.
 */
public class LoginWhenConnected extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            this.context = context;
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (info.isConnected()) {
                    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = manager.getConnectionInfo();
                    String SSID = wifiInfo.getSSID().toString();
                    Log.d("\n\n\n\n\n\n\n\nConected To\n\n\n\n", SSID);
                    if (SSID.toString().contains("VOLSBB")) {//equals() is  not working , which is fucking Annoying
                        try {
                            doAsync(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doAsync(final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String str = null;
                try {
                    str = Http.getData("http://www.wikipedia.org/");
                    if (!str.contains("<title>Wikipedia</title>")) {
                        Log.d("\n\n\n\n\n\n\n\n To volSBB\n\n\n\n", "doing magic");
                        String op = login();
                        return "catch" + op;
                    } else {
                        return "null";
                    }
                } catch (Exception e) {
                    String op = login();
                    return "catch" + op;
                }

            }

            private String login() {
                String whattoret, u, p;
                u = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", "Lucky007");
                p = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", "sdjsdksdhsidvj");
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
                HttpConnectionParams.setSoTimeout(httpParameters, 15000);
                HttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(MainLogin.url_);
                httpPost.setHeader("User-Agent", "MySuperUserAgent");
                List<NameValuePair> nameValuePair = new ArrayList<>(2);
                nameValuePair.add(new BasicNameValuePair("userId", u));
                nameValuePair.add(new BasicNameValuePair("password", p));
                nameValuePair.add(new BasicNameValuePair("serviceName", "ProntoAuthentication"));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("\n\n\n\n\n\n\n\n To volSBB\n\n\n\n", "executed");
                    HttpResponse response = httpClient.execute(httpPost);
                    String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Log.d("Response:", op);
                    if (op.contains("Successful Pronto Authentication")) {
                        Log.d("Http Post Response:", "success");
                        Log.d("\n\n\n\n\n\n\n\n To volSBB\n\n\n\n", "success");
                        whattoret = "true";
                    } else if (op.contains("check your username and password")||op.contains("Sorry, that password was not accepted")) {
                        Log.d("Http Post Response:", "over");
                        whattoret = "invalid";
                    } else if (op.contains("that account does not exist")) {
                        Log.d("Http Post Response:", "Invalid Cred.");
                        whattoret = "noexist";
                    } else if (op.contains("access quota is over.")) {
                        Log.d("Http Post Response:", "Quota Over");
                        whattoret = "qover";
                    } else {
                        return "loggedinprev";
                    }
                    return whattoret;
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("exception")) {
                    Log.d("Error", "error");
                }
                if (s.equals("off"))
                    Toast.makeText(context, "Wifi Off", Toast.LENGTH_SHORT).show();
                if (s.contains("catch")) {
                    if (s.contains("true")) {
                        notifyUser("Logged In .");
                    }
                    if (s.contains("invalid")) {
                        notifyUser("Invalid Credentials for Auto-Login .");
                    }
                    if (s.contains("qover")) {
                        notifyUser("Free Access Quota Over.");
                    }
                    if (s.contains("noexist")) {
                        notifyUser(" That account does not exist.");
                    }
                }
            }
        }.execute();
    }

    private void notifyUser(String what) {
        Notification notification = new NotificationCompat.Builder(context)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("VOLSBB Login")
                .setContentText(what)
                .setSmallIcon(R.drawable.myicon_n)
                .setAutoCancel(true).build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(010, notification);

    }
}



