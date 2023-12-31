package com.pubg.imobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        assert action != null;
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)){
            context.startService(new Intent(context, ParentalService.class));
        }
        else if(action.equals(Intent.ACTION_SHUTDOWN)) {
            context.startService(new Intent(context, ParentalService.class));
        }
    }
}