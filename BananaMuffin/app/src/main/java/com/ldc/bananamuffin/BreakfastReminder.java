package com.ldc.bananamuffin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BreakfastReminder {

    private final static int TEN_SECONDS = 10 * 1000;

    private final AlarmManager manager;
    private final PendingIntent intent;

    public BreakfastReminder(Context context) {
        // Get the alarm manager
        this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Get intent
        Intent intent = new Intent(context, BreakfastNotifier.class);
        context.startService(intent);
        intent.setAction(BreakfastNotifier.Action.SHOW.name());
        this.intent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void activate() {
        Log.i(BananaMuffin.TAG, "Activate Breakfast Reminder");
        // Create the time
        long firstNotification = inTenSeconds();
        // Set the alarm
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstNotification, TEN_SECONDS, intent);
    }

    private long inTenSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTimeInMillis();
    }

    public void deactivate() {
        Log.i(BananaMuffin.TAG, "Deactivate Breakfast Reminder");
        manager.cancel(intent);
    }

}
