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

    public BreakfastReminder(Context context) {
        Log.i(BananaMuffin.TAG, "Breakfast reminder creation");
        Log.i(BananaMuffin.TAG, "Get alert manager and create intent");
        // Get the alarm manager
        this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Get intent
        Log.i(BananaMuffin.TAG, "Create pending intent");
        Intent intent = new Intent(context, BreakfastNotifier.class);
        intent.setAction(BreakfastNotifier.Action.SHOW.name());
        this.intent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private final static int TEN_SECONDS = 10 * 1000;

    public void activateShortAlarm() {
        Log.i(BananaMuffin.TAG, "Activate Breakfast Short Reminder");
        // Set the alarm
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, inTenSeconds(), TEN_SECONDS, this.intent);
    }

    private long inTenSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTimeInMillis();
    }

    private final static int ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

    public void activateLongAlarm() {
        Log.i(BananaMuffin.TAG, "Activate Breakfast Long Reminder");
        // Set the alarm
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, nextTuesdayAtNine(), ONE_WEEK, this.intent);
    }

    private long nextTuesdayAtNine() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        if (calendar.getTimeInMillis() < now) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        return calendar.getTimeInMillis();
    }

    public void deactivate() {
        Log.i(BananaMuffin.TAG, "Deactivate Breakfast Reminder");
        manager.cancel(intent);
    }
}