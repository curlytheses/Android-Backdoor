package com.pubg.imobile;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ParentalService extends Service {

    private final String TAG = "ParentalService";

    private ActivityManager activityManager = null;

    private static final ScheduledExecutorService worker = Executors
            .newSingleThreadScheduledExecutor();

    private SharedPreferences sPref = ParentalApplication
            .getInstance()
            .getSharedPreferences();

    private String[] apps_to_lock;

    private boolean lockAgain   = true;

    private String excludeApp   = null;

    private final BroadcastReceiver stopTimerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String in = intent.getAction();

            if (in.equals(ParentalApplication.STOP_TIMER)) {

                lockAgain   = true;
                excludeApp  = null;
            }
            else if (in.equals(ParentalApplication.LOCK)) {

                lockAgain   = false;
                excludeApp  = intent.getStringExtra("exclude");

                Log.d(TAG, "dari LOCK => " + excludeApp);
            }
        }
    };

    public ParentalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate");

        super.onCreate();

        IntentFilter inFilter = new IntentFilter();
        inFilter.addAction(ParentalApplication.STOP_TIMER);
        inFilter.addAction(ParentalApplication.LOCK);
        registerReceiver(stopTimerReceiver, inFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "Started");

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                apps_to_lock = sPref.getString("apps_to_lock", "").split(";");

                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

                @SuppressWarnings("deprecation")
                String runApp = am.getRunningTasks(1).get(0).topActivity.getPackageName().toString();

                Log.d(TAG, "BEGIN ------------------------");

                Log.d(TAG, "NOW app => " + runApp);

                Log.d(TAG, "dari scheduler => "+ runApp + " - "  + excludeApp + " - " + lockAgain);

                if (!runApp.equals(excludeApp)) {
                    lockAgain = true;
                }

                for (String lockApp : apps_to_lock) {

                    if (runApp.equals(lockApp) && lockAgain == true && !runApp.equals(excludeApp)) {

                        Log.d(TAG, runApp + " - LOCKED");

                        sPref.edit().putString("exclude", runApp).commit();

                        //screenlock
                        Intent intent = new Intent(ParentalService.this, LockScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {

                        Log.d(TAG, "NOT LOCKED");

                    }

                }

                Log.d(TAG, "LOCK AGAIN ? -> " +lockAgain);

                Log.d(TAG, "END   ------------------------");

            }
        }, 1000, 1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "Destroyed");

        super.onDestroy();

        unregisterReceiver(stopTimerReceiver);
    }
}