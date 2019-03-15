package io.cordova.test2_6720b;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.gcm.GcmListenerService;

import static android.content.ContentValues.TAG;

public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");



        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
/*
        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();


        final NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, VideoActivity.class);
        notificationIntent.putExtra("NotificationMessage", "test");
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // устанавливаем объект PendingIntent, большую иконку, заголовки и контент


        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v7.app.NotificationCompat.Builder(this)
                        .setSmallIcon(io.cordova.bizone2.R.drawable.mess)
                        // .setLargeIcon(bitmap)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Тима")
                        .setTicker("Новое сообщение от " + "Тима")
                        .setContentText(toString().valueOf(message))
                        .setNumber(1)
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true);

        mNotificationManager.notify("App Name", 228, mBuilder.build());

        */

       // Toast.makeText(this, toString().valueOf(message), Toast.LENGTH_SHORT).show();

    }
}

