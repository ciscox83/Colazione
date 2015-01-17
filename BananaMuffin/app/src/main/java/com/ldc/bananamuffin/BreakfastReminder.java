package com.ldc.bananamuffin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BreakfastReminder {

    private final AlarmManager manager;
    private final PendingIntent intent;

    private final static int TEN_SECONDS = 10 * 1000;

    public BreakfastReminder(Context context) {
        Log.i(BananaMuffin.TAG, "Breakfast reminder creation");
        Log.i(BananaMuffin.TAG, "Get alert manager and create intent");
        // Get the alarm manager
        this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Start the service
        Intent startNotifier = new Intent(context, BreakfastNotifier.class);
        context.startService(startNotifier);
        // Get intent
        Intent intent = new Intent(context, BreakfastNotifier.class);
        intent.setAction(BreakfastNotifier.Action.SHOW.name());
        this.intent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.i(BananaMuffin.TAG, "Activate Breakfast Reminder");
    }

    public void activate() {
        Log.i(BananaMuffin.TAG, "Activate Breakfast Reminder");
        // Create the time
        long firstNotification = inTenSeconds();
        // Set the alarm
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstNotification, TEN_SECONDS, this.intent);
    }

    private long inTenSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTimeInMillis();
    }

    public void deactivate() {
        Log.i(BananaMuffin.TAG, "Deactivate Breakfast Reminder");
        // The manager is not saved because the service is creates new all the time
        // manager.cancel(intent);
    }
}
