package com.ldc.bananamuffin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SendMessageActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Log.i(BananaMuffin.TAG, "SendMessageActivity created.");
    }
}
