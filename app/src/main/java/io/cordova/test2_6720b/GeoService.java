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
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GeoService extends Service {

    Handler mHandler = new Handler();

    private FirebaseAuth mAuth;

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Coords coords = new Coords(latitude, longitude);

            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").setValue(coords);

            //Toast.makeText(getApplication(), String.valueOf(latitude + "," +longitude), Toast.LENGTH_LONG).show();

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

    LocationManager mLocationManager;

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);


        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mAuth = FirebaseAuth.getInstance();

        Context context = getApplicationContext();

        final LocationManager lm = (LocationManager) getSystemService(context.LOCATION_SERVICE);

        //mHandler.postDelayed(ToastRunnable, 5000);


        //Location myLocation = getLastKnownLocation();

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mAuth.getCurrentUser().getUid()).child("status").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  Log.d("status: ", String.valueOf(dataSnapshot.getValue()));


                if (dataSnapshot.getValue().equals("online")) {


                    if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

                }else
                {
                    //Toast.makeText(getApplication(), String.valueOf("ХУЙ"), Toast.LENGTH_LONG).show();

                    lm.removeUpdates(locationListener);
                    // locationListener = null;

                }
                  /*

                for (DataSnapshot child : dataSnapshot.getChildren()) {


                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    Runnable ToastRunnable = new Runnable() {
        public void run() {

            Context context = getApplicationContext();

            LocationManager lm = (LocationManager)getSystemService(context.LOCATION_SERVICE);

            Location myLocation = getLastKnownLocation();

            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



            if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

                Toast.makeText(getApplication(), "Включите геолокацию", Toast.LENGTH_LONG).show();

            }else {

                Coords coords = new Coords(myLocation.getLongitude(), myLocation.getLatitude());

                Toast.makeText(getApplication(), String.valueOf(myLocation.getLongitude() + " / " + myLocation.getLatitude()), Toast.LENGTH_LONG).show();

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mAuth.getCurrentUser().getUid()).child("coords").setValue(coords);

                // Log.d("geo data: ", String.valueOf(myLocation.getLongitude() + " / " + myLocation.getLatitude()));
            }


            mHandler.postDelayed( ToastRunnable, 5000);
        }
    };

}