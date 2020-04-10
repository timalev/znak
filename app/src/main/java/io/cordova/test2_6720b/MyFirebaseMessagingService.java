package io.cordova.test2_6720b;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private View mView;

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;



    @Override
    public void onMessageReceived(RemoteMessage message) {

        final String notif_mess;
        String s;

        String data = message.getData().get("extram");

        if (message.getData().containsKey("extram3"))
        {
            notif_mess = new Languages().NewLike();
            s = "like";
        }else
        {
            notif_mess = new Languages().NewMessage();
            s = "mess";
        }


        /*
        Intent nextScreen = new Intent(getApplication(), VideoActivityMess.class);

        nextScreen.putExtra("curruser", message.getData().get("extram").toString());
        nextScreen.putExtra("currname", message.getData().get("extram2").toString());

        startActivity(nextScreen);
*/
        if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getData().get("extram"))) {


            sendMyNotification(s, message.getNotification().getBody(), message.getData().get("extram"), message.getData().get("extram2"));

            Log.i("messbody:", data);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {

                    Toast.makeText(getApplicationContext(), notif_mess, Toast.LENGTH_SHORT).show();

                    //Context contex = context;


                }
            });


        }
    }

    private void sendMyNotification(String s, String message, String extram, String extram2) {

        //On click of notification it redirect to this Activity


        Intent intent = new Intent(this, VideoActivityMess.class);
        intent.putExtra("curruser", extram);
        intent.putExtra("currname", extram2);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;

        if (s.equals("like")) {

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(extram2)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.n_heart)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);
        }else
        {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(extram2)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.mess)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());




        //Toast.makeText(this, "Сообщение", Toast.LENGTH_LONG).show();

    }


}