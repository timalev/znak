package io.cordova.test2_6720b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PrintMessage extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Обнаружено сообщение: " +
                        intent.getStringExtra("ru.alexanderklimov.broadcast.Message"),
                Toast.LENGTH_LONG).show();

        Log.d("soob:","ok");
    }
}