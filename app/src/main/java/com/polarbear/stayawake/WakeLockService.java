package com.polarbear.stayawake;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by YingjieFan on 9/19/17.
 * This service will spin up a wake lock that keeps the device screen on.
 */

public class WakeLockService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private final String TAG = getClass().getSimpleName();
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service onCreate()");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "MyWakelockTag");

        initForeground();
        acquireWakeLock();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        Toast.makeText(this, "Service started!", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initForeground() {
        Log.d(TAG, "Starting foreground");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle(getText(R.string.notification_title))
                        .setContentText(getText(R.string.notification_message))
                        .setSmallIcon(R.drawable.lock)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.ticker_text))
                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        Log.d(TAG, "Foreground started");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        releaseWakelock();
        Toast.makeText(this, "Service stopped!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Release the wakelock if it's being held
     */
    private void releaseWakelock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    /**
     * Acquire the wake lock
     */
    private void acquireWakeLock() {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
