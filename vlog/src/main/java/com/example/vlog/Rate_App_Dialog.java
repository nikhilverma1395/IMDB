package com.example.vlog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class Rate_App_Dialog extends DialogFragment implements View.OnClickListener {
    private TextView body_rate;
    private Button nothanks, later, rate;

    public Rate_App_Dialog() {
    }

    public static Rate_App_Dialog newInstance(String title) {
        Rate_App_Dialog ra = new Rate_App_Dialog();
        Bundle bun = new Bundle();
        bun.putString("data.RATE", title);
        ra.setArguments(bun);
        return ra;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.rateapp, container, false);
        init(view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        rate.requestFocus();
        return view;
    }

    private void init(ViewGroup view) {
        body_rate = (TextView) view.findViewById(R.id.rate_body);
        nothanks = (Button) view.findViewById(R.id.no_thanks);
        later = (Button) view.findViewById(R.id.remind_later);
        rate = (Button) view.findViewById(R.id.rateit);
        nothanks.setOnClickListener(this);
        later.setOnClickListener(this);
        rate.setOnClickListener(this);

        String retr = "If You Enjoy Using Pronto Login, please take a moment to rate it. Thanks for Your support! ";
        body_rate.setText(retr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_thanks:
                getDialog().dismiss();
                break;
            case R.id.remind_later:
                getDialog().dismiss();
                prefs();
                break;

            case R.id.rateit:
                getDialog().dismiss();
                launchMarket();
                break;

        }
    }

    private void prefs() {
        getActivity().getSharedPreferences("PREFERENCE.IMDB.RATE", getActivity().MODE_PRIVATE)
                .edit()
                .putBoolean("remindlater", true)
                .commit();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent gotomarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(gotomarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "App Not Found Or Check Your Internet Connection .", Toast.LENGTH_LONG).show();
        }
    }
}
