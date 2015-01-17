package com.ldc.bananamuffin;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BreakfastNotifier extends IntentService {

    public final static String NAME = BreakfastNotifier.class.getSimpleName();

    public static enum Action {
        SHOW, YES, NO
    }

    public BreakfastNotifier() {
        super(NAME);
        Log.i(BananaMuffin.TAG, "Breakfast Notifier created.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        Log.i(BananaMuffin.TAG, "Handling action: " + action + " for Alarm intent: " + intent);
        if (action != null) {
            switch (Action.valueOf(action)) {
                case SHOW:
                    show();
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

    private void show() {
        Log.i(BananaMuffin.TAG, "Show notification");

        Intent yesIntent = new Intent(this, BreakfastNotifier.class);
        yesIntent.setAction(Action.YES.name());
        PendingIntent pendingYes = PendingIntent.getService(this, 0, yesIntent, 0);

        Intent noIntent = new Intent(this, BreakfastNotifier.class);
        noIntent.setAction(Action.NO.name());
        PendingIntent pendingNo = PendingIntent.getService(this, 0, noIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Colazione?")
                .setContentText(getText())
                .addAction(0, getString(R.string.yes), pendingYes)
                .addAction(0, getString(R.string.no), pendingNo);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void yes() {
        // Todo
    }

    private void no() {
        // Todo
    }

    private String getText() {
        return list[0];
    }

    private String[] list = new String[]{
        "Tanto va la gatta al largo che ci lascia il Muffin-o"
    };

}