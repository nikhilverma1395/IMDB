package com.nikhilvermavit.vlog.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Fragment.MainLogin;
import com.nikhilvermavit.vlog.Http;

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
 * Created by Nikhil Verma on 2/21/2015.
 */
public class receiver extends BroadcastReceiver {
    private String u;
    private String p;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("runing", "running");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String str = null;
                try {

                    str = Http.getData("http://www.wikipedia.org/");
                    Log.d("data", str);
                    if (str.contains("<title>Wikipedia</title>")) {
                        Log.d("already", "al");
                    } else {
                        String op = login();
                        return "catch" + op;
                    }
                    Log.d("her", "1");
                    return "connected";
                } catch (Exception e) {
                    Log.d("catch", e.toString());

                    return "exception";
                }

            }

            private String login() {
                String whattoret;
                u = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", "nuller");
                p = context.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", "nuller");
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
                    HttpResponse response = httpClient.execute(httpPost);
                    String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Log.d("Response:", op);
                    if (op.contains("Successful Pronto Authentication")) {
                        Log.d("Http Post Response:", "success");
                        whattoret = "true";
                    } else if (op.contains("Sorry, please check your username and password ")) {
                        Log.d("Http Post Response:", "over");
                        whattoret = "invalid";
                    } else if (op.contains("Sorry, that account does not exist")) {
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
