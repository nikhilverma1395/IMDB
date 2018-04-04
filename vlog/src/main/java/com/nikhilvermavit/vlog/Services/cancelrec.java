package com.nikhilvermavit.vlog.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Nikhil Verma on 2/21/2015.
 */
public class cancelrec extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            Intent myIntent = new Intent(context, receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            try {
                Intent myIntent1 = new Intent(context, cancelrec.class);
                pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent1, 0);
                AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager1.cancel(pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

