package com.ldc.bananamuffin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BreakfastNotifier extends BroadcastReceiver {

    public static enum Action {
        SHOW, YES, NO
    }

    public BreakfastNotifier() {
        Log.i(BananaMuffin.TAG, "Breakfast Notifier created.");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.i(BananaMuffin.TAG, "Handling action: " + action + " for Alarm intent: " + intent);
        if (action != null) {
            switch (Action.valueOf(action)) {
                case SHOW:
                    show(context);
                    break;
                case YES:
                    yes();
                    break;
                case NO:
                    no();
                    break;
                default:
                    Log.d(BananaMuffin.TAG, "Action not supported " + action);
            }
        }
    }

    private void show(Context context) {
        Log.i(BananaMuffin.TAG, "Show notification");

        Intent yesIntent = new Intent(context, BreakfastNotifier.class);
        yesIntent.setAction(Action.YES.name());
        PendingIntent pendingYes = PendingIntent.getService(context, 0, yesIntent, 0);

        Intent noIntent = new Intent(context, BreakfastNotifier.class);
        noIntent.setAction(Action.NO.name());
        PendingIntent pendingNo = PendingIntent.getService(context, 0, noIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Tanto va la gatta al largo ...")
                .setContentText("... che ci lascia il Muffin-o")
                .addAction(0, context.getString(R.string.yes), pendingYes)
                .addAction(0, context.getString(R.string.no), pendingNo);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void yes() {
        // Todo
    }

    private void no() {
        // Todo
    }

}