package com.pubg.imobile;

import android.app.Application;
import android.content.SharedPreferences;
public class ParentalApplication extends Application {

    public static ParentalApplication singleton;
    public static String packageName;

    public static final String TAG = "com.pubg,imobile.parentalcontrol.";
    public static final String STOP_TIMER = TAG + "STOP_TIMER";
    public static final String LOCK = TAG + "LOCK";

    public static ParentalApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        packageName = getPackageName();

    }

    public SharedPreferences getSharedPreferences() {
        return this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
    }

}