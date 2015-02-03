package com.example.vlog;

/**
 * Created by Barry Allen on 2/1/2015.
 */

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method.
 */
public class TestJobService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MainActivity().login_main();
                jobFinished(params, true);
            }
        }).start();
        // Start your job in a seperate thread, calling jobFinished(params, needsRescheudle) when you are done.
        // See the javadoc for more detail.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}