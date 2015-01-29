package com.ldc.bananamuffin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BreakfastNotifier extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    public static final String PACKAGE_NAME = "com.whatsapp";
    public static final String WHATSAPP_PACKAGE_NAME = PACKAGE_NAME;
    public static final String TYPE_TEXT_PLAIN = "text/plain";

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
                    yes(context, "Yes");
                    break;
                case NO:
                    no(context);
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

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void yes(Context context, String message) {
        Log.i(BananaMuffin.TAG, "Press Yes");
        Intent send = new Intent();
        send.setAction(Intent.ACTION_SEND);
        send.putExtra(Intent.EXTRA_TEXT, message);
        send.setType(TYPE_TEXT_PLAIN);
        send.setPackage(WHATSAPP_PACKAGE_NAME); // TODO Check if Whatsapp exists
        context.startActivity(send);
    }

    private void no(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(BananaMuffin.TAG, "Press No");
        notificationManager.cancel(NOTIFICATION_ID);
    }

}