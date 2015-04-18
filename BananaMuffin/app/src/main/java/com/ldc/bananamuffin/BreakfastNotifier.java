package com.ldc.bananamuffin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

public class BreakfastNotifier extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String TYPE_TEXT_PLAIN = "text/plain";
    private static final int TITLE = 0;
    private static final int BODY = 1;

    public enum Action {
        SHOW, NO
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
                case NO:
                    no(context);
                    break;
                default:
                    Log.e(BananaMuffin.TAG, "Action not supported " + action);
            }
        }
    }

    private void show(Context context) {
        Log.i(BananaMuffin.TAG, "Show notification");

        String message = getRandomMessage(context);
        String[] messageParts = message.split(",");

        Intent sendMessage = buildIntendToSendWhatsappMessage(message);
        PendingIntent pendingYes = PendingIntent.getActivity(context, 0, sendMessage,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancel = buildIntentToCloseNotification(context);
        PendingIntent pendingNo = PendingIntent.getBroadcast(context, 0, cancel, 0);

        showChoiceNotification(context, messageParts, pendingYes, pendingNo);
    }

    private Intent buildIntentToCloseNotification(Context context) {
        Intent cancel = new Intent(context, BreakfastNotifier.class);
        cancel.setAction(Action.NO.name());
        return cancel;
    }

    private Intent buildIntendToSendWhatsappMessage(String message) {
        Intent sendMessage = new Intent();
        sendMessage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendMessage.setAction(Intent.ACTION_SEND); // TODO Use SENDTO instead?
        sendMessage.putExtra(Intent.EXTRA_TEXT, message);
        sendMessage.setType(TYPE_TEXT_PLAIN);
        sendMessage.setPackage(WHATSAPP_PACKAGE_NAME); // TODO Check if Whatsapp exists
        return sendMessage;
    }

    private final static long[] VIBRATION_MMM_M_M = new long[]{300, 300, 200, 200, 200, 200};

    private void showChoiceNotification(Context context, String[] messageParts,
                                        PendingIntent pendingYes, PendingIntent pendingNo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(messageParts[TITLE] + " ...")
                .setContentText("..." + messageParts[BODY])
                .setLights(Color.WHITE, 300, 300)
                .setVibrate(VIBRATION_MMM_M_M)
                .setStyle(getBigMessage(context, messageParts))
                .setTicker(messageParts[TITLE] + messageParts[BODY])
                .addAction(0, context.getString(R.string.yes), pendingYes)
                .addAction(0, context.getString(R.string.no), pendingNo);

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private NotificationCompat.InboxStyle getBigMessage(Context context, String[] messageParts) {
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[] {messageParts[TITLE], messageParts[BODY]};
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_title));
        for (String event : events) {
            inboxStyle.addLine(event);
        }
        return inboxStyle;
    }

    private String getRandomMessage(Context context) {
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

    private void no(Context context) {
        Log.i(BananaMuffin.TAG, "Press No");
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}