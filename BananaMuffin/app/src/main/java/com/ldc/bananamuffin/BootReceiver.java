package com.ldc.bananamuffin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    private final static int ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private final static int ONE_MINUTE = 60 * 1000;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent externalIntent) {
        Log.i(BananaMuffin.TAG, "BootReceiver onReceive()");
        if (isTheRightIntent(externalIntent)) {
            Log.i(BananaMuffin.TAG, "BootReceiver Action Boot Completed");
            // Get the alarm manager
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // Get intent
            Intent intent = new Intent(context, BreakfastNotifier.class);
            intent.setAction(BreakfastNotifier.Action.SHOW.name());
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            // Create the time
            long firstNotification = inFiveMinutes();
            // Set the alarm
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstNotification, ONE_MINUTE, alarmIntent);
        }
    }

    private boolean isTheRightIntent(Intent intent) {
        String action = intent.getAction();
        return action.equals(Intent.ACTION_BOOT_COMPLETED) ||
            action.equals(Intent.ACTION_PACKAGE_ADDED);
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

    private long inFiveMinutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);
        return calendar.getTimeInMillis();
    }
}
