package com.pubg.imobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Catcher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent newintent = new Intent(context, MainActivity.class);
            newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newintent);
        }
    }
}
