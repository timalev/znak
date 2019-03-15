package io.cordova.test2_6720b;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.ui.IconGenerator;


//import com.google.maps.android.ui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// видеоплеер - https://www.javatpoint.com/playing-video-in-android-example

import static io.cordova.test2_6720b.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private TextView helloTextView;

    PopupWindow popUpWindow;

    private ImageView TargetImage;

    private DatabaseReference mPostReference;

    private Map<String, Marker> MarkersArray = new HashMap<String, Marker>();

    private FirebaseAuth mAuth;

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, 10, 10);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private FirebaseUser user;

    private StorageReference storageRef;

    private FirebaseStorage storage;

    private StorageReference videoRef;

    private String filesdir = "Android/data/io.cordova.bizone2/files";

    public static final int CAMERA_REQUEST_CODE = 1999;
    public static final int REQUEST_IMAGE_CAPTURE = 777;


    private double longitude;
    private double latitude;

    SharedPreferences sPref;

    final String SAVED_TEXT = "saved_text";

    private StorageReference photoRef;




    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            //  double longitude = location.getLongitude();
            // double latitude = location.getLatitude();

            //  Toast.makeText(getApplication(), String.valueOf(latitude), Toast.LENGTH_LONG).show();

            //Log.d("lat/lng: ", longitude + "/" + latitude);
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
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.reset:

                String curruser = getIntent().getExtras().getString("curruser");
                String currname = getIntent().getExtras().getString("currname");
                String activity = getIntent().getExtras().getString("activity");

                Intent Back;

                //Toast.makeText(this, "Ебашим назад, и не забываем седня подключить сервис к геолокации!!!", Toast.LENGTH_LONG).show();

                switch (activity)
                {
                    case "videoactivitymess":
                        Back = new Intent(getApplication(), VideoActivityMess.class);
                        break;

                        default:
                            Back = new Intent(getApplication(), VideoActivity.class);
                }

                Back.putExtra("curruser", curruser);
                Back.putExtra("currname", currname);

                startActivity(Back);

                finish();

                return true;

            case R.id.about:

               // initiatePopupWindow();

                //Toast.makeText(this, "О приложении", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_map, menu);

        return true;

    }
/*
    private void initiatePopupWindow() {
        try {


            View convertView = getLayoutInflater().inflate(R.layout.popap, null);
            // ImageButton CloBtn = (ImageButton) convertView.findViewById(R.id.clo);
            Button CloBtn = (Button) convertView.findViewById(R.id.clo);

            // final PopupWindow popUpWindow = new PopupWindow(convertView,400,550,false);

            final PopupWindow popUpWindow = new PopupWindow(convertView, getResources().getInteger(R.integer.popup_height),getResources().getInteger(R.integer.popup_width),false);

            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.relLayout);

            popUpWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

            CloBtn.setOnClickListener(this);

            CloBtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View popupView) {
                    //Toast.makeText(getApplication(), "ОК", Toast.LENGTH_LONG).show();

                    popUpWindow.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Загрузка видео: ", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE)
        {
            if (resultCode == RESULT_OK) {





/*

                Uri selectedImage = data.getData();
                ContentResolver cr = this.getContentResolver();
                String mime = cr.getType(selectedImage);
                PlusShare.Builder share = new PlusShare.Builder(this);

                share.setText("hello everyone!");
                share.addStream(selectedImage);
                share.setType(mime);
                startActivityForResult(share.getIntent(), 1);

*/




                Toast.makeText(this, "Take picture OK", Toast.LENGTH_SHORT).show();

                UploadPicture();


            } else {
                Toast.makeText(this, "Take picture Error", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {

                Log.d("Загрузка видео2: ", "Record OK. requestCode: " + requestCode + ", resultCode: " + resultCode);

                Toast.makeText(this, "Record OK", Toast.LENGTH_SHORT).show();

                Intent serviceIntent3 = new Intent(this, UploadService.class);
                serviceIntent3.putExtra("latitude", latitude);
                serviceIntent3.putExtra("longitude", longitude);
                this.startService(serviceIntent3);

            } else {
                Toast.makeText(this, "Record err.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Common error: requestCode: " + requestCode + ", resultCode: " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());

        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);



        // Toast.makeText(getApplication(), Environment.getExternalStorageDirectory().toString(), Toast.LENGTH_LONG).show();


        File folder = new File(Environment.getExternalStorageDirectory(),filesdir);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (success) {
            Log.d("files dir: ", "директория files создана");

            // Do something on success
        } else {
            Log.d("files dir: ", "Ошибка создания директории");

        }


        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();



        //LocationManager lm = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);

/*
        LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        */

/*
        if (getLastKnownLocation()!=null) {

            longitude = getLastKnownLocation().getLongitude();
            latitude = getLastKnownLocation().getLatitude();
        }
        else
        {
            longitude = 0.00;
            latitude = 0.00;

            Toast.makeText(getApplication(), "Включите геолокацию!", Toast.LENGTH_SHORT).show();
        }
*/


        // Toast.makeText(getApplication(), longitude + ", " + latitude, Toast.LENGTH_LONG).show();

        // lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // https://firebase.google.com/docs/storage/android/download-files

/*

        if (mAuth.getCurrentUser()!=null) {

            //gs://test2-6720b.appspot.com/3sVWYEqSnSRTiIa79B0yP19e16V2.jpg

            // http://java-help.ru/glide-getting-started/

            // Toast.makeText(this, "Юзер в СЕТИ!", Toast.LENGTH_LONG).show();

            Intent serviceIntent = new Intent(this, PushService.class);
            this.startService(serviceIntent);

            Intent serviceIntent2 = new Intent(this, GeoService.class);
            this.startService(serviceIntent2);

        }

*/

        setContentView(R.layout.activity_maps);

        View myView = getView3();
/*
        RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.relLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        View view = ltInflater.inflate(R.layout.image, relLayout, false);

        ImageView mark = (ImageView) view.findViewById(R.id.MarkerIcon);
       // mark.setImageResource(R.drawable.marker);

        //ViewGroup.LayoutParams lp = view.getLayoutParams();

        Glide
                .with(this)
                .load(R.drawable.target)
                .into(mark);



        relLayout.addView(view);
*/



/*
        ImageView mark = (ImageView) findViewById(R.id.MarkerIcon);
        mark.setImageResource(R.drawable.marker);



        Glide
                .with(getApplication().getApplicationContext() )
                .load(R.drawable.pho)
                .into(mark);
*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_maps_activity);
        setSupportActionBar(toolbar);



        Button TargetBtn = (Button) findViewById(R.id.btn1);



/*
        float zoom = mMap.getCameraPosition().zoom;

        if (zoom==2.0)
            ZoomOutBtn.setVisibility(View.GONE);

            */

        final ImageButton VideoBtn = (ImageButton) findViewById(R.id.Button3);



        VideoBtn.setVisibility(View.GONE);


        VideoBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //Button Pressed
                    VideoBtn.setImageResource(R.drawable.cam2);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //finger was lifted

                    VideoBtn.setImageResource(R.drawable.cam1);
                }
                return false;
            }
        });

        VideoBtn.setOnClickListener(this);

        TargetBtn.setOnClickListener(this);




        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(new Config2().tab_users);



        //   User user = new User("test");


        //  mapFragment.getView().setVisibility(View.GONE);


        helloTextView = (TextView) findViewById(R.id.text_view_id);


        helloTextView.setText(R.string.hello);







    }


    public void onClick(View v){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();

        switch (v.getId()) {

            case R.id.Button3:

                FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(false);


                // сообщаем для слушателя сообщений , что это видео и отправлять пуши не надо

                // edit.putBoolean("sendnotific", true);
                // edit.commit();

                /*

                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,3);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("sdcard/" + filesdir,"story.mp4")));

                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);

                */


                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File videoFile = null;
                    try {
                        videoFile = createImageFile("story.mp4");
                    } catch (IOException ex) {

                        Toast.makeText(this, "rcreateImageFile error.", Toast.LENGTH_LONG).show();

                    }
                    // Continue only if the File was successfully created
                    if (videoFile != null) {

                        Uri videoURI = Uri.fromFile(videoFile);

                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,3);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                        startActivityForResult(takeVideoIntent, CAMERA_REQUEST_CODE);

                    }
                }else
                {
                    Toast.makeText(this, "resolveActivity error.", Toast.LENGTH_LONG).show();
                }



                break;
/*
            case R.id.pho:



                // сообщаем для слушателя сообщений , что это фото и отправлять пуши не надо

                //  Toast.makeText(this, "resolveActivity error.", Toast.LENGTH_LONG).show();


                FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(false);



                edit.putBoolean("sendnotific", true);
                edit.commit();

                //FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").setValue("yes");

                //Intent photoScreen = new Intent(this, PhotoActivity.class);
                //startActivity(photoScreen);

                //Toast.makeText(this, "Кнопка Photo нажата.", Toast.LENGTH_LONG).show();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile("photo.jpg");
                    } catch (IOException ex) {

                        Toast.makeText(this, "rcreateImageFile error.", Toast.LENGTH_LONG).show();

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        Uri photoURI = Uri.fromFile(photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else
                {
                    Toast.makeText(this, "resolveActivity error.", Toast.LENGTH_LONG).show();
                }


                break;
*/
            case R.id.btn1:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mAuth.getCurrentUser().getUid()).child("coords").addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        Coords coords = dataSnapshot.getValue(Coords.class);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coords.lat, coords.lng), 17.0f));

                        //Toast.makeText(getApplication(), String.valueOf(coords.lat), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));
                break;

            case R.id.btn2:

                // Toast.makeText(getApplication(), String.valueOf(zoom), Toast.LENGTH_LONG).show();

                mMap.animateCamera(CameraUpdateFactory.zoomOut());

                break;


        }
    }


    public View getView3()
    {
        RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.relLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        View view = ltInflater.inflate(R.layout.image, relLayout, false);

        ImageView mark = (ImageView) view.findViewById(R.id.MarkerIcon);
        // mark.setImageResource(R.drawable.marker);

        //ViewGroup.LayoutParams lp = view.getLayoutParams();

        Glide
                .with(this)
                .load(R.drawable.target)
                .into(mark);



        return view;
    }

    public View getView() {
        ViewHolder viewHolder;


        View convertView = getLayoutInflater().inflate(R.layout.cluster_view, null);

        viewHolder = new ViewHolder();
        viewHolder.text = (TextView) convertView.findViewById(R.id.priceTag);
        viewHolder.icon = (ImageView) convertView.findViewById(R.id.MarkerIcon);

        convertView.setTag(viewHolder);

        viewHolder.text.setText("Position " );

        Glide
                .with(this)
                .load("https://lh5.googleusercontent.com/-QXRo-IWOCfI/AAAAAAAAAAI/AAAAAAAAABM/ioaz3ND8AWQ/s96-c/photo.jpg")
                .asBitmap()
                .centerCrop()
                .into(viewHolder.icon);

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView icon;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Уменьшитель камеры

        final Button ZoomOutBtn = (Button) findViewById(R.id.btn2);
        ZoomOutBtn.setOnClickListener(this);

        // если уменьшение самое маленькое - убираем кнопку чтобы не маячила

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                if(cameraPosition.zoom > 2.0) {

                    ZoomOutBtn.setVisibility(View.VISIBLE);

                    //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    ZoomOutBtn.setVisibility(View.GONE);
                    // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });



        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("videolat") && extras.containsKey("videolng")) {


                Double videolat = Double.parseDouble(getIntent().getExtras().getString("videolat"));
                Double videolng = Double.parseDouble(getIntent().getExtras().getString("videolng"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(videolat, videolng), 17.0f));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(videolat, videolng)));

                // mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );




            }
        }




        // Выводим маркеры на карту


        FirebaseDatabase.getInstance().getReference()
                .child(new Config2().tab_users).orderByKey().addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // mMap.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String curractivity = getIntent().getExtras().getString("activity");
                    String curruser = getIntent().getExtras().getString("curruser");
                    String currname = getIntent().getExtras().getString("currname");

                    //Log.i("mapsact: ", curractivity + " / " +curruser + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid());


                    if (child.getKey().equals(curruser) || child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {

                        Log.i("mapsact2: ", curractivity +  ", getKey: " + child.getKey() + " / " +curruser + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid());


                        Post post = dataSnapshot.child(child.getKey()).getValue(Post.class);
                    Coords coords = dataSnapshot.child(child.getKey()).child("coords").getValue(Coords.class);

                    if (coords!=null) {

                        final String user = child.getKey();
                        final String name = post.name;
                        final String photo = post.photo;
                        final String avatar = post.avatar;
                        final Double lat = coords.lat;
                        final Double lng = coords.lng;

                        Marker test = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));
                        loadMarkerIcon(test, name, avatar);

                        MarkersArray.put(user, test);
                    }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });

        // операции с маркерами маркеров

        mPostReference.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                   /*  просмотр массива маркеров

                    for(Map.Entry<String, Marker> entry : MarkersArray.entrySet()) {
                        String key = entry.getKey();
                        Marker value = entry.getValue();

                        Log.d("Маркеры: ", "ключ: " +  key + ", значение: " + value);
                    }
                    */

                for (DataSnapshot child : dataSnapshot.getChildren()) {


                    String curractivity = getIntent().getExtras().getString("activity");
                    String curruser = getIntent().getExtras().getString("curruser");
                    String currname = getIntent().getExtras().getString("currname");



                    if (child.getKey().equals(curruser) || child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {

                        Log.i("mapsact: ", curractivity +  ", getKey: " + child.getKey() + " / " +curruser + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid());



                        Post post = dataSnapshot.child(child.getKey()).getValue(Post.class);
                    Coords coords = dataSnapshot.child(child.getKey()).child("coords").getValue(Coords.class);

                    if (coords!=null) {

                        final Double lat = coords.lat;
                        final Double lng = coords.lng;
                        final String status = post.status;
                        final String photo = post.photo;

                        Marker marker = MarkersArray.get(child.getKey());

                        if (marker!=null) {

                            // if (photo!=null)
                            //loadMarkerIcon(marker,photo);

                            // Log.d("Маркеры2: ", String.valueOf(MarkersArray.get(child.getKey()) + ", lat/lng: " + lat + "/" + lng));

                            if (status.equals("online")) {
                                // MarkersArray.get(child.getKey()).showInfoWindow();
                                marker.setVisible(true);

                                //MarkersArray.get(child.getKey()).showInfoWindow();
                            }

                            if (status.equals("offline")) {
                                // MarkersArray.get(child.getKey()).showInfoWindow();
                                marker.setVisible(false);
                            }
                            marker.setPosition(new LatLng(lat, lng));
                        }
                    }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });



        mMap.setOnMarkerClickListener(this); // слушаем клики по маркерам

    }
    // gs://test2-6720b.appspot.com/3sVWYEqSnSRTiIa79B0yP19e16V2.jpg


    private void loadMarkerIcon(final Marker marker, final String name, final String photo) {

        // Функция оформления маркеров на карте

        Glide.with(this)

                .load(photo)  // грузим фото на которое заменим маркер
                .asBitmap()  // переводим его в нужный формат
                .error(R.drawable.noavatar)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(150,150) {




                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                        // xml-сборка страницы маркеров, шаблон в файле cluster_view.xml


                        IconGenerator generator = new IconGenerator(getApplication());

                        ViewHolder viewHolder = new ViewHolder();

                        LayoutInflater ltInflater = getLayoutInflater();

                        View convertView = ltInflater.inflate(R.layout.cluster_view, null);

                        viewHolder.text = (TextView) convertView.findViewById(R.id.priceTag);
                        viewHolder.icon = (ImageView) convertView.findViewById(R.id.MarkerIcon);

                        convertView.setTag(viewHolder);

                        viewHolder.text.setText(name);
                        //viewHolder.icon.setImageResource(R.drawable.marker); // можно стандартный маркер подгрузить вместо аватарок
                        viewHolder.icon.setImageBitmap(bitmap); // установка подгружаемого фото в код xml-страницы

                        generator.setBackground(null);
                        generator.setContentView(convertView);

                        Bitmap icon = generator.makeIcon();

                        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromBitmap(icon);

                        marker.setIcon(icon2);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        //Log.i("Glide_exep: ", name);


                        IconGenerator generator = new IconGenerator(getApplication());

                        ViewHolder viewHolder = new ViewHolder();

                        LayoutInflater ltInflater = getLayoutInflater();

                        View convertView = ltInflater.inflate(R.layout.cluster_view, null);

                        viewHolder.text = (TextView) convertView.findViewById(R.id.priceTag);
                        viewHolder.icon = (ImageView) convertView.findViewById(R.id.MarkerIcon);

                        convertView.setTag(viewHolder);

                        viewHolder.text.setText(name);
                        //viewHolder.icon.setImageResource(R.drawable.marker); // можно стандартный маркер подгрузить вместо аватарок
                        viewHolder.icon.setImageResource(R.drawable.noavatar); // установка подгружаемого фото в код xml-страницы

                        generator.setBackground(null);
                        generator.setContentView(convertView);

                        Bitmap icon = generator.makeIcon();

                        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromBitmap(icon);

                        marker.setIcon(icon2);


                        //never called
                    }

                });

    }



    @Override
    public boolean onMarkerClick(final Marker marker) {


        for (Map.Entry<String, Marker> e : MarkersArray.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();

            if (value.toString().equals(marker.toString()))
            {
                Intent nextScreen = new Intent(this, VideoActivity.class);

                nextScreen.putExtra("curruser", key.toString());

                startActivity(nextScreen);

                finish();
                // Toast.makeText(this, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();

            }
        }



        return false;
    }



    void UploadPicture()
    {
        final File photo = new File("sdcard/" + filesdir,"photo.jpg");
        File photo2;

        if (photo.exists()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile("sdcard/" + filesdir + "/photo.jpg", options);
            int width = options.outWidth;
            int height = options.outHeight;

            if (width>1000) {

                int nh = 1000 * height / width;

                BitmapFactory.Options options2 = new BitmapFactory.Options();

                Bitmap mybit = BitmapFactory.decodeFile("sdcard/" + filesdir + "/photo.jpg", options2);
                options2.inJustDecodeBounds = false;

                Bitmap resized = Bitmap.createScaledBitmap(mybit, 1000, nh, true);

                FileOutputStream fOut;
                try {
                    File small_picture = new File("sdcard/" + filesdir, "photo.jpg");
                    fOut = new FileOutputStream(small_picture);
                    // 0 = small/low quality, 100 = large/high quality
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    resized.recycle();

                    photo2 = new File("sdcard/" + filesdir,"photo.jpg");

                } catch (Exception e) {
                    Log.e("file err:", "Failed to save/resize image due to: " + e.toString());
                    photo2 = photo;
                }
            }
            else
            {
                photo2 = photo;
            }



            photoRef = storageRef.child(mAuth.getCurrentUser().getUid() + ".jpg");


            try {

                InputStream stream2 = new FileInputStream(photo2);

                UploadTask uploadTask = photoRef.putStream(stream2);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("Ошибка загрузки: ", "не успешно");

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("video").setValue(downloadUrl.toString());
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("type").setValue("jpg");
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("coords2").child("lat").setValue(latitude);
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("coords2").child("lng").setValue(longitude);
                        // longitude = 0.00;
                        //  latitude = 0.00;

                        if (photo.delete()) {

                            Toast.makeText(getApplication(), "Фото успешно добавлено!", Toast.LENGTH_LONG).show();

                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
/*
                            Notific notific = new Notific(downloadUrl.toString(),mAuth.getCurrentUser().getUid(),ts,"jpg",mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getPhotoUrl().toString());
                            FirebaseDatabase.getInstance().getReference().child("notific").child("uid").setValue(notific);
*/
                            // Удаление комментов при загрузки нового фото



                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByChild("page_for_comment").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).child(child.getKey()).removeValue();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                                        }
                                    });
                        }

                    }
                });

            } catch (Exception e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("Ошибка файла: ", e.getMessage());
                e.printStackTrace();
            }


        }
    }


    private File createImageFile(String fileName) throws IOException {
        // Create an image file name

        File image = new File("sdcard/" + filesdir,fileName);

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

}

