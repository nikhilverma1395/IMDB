package com.nikhilvermavit.vlog.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Services.cancelrec;
import com.nikhilvermavit.vlog.Services.receiver;
import com.nikhilvermavit.vlog.TabActivity;

/**
 * Created by Nikhil Verma on 2/21/2015.
 */
public class schedule extends Fragment implements View.OnClickListener {
    private SeekBar slider1, slider2;
    private ButtonRectangle set, cancel, cancel_timeset;
    private int interval, cancelat;
    private TextView t1, t2, tt1, tt2, checkBoxTv;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private CheckBox checkBox;

    public schedule() {
    }

    public static schedule newInstance(String text) {

        schedule f = new schedule();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timers, container, false);
        init(view);
        slider1.setMax(50);
        slider2.setMax(50);
        slider1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                interval = progress;
                t1.setText(interval + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cancelat = progress;
                t2.setText(cancelat + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        set.setOnClickListener(this);
        cancel_timeset.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    private void init(View view) {
        slider1 = (SeekBar) view.findViewById(R.id.interval_slider);
        slider2 = (SeekBar) view.findViewById(R.id.cancelat_slider);
        t1 = (TextView) view.findViewById(R.id.t1);
        tt1 = (TextView) view.findViewById(R.id.tt1);
        tt2 = (TextView) view.findViewById(R.id.tt2);
        checkBox = (CheckBox) view.findViewById(R.id.appAutoLoginCheckBox);
        checkBoxTv = (TextView) view.findViewById(R.id.appAutoLoginCtv);
        t2 = (TextView) view.findViewById(R.id.t2);
        cancel_timeset = (ButtonRectangle) view.findViewById(R.id.cancel_timeset);
        set = (ButtonRectangle) view.findViewById(R.id.setalarm);
        cancel = (ButtonRectangle) view.findViewById(R.id.cancel);
        changeTypeFaceForAll();
        checkBox.setChecked(TabActivity.sharedPrefs.getBoolValue(Config.autoLogin, true));
        checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(CheckBox view, boolean check) {
                TabActivity.sharedPrefs.storeBoolValue(Config.autoLogin, check);
            }


        });
    }

    private void changeTypeFaceForAll() {
        Typeface typeface = TabActivity.getRaleway(Config.RALEWAY_REG);
        t1.setTypeface(typeface);
        tt1.setTypeface(typeface);
        tt2.setTypeface(typeface);
        t2.setTypeface(typeface);
        cancel_timeset.getTextView().setTypeface(typeface);
        set.getTextView().setTypeface(typeface);
        cancel_timeset.getTextView().setTypeface(typeface);
        checkBoxTv.setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));

    }

    public boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setalarm) {
            if (isWifiConnected()) {
                Intent myIntent = new Intent(getActivity(), receiver.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * interval, 1000 * 60 * interval, pendingIntent);
                Toast.makeText(getActivity(), "Auto-login timer set for " + interval + " minutes.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "First Turn On WiFi and Login .", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.cancel) {

            Intent myIntent = new Intent(getActivity(), receiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            try {
                Intent myIntent1 = new Intent(getActivity(), cancelrec.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent1, 0);
                AlarmManager alarmManager1 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager1.cancel(pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "Timer Cancelled .", Toast.LENGTH_SHORT).show();
        }

        if (R.id.cancel_timeset == v.getId()) {
            if (isWifiConnected()) {
                Intent myIntent = new Intent(getActivity(), cancelrec.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + cancelat * 1000 * 60, pendingIntent);
                Toast.makeText(getActivity(), "Auto-Login Timer will cancel after " + cancelat + " minutes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "First Turn On WiFi and Login.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
