package com.nikhilvermavit.vlog.Dialog;

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

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.TabActivity;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class RateAPP extends DialogFragment implements View.OnClickListener {
    private TextView body_rate, rateapphead;
    private Button nothanks, later, rate;

    public RateAPP() {

    }

    public static RateAPP newInstance(String title) {
        RateAPP ra = new RateAPP();
        Bundle bun = new Bundle();
        bun.putString(Config.ratePREF, title);
        ra.setArguments(bun);
        return ra;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.rateanim;
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
        rateapphead = (TextView) view.findViewById(R.id.rate_app_head);
        nothanks = (Button) view.findViewById(R.id.no_thanks);
        later = (Button) view.findViewById(R.id.remind_later);
        rate = (Button) view.findViewById(R.id.rateit);
        nothanks.setOnClickListener(this);
        later.setOnClickListener(this);
        rate.setOnClickListener(this);
        body_rate.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        rateapphead.setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        nothanks.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        later.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        rate.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        String retr = "If You Enjoy Using Volsbb Login, please take a moment to rate it. Thanks for Your support!";
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
                int pr = TabActivity.sharedPrefs.getIntValue(Config.disable_ratePREF, -1);
                TabActivity.sharedPrefs.storeIntValue(Config.disable_ratePREF, pr + 1);
                launchMarket();
                break;

        }
    }

    private void prefs() {
        getActivity().getSharedPreferences(Config.prefNAME, getActivity().MODE_PRIVATE)
                .edit()
                .putBoolean(Config.remindLaterPREF, true)
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
