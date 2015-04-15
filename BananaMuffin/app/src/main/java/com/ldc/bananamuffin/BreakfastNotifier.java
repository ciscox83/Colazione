package com.ldc.bananamuffin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

public class BreakfastNotifier extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    public static final String PACKAGE_NAME = "com.whatsapp";
    public static final String WHATSAPP_PACKAGE_NAME = PACKAGE_NAME;
    public static final String TYPE_TEXT_PLAIN = "text/plain";

    public enum Action {
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
                    yes(context, intent.getStringExtra(MESSAGE));
                    break;
                case NO:
                    no(context);
                    break;
                default:
                    Log.d(BananaMuffin.TAG, "Action not supported " + action);
            }
        }
    }

    public final static String MESSAGE = "message";

    private void show(Context context) {
        Log.i(BananaMuffin.TAG, "Show notification");

        String message = getMessage(context);

        Intent yesIntent = new Intent(context, BreakfastNotifier.class);
        yesIntent.setAction(Action.YES.name());
        yesIntent.putExtra(MESSAGE, message);
        PendingIntent pendingYes = PendingIntent.getBroadcast(context, 0, yesIntent, 0);

        Intent noIntent = new Intent(context, BreakfastNotifier.class);
        noIntent.setAction(Action.NO.name());
        PendingIntent pendingNo = PendingIntent.getBroadcast(context, 0, noIntent, 0);

        String[] split = message.split(",");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(split[0] + " ...")
                .setContentText("..." + split[1])
                .addAction(0, context.getString(R.string.yes), pendingYes)
                .addAction(0, context.getString(R.string.no), pendingNo);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String getMessage(Context context) {
        SharedPreferences preferences = getPreferences(context);
        String customMessages = preferences.getString(context.getString(R.string.custom_messages), "");
        if (customMessages.isEmpty()) {
            String[] messages = context.getResources().getStringArray(R.array.messages);
            Random random = new Random();
            return messages[random.nextInt(messages.length)];
        } else {
            String[] messages = customMessages.split("\n");
            Random random = new Random();
            return messages[random.nextInt(messages.length)];
        }
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE);
    }

    private void yes(Context context, String message) {
        Log.i(BananaMuffin.TAG, "Press Yes");
        // Close notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        // Create an intent to send a message via Whatsapp
        Intent send = new Intent();
        send.setAction(Intent.ACTION_SEND);
        send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        send.putExtra(Intent.EXTRA_TEXT, message);
        send.setType(TYPE_TEXT_PLAIN);
        send.setPackage(WHATSAPP_PACKAGE_NAME); // TODO Check if Whatsapp exists
        context.startActivity(send);
    }

    private void no(Context context) {
        Log.i(BananaMuffin.TAG, "Press No");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

}