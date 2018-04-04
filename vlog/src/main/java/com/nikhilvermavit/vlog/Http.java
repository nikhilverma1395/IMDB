/**
 * Created by Nikhil Verma on 16-12-2014.
 */

package com.nikhilvermavit.vlog;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Http {
    public static String getData(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String post(String url, String u, String p) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add(Config.unamePOST, u)
                .add(Config.passPOST, p)
                .add(Config.servicePOST, "ProntoAuthentication")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.retryOnConnectionFailure();
        Request request = new Request.Builder()
                .url(HttpUrl.parse(url))
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}