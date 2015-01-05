package com.example.nikhilverma.imdb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarIndeterminate;

/**
 * Created by Nikhil Verma on 31-12-2014.
 */
public class web extends Fragment {
    static String url;
    private static ProgressBarIndeterminate pbi;

    public web() {

    }

    public web(String w) {
        url = w;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_image, container, false);
        final Activity activity = getActivity();
        Log.d("URL", url);
        final String EXTRA = "LX1920.jpg";
        String d = url.substring(0, url.length() - 9);
        Log.d("URL", d);
        WebView wv = (WebView) v.findViewById(R.id.webview);
        pbi = (ProgressBarIndeterminate) v.findViewById(R.id.pbi);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().getJavaScriptEnabled();
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbi.setVisibility(ViewGroup.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbi.setVisibility(ViewGroup.INVISIBLE);
                super.onPageFinished(view, url);
            }
        });

        //  getActivity().
        wv.getSettings().setJavaScriptEnabled(true);
        if (url.contains(".jpg")) {
            wv.loadUrl(d + EXTRA);
        } else {
            wv.loadUrl(url);
        }
        return v;
    }
}
