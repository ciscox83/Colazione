package com.ldc.bananamuffin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;


public class BreakfastActivity extends Activity {

    private final static String ACTIVE_REMINDER = "activeReminder";

    private EditText textEdit;
    private boolean activeReminder = false;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Log.i(BananaMuffin.TAG, "Create boot activity");
        // Create view
        setContentView(R.layout.activity_boot);
        // Get status
        if (state != null) {
            activeReminder = state.getBoolean(ACTIVE_REMINDER, false);
        }
        // Start Reminder
        final BreakfastReminder reminder = new BreakfastReminder(this);
        Log.i(BananaMuffin.TAG, "Start notifier");
        Intent startReminder = new Intent(this, BreakfastReminder.class);
        startService(startReminder);
        // Set switch status and add listener
        Switch activator = (Switch) findViewById(R.id.activator);
        activator.setChecked(activeReminder);
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(BananaMuffin.TAG, "Switch on");
                    activeReminder = true;
                    reminder.activateShortAlarm();
                } else {
                    Log.i(BananaMuffin.TAG, "Switch off");
                    activeReminder = false;
                    reminder.deactivate();
                }
            }
        });
        // Notify breakfast directly
        Button notifier = (Button) findViewById(R.id.notifier);
        notifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(BananaMuffin.TAG, "Click the notifier");
                Intent show = new Intent(BreakfastActivity.this, BreakfastNotifier.class);
                show.setAction(BreakfastNotifier.Action.SHOW.toString());
                sendBroadcast(show);
            }
        });
        // Message area
        textEdit = getEditMessages();

        /*
         * TODO This is a bit of a magic, probably can be done better.
         */
        new HttpGetTask().execute();

        // Add Click Handler For Save Messages
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(BananaMuffin.TAG, "Click on save button");
                String text = textEdit.getText().toString();
                SharedPreferences sharedPref = getPreferences();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.custom_messages), text);
                editor.commit();
                Toast.makeText(BreakfastActivity.this, R.string.custom_messages_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EditText getEditMessages() {
        return (EditText) findViewById(R.id.messages);
    }

    private SharedPreferences getPreferences() {
        return getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boot, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean(ACTIVE_REMINDER, activeReminder);
    }

    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String URL = "http://banana-muffin.appspot.com/sentences";

        AndroidHttpClient client = AndroidHttpClient.newInstance("");

        @Override
        protected String doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {
                return client.execute(request, responseHandler);
            } catch (Exception e) {
                Log.e(BananaMuffin.TAG, "Problem getting sentences from the web service.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (client != null)
                client.close();
            textEdit.setText(result);
        }
    }
}
