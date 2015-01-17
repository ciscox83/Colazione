package com.ldc.bananamuffin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class BreakfastActivity extends Activity {

    private final static String ACTIVE_REMINDER = "activeReminder";

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
        // Start Notifier
        Log.i(BananaMuffin.TAG, "Start notifier");
        Intent startNotifier = new Intent(this, BreakfastNotifier.class);
        startService(startNotifier);
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
                    reminder.activate();
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
                startService(show);
            }
        });
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
}
