package com.nikhilvermavit.vlog.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Services.cancelrec;
import com.nikhilvermavit.vlog.Services.receiver;

/**
 * Created by Nikhil Verma on 2/21/2015.
 */
public class schedule extends Fragment implements View.OnClickListener {
    SeekBar slider1, slider2;
    ButtonRectangle set, cancel, cancel_timeset;
    int interval, cancelat;
    TextView t1, t2;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

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
        t2 = (TextView) view.findViewById(R.id.t2);
        cancel_timeset = (ButtonRectangle) view.findViewById(R.id.cancel_timeset);
        set = (ButtonRectangle) view.findViewById(R.id.setalarm);
        cancel = (ButtonRectangle) view.findViewById(R.id.cancel);
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
                Log.d("set", interval + "");
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
                Log.d("cancel", e.toString());
            }
            Toast.makeText(getActivity(), "Timer Cancelled .", Toast.LENGTH_SHORT).show();
        }

        if (R.id.cancel_timeset == v.getId()) {
            if (isWifiConnected()) {
                Intent myIntent = new Intent(getActivity(), cancelrec.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + cancelat * 1000 * 60, pendingIntent);
                Log.d("cancel", cancelat + "cancel timeout");
                Toast.makeText(getActivity(), "Auto-Login Timer will cancel after "+cancelat+" minutes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "First Turn On WiFi and Login.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
