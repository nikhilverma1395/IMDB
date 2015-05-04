package com.nikhilvermavit.vlog.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nikhil Verma on 4/15/2015.
 */
public class StartBootService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) ||"android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction()) ) {
            try {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                context.registerReceiver(new LoginWhenConnected(), intentFilter);
                Toast.makeText(context, "On Boot", Toast.LENGTH_SHORT).show();
                Log.d(StartBootService.class.getSimpleName() + "\n\n\n\n\n\n\n\n", "On Boot");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
