package io.cordova.test2_6720b;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class GeoService2 extends Service {

    Handler mHandler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler.postDelayed(ToastRunnable, 5000);


        // Log.i("Params: ",device_token);

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    Runnable ToastRunnable = new Runnable() {
        public void run() {


            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();


                    Toast.makeText(getApplication(), String.valueOf(latitude + "," + longitude), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

/*
            String device_token = FirebaseInstanceId.getInstance().getToken();


            Location myLocation = getLastKnownLocation();

            if (getLastKnownLocation()!=null) {

                Toast.makeText(getApplication(), String.valueOf(myLocation.getLongitude() + " / " + myLocation.getLatitude() + " / " + device_token), Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplication(), "ХУЙ", Toast.LENGTH_LONG).show();

            }
*/

            mHandler.postDelayed( ToastRunnable, 5000);
        }
    };
}
