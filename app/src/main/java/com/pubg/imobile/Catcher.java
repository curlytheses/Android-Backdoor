package com.pubg.imobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Catcher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent inten = new Intent(context, MainActivity.class);
            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(inten);
        }
    }
}
