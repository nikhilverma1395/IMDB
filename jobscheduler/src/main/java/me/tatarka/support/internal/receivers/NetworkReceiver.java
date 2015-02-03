package me.tatarka.support.internal.receivers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.net.ConnectivityManagerCompat;

import me.tatarka.support.internal.util.ArraySet;
import me.tatarka.support.internal.job.JobServiceCompat;
import me.tatarka.support.internal.job.JobStore;

/**
 * @hide
 */
public class NetworkReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // Connectivity manager for THIS context - important!
            final ConnectivityManager connManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

            if (ensureActiveNetwork(intent, activeNetwork)) {
                boolean networkUnmetered = false;
                boolean networkConnected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (networkConnected) { // No point making the call if we know there's no conn.
                    networkUnmetered = !ConnectivityManagerCompat.isActiveNetworkMetered(connManager);
                }
                updateTrackedJobs(context, networkConnected, networkUnmetered);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean ensureActiveNetwork(Intent intent, NetworkInfo networkInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);
            return networkInfo != null && networkInfo.getType() == networkType;
        } else {
            return networkInfo != null;
        }
    }

    public static void setNetworkForJob(Context context, JobStatus job) {
        if (job.hasConnectivityConstraint() || job.hasUnmeteredConstraint()) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean networkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            boolean networkUnmetered = !ConnectivityManagerCompat.isActiveNetworkMetered(cm);
            job.connectivityConstraintSatisfied.set(networkConnected);
            job.unmeteredConstraintSatisfied.set(networkUnmetered);
        }
    }

    private static void updateTrackedJobs(Context context, boolean networkConnected, boolean networkUnmetered) {
        final JobStore jobStore = JobStore.initAndGet(context);
        synchronized (jobStore) {
            boolean changed = false;
            ArraySet<JobStatus> jobs = jobStore.getJobs();
            for (int i = 0; i < jobs.size(); i++) {
                JobStatus ts = jobs.valueAt(i);
                boolean prevIsConnected = ts.connectivityConstraintSatisfied.getAndSet(networkConnected);
                boolean prevIsMetered = ts.unmeteredConstraintSatisfied.getAndSet(networkUnmetered);
                if (prevIsConnected != networkConnected || prevIsMetered != networkUnmetered) {
                    changed = true;
                }
            }

            if (changed) {
                startWakefulService(context, JobServiceCompat.maybeRunJobs(context));
            }
        }
    }
}
