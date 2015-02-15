package com.ldc.bananamuffin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

public class BreakfastNotifier extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String TYPE_TEXT_PLAIN = "text/plain";
    private static final int TITLE = 0;
    private static final int BODY = 1;

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

    private void showChoiceNotification(Context context, String[] messageParts,
                                        PendingIntent pendingYes, PendingIntent pendingNo) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(messageParts[TITLE] + " ...")
                .setContentText("..." + messageParts[BODY])
                .addAction(0, context.getString(R.string.yes), pendingYes)
                .addAction(0, context.getString(R.string.no), pendingNo);

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String getRandomMessage(Context context) {
        String[] messages = context.getResources().getStringArray(R.array.messages);
        Random random = new Random();
        return messages[random.nextInt(messages.length)];
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