package io.cordova.test2_6720b;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class DetectLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_location);


        LocationManager lm = (LocationManager) getApplication().getSystemService(this.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (gps_enabled) {

                Intent serviceIntent2 = new Intent(getApplicationContext(), GeoService2.class);
                getApplication().startService(serviceIntent2);


                //   Toast.makeText(getApplication(), "Геолокация включена", Toast.LENGTH_SHORT).show();
            } else {
                displayPromptForEnablingGPS(DetectLocation.this);
                //Toast.makeText(getApplication(), "ВКЛЮЧИТЕ ГЕОЛОКАЦИЮ!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex) {

            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();

        }





    }

    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Пожалуйста, включи геолокацию на устройстве и нажми снова!";

        builder.setMessage(message)
                .setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                //FirebaseDatabase.getInstance().getReference().child("users6").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");

                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Отменить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }
}
