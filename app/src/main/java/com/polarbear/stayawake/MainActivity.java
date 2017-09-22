package com.polarbear.stayawake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    ToggleButton activateButton;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (activateButton != null) {
            writeBolToSharedPref(getString(R.string.saved_button_state), activateButton.isChecked());
        }
    }

    /**
     * Read a boolean from shared preference
     *
     * @param str        The key of the  value to retrive
     * @param defaultVal Default value if pref doesn't exist
     * @return The boolean value returned from sharedPref
     */
    private boolean readSharedPrefBol(String str, boolean defaultVal) {
        return sharedPref.getBoolean(str, defaultVal);
    }


    /**
     * Write a boolean value to shared pref
     *
     * @param key   The key for the value to write
     * @param value The value to store
     */
    private void writeBolToSharedPref(String key, boolean value) {
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isChecked = readSharedPrefBol(getString(R.string.saved_button_state), false);
        if (activateButton != null) {
            activateButton.setChecked(isChecked);
        }
    }

    /**
     * Do the view initialization
     */
    private void initView() {
        activateButton = (ToggleButton) findViewById(R.id.enable_btn);
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Toggle Button onClicked()");
                if (activateButton.isChecked()) {
                    initService();
                } else {
                    killService();
                }
            }
        });
    }

    /**
     * Initiate the wake lock service
     */
    private void initService() {
        Intent serviceIntent = new Intent(this, WakeLockService.class);
        startService(serviceIntent);
    }

    /**
     * Kill the wakelock service
     */
    private void killService() {
        Intent serviceIntent = new Intent(this, WakeLockService.class);
        stopService(serviceIntent);
    }


}
