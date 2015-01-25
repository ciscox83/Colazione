package com.ldc.bananamuffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent externalIntent) {
        Log.i(BananaMuffin.TAG, "BootReceiver onReceive()");
        if (externalIntent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i(BananaMuffin.TAG, "BootReceiver Action Boot Completed");
            BreakfastReminder reminder = new BreakfastReminder(context);
            reminder.activateLongAlarm();
        } else {
            Log.e(BananaMuffin.TAG, "Intent not supported: " + externalIntent.getAction());
        }
    }

}