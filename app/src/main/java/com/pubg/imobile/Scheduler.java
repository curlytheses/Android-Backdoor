package com.pubg.imobile;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;


public class Scheduler extends JobService {
    private static final String TAG ="jobSchedulerTest" ;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        doBackgroundWork(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        return true;
    }


    private void doBackgroundWork(final JobParameters params) {
        new Thread(() -> {
            new Jumper(getApplicationContext()).init();
            Log.d(TAG, "Job finished");
            jobFinished(params, false);
        }).start();
    }
}

