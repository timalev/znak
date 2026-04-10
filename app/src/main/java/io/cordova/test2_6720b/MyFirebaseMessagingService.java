package io.cordova.test2_6720b;


import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private View mView;

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // 🔥 Этот лог появится ТОЛЬКО если сервис вообще запустился
        Log.i("FCM_SERVICE", "✅ MyFirebaseMessagingService создан!");
    }


    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        if (isAppInForeground()) {
            Log.i("FCM_DEBUG", "Приложение открыто — игнорируем пуш");
            return; // Просто выходим из метода, ничего не рисуем в шторке
        }

        // 🔥 ЛОГИРУЕМ ВСЁ, что пришло
        Log.i("FCM_DEBUG", "🔔🔔🔔 onMessageReceived() CALLED! 🔔🔔🔔");
        Log.i("FCM_DEBUG", "From: " + message.getFrom());
        Log.i("FCM_DEBUG", "Notification object: " + message.getNotification());
        Log.i("FCM_DEBUG", "Data map: " + message.getData());

        // 🔥 Читаем из DATA (не из notification!)
        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String extram = message.getData().get("extram");
        String extram2 = message.getData().get("extram2");
        String type = message.getData().get("type");

        Log.i("FCM_DEBUG", "Parsed: title='" + title + "', body='" + body + "'");
        Log.i("FCM_DEBUG", "Parsed: extram='" + extram + "', extram2='" + extram2 + "'");

        // Ваша логика
        final String notif_mess;
        String s = "mess";

        if ("like".equals(type) || message.getData().containsKey("extram3")) {
            notif_mess = new Languages().NewLike();
            s = "like";
        } else {
            notif_mess = new Languages().NewMessage();
        }

        String currentUid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (currentUid != null && !currentUid.equals(extram)) {
            Log.i("FCM_DEBUG", "Showing notification for: " + extram2);
            sendMyNotification(s, body, extram, extram2, title);

            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getApplicationContext(), notif_mess, Toast.LENGTH_SHORT).show()
            );
        } else {
            Log.w("FCM_DEBUG", "Skipping notification: currentUid=" + currentUid + ", extram=" + extram);
        }
    }

    private void sendMyNotification(String s, String message, String extram, String extram2, String title) {
        // 🔥 Проверка разрешения для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.w("FCM_DEBUG", "⚠️ POST_NOTIFICATIONS not granted");
                return;
            }
        }

        Intent intent = new Intent(this, VideoActivityMess.class);
        intent.putExtra("curruser", extram);
        intent.putExtra("currname", extram2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);
        createNotificationChannel();

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // 🔥 Обязательно channel_id вторым параметром!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel_id")
                .setSmallIcon(s.equals("like") ? R.drawable.n_heart : R.drawable.mess)
                .setContentTitle(title != null && !title.isEmpty() ? title : (extram2 != null ? extram2 : "Уведомление"))
                .setContentText(message != null ? message : "")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager = getSystemService(NotificationManager.class);
        }
        if (manager != null) {
            Log.i("FCM_DEBUG", "🔔 Showing notification with ID 0");
            manager.notify(0, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel_id", "Основные уведомления", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Канал для пуш-уведомлений");
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.i("FCM_DEBUG", "✅ Notification channel created");
            }
        }
    }
    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        final String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }



}