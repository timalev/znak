package io.cordova.test2_6720b;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class VideoActivityMess extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView numbersList;
    private numbersAdapter adapter;
    ArrayList<Profile2> list;

    private VideoView videoView;
    private TextView VideoText;
    private TextView VideoText2;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private String curruser;
    private String currname;

    private double longitude;
    private double latitude;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        String curruserM;


        if (getIntent().getExtras().containsKey("extram")) {

            curruserM = getIntent().getExtras().getString("extram");

        } else {

            curruserM = getIntent().getExtras().getString("curruser");
        }

       // Log.d("curruserM: ", curruserM);

        for(int i = 0; i < menu.size(); i++){

            if (menu.getItem(i).toString().equals("написать письмо"))
            {
                MenuItem item=menu.getItem(i);

                item.setVisible(false);
            }



            if (menu.getItem(i).toString().equals("к сообщениям")) {
                final MenuItem item = menu.getItem(i);
                final Drawable drawable = item.getIcon();

                drawable.setAlpha(255);

               // drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

            }
                if (menu.getItem(i).toString().equals("показать на карте"))
            {
                final MenuItem item=menu.getItem(i);
                final Drawable drawable = item.getIcon();

                //item.setEnabled(false);
                //drawable.setAlpha(130);

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruserM).child("status").addValueEventListener( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.getValue().equals("online")) {

                            item.setEnabled(true);
                            drawable.setAlpha(255);
                        }else
                        {
                            item.setEnabled(false);
                            drawable.setAlpha(130);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                // Log.d("Item: ", String.valueOf(menu.getItem(i)));
            }

            if (menu.getItem(i).toString().equals("моё местоположение"))
            {
                final MenuItem item=menu.getItem(i);
                final Drawable drawable = item.getIcon();

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").addValueEventListener( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {


                        //Toast.makeText(getApplication(),dataSnapshot.getValue() + "/" + curruser, Toast.LENGTH_LONG).show();
                        //Log.d("Status: ", String.valueOf(dataSnapshot.getValue()));

                        if (dataSnapshot.getValue()!=null) {

                            if (dataSnapshot.getValue().equals("online")) {

                                //Log.d("StatusG: ", "Online");

                                item.setEnabled(true);
                                drawable.setAlpha(255);

                                if (!isMyServiceRunning(GeoService.class)) {

                                    Intent serviceIntent2 = new Intent(getApplicationContext(), GeoService.class);
                                    getApplication().startService(serviceIntent2);
                                }
                            } else {
                                // Log.d("StatusG: ", "Offline");

                                item.setEnabled(true);
                                drawable.setAlpha(130);

                                if (isMyServiceRunning(GeoService.class)) {

                                    Intent serviceIntent2 = new Intent(getApplicationContext(), GeoService.class);
                                    getApplication().stopService(serviceIntent2);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // Log.d("Item: ", String.valueOf(menu.getItem(i)));

            }



        }

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {


        final MenuItem delphoto = menu.findItem(R.id.delphoto);
        final MenuItem banuser = menu.findItem(R.id.banuser);


                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2"))
                {
                    delphoto.setVisible(true);
                    banuser.setVisible(true);

                }else
                {
                    delphoto.setVisible(false);
                    banuser.setVisible(false);
                }



        return true;
    }

    final String SAVED_TEXT = "saved_text";

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            Toast.makeText(getApplication(), "Не верный формат видео", Toast.LENGTH_SHORT).show();

            return true;
        }
    };


    //private String filesdir = "Android/data/io.cordova.bizone2/files";
    private String filesdir =  Environment.getExternalStorageDirectory().toString() + "/Android/data/io.cordova.test2_6720b/files";



    public static final int REQUEST_IMAGE_CAPTURE = 777;

    private StorageReference storageRef;
    private FirebaseStorage storage;
    private StorageReference photoRef;

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
            case R.id.friends:

                Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                startActivity(usersScreen);


                //Toast.makeText(this, "Аватарка обновлена", Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:

                Functions functions = new Functions();

                functions.About(VideoActivityMess.this);

                //Toast.makeText(this, "О приложении", Toast.LENGTH_LONG).show();
                return true;
/*
            case R.id.mapmode:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Log.d("LAT/LNG: ", String.valueOf(dataSnapshot.child("coords/lat").getValue()));

                        Log.d("CurruserMap: ", curruser);


                        Intent prevScreen = new Intent(getApplicationContext(),MapsActivity.class);

                        prevScreen.putExtra("videolat", String.valueOf(dataSnapshot.child("coords/lat").getValue()));
                        prevScreen.putExtra("videolng", String.valueOf(dataSnapshot.child("coords/lng").getValue()));
                        prevScreen.putExtra("curruser", curruser);
                        prevScreen.putExtra("currname", currname);
                        prevScreen.putExtra("activity", "videoactivitymess");


                        startActivity(prevScreen);

                        finish();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


               return true;
               */
                case R.id.revert:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Log.d("LAT/LNG: ", String.valueOf(dataSnapshot.getKey()));


                            Intent allmess = new Intent(getApplication(), VideoActivityAllMess.class);

                            allmess.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            allmess.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                            startActivity(allmess);

                            finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                return true;

            case R.id.banuser:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_banlist).child(curruser).setValue(1);

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).child("profile_photo").removeValue()

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getApplicationContext(), "Юзер забанен!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getApplicationContext(), "Ошибка с баном юзера.", Toast.LENGTH_SHORT).show();

                                    }
                                }); //

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }});


                return true;

            case R.id.delphoto:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).child("profile_photo").removeValue()

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(), "Фото удалено!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "Ошибка с удалением фото.", Toast.LENGTH_SHORT).show();

                            }
                        }); //





               // Toast.makeText(this, "Фото удалено!", Toast.LENGTH_SHORT).show();


                return true;

            case R.id.messages:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Log.d("LAT/LNG: ", String.valueOf(dataSnapshot.getKey()));


                            Intent allmess = new Intent(getApplication(), VideoActivityAllMess.class);

                            allmess.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            allmess.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                            startActivity(allmess);

                            finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                return true;
/*
            case R.id.myplace:


                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mAuth.getCurrentUser().getUid()).child("status").addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        //  Log.d("status: ", String.valueOf(dataSnapshot.getValue()));
if (dataSnapshot.getValue()!=null) {


    if (dataSnapshot.getValue().equals("online")) {


        mPostReference.child(mAuth.getCurrentUser().getUid()).child("status").setValue("offline");

    } else {
        LocationManager lm = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (gps_enabled) {

                mPostReference.child(mAuth.getCurrentUser().getUid()).child("status").setValue("online");

                Coords coords = new Coords(latitude, longitude);

                if (latitude > 0 && longitude > 0) {

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").setValue(coords);
                }




                //   Toast.makeText(getApplication(), "Геолокация включена", Toast.LENGTH_SHORT).show();
            } else {
                displayPromptForEnablingGPS(VideoActivityMess.this);
                //Toast.makeText(getApplication(), "ВКЛЮЧИТЕ ГЕОЛОКАЦИЮ!", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ex) {

            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();

        }




    }
}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;



*/
            case R.id.zal:

               // Log.i("tags","ok");

                takeScreenshot(curruser, currname);

                return true;


            case R.id.pho:

                //Toast.makeText(this, "Кнопка нажата", Toast.LENGTH_LONG).show();

                //  FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(false);

                // edit.putBoolean("sendnotific", true);
                // edit.commit();

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

                        //Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".io.cordova.bizone2.provider", photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else
                {
                    Toast.makeText(this, "resolveActivity error.", Toast.LENGTH_LONG).show();
                }

                return true;


            case R.id.index:


                /*
                Intent nextScreen = new Intent(getApplication(), VideoActivity.class);

                nextScreen.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                nextScreen.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                startActivity(nextScreen);
*/
                Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                startActivity(Profile);

                finish();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Загрузка видео: ", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE)
        {
            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Take picture OK", Toast.LENGTH_SHORT).show();

                UploadPicture();

            } else {
                Toast.makeText(this, "Take picture Error", Toast.LENGTH_SHORT).show();
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


        //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("currtime").setValue(ts2);





        // Создаем директорию для фото


/*
        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "test");

        Log.i("test: ", Environment.getExternalStorageDirectory().getAbsolutePath());

        if (!externalAppDir.exists()) {

            externalAppDir.mkdir();

            Toast.makeText(this, String.valueOf(externalAppDir.mkdir()), Toast.LENGTH_SHORT).show();

        }
*/


/*
        final File dir = new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), "io.cordova.bizone2");

        if (!dir.mkdirs()) {

            Toast.makeText(this, "ХУЙ!!!", Toast.LENGTH_SHORT).show();
        }
        */

/*
        File file = new File(externalAppDir , "FileName.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        //Log.d("Сервис запущен: ",  String.valueOf(isMyServiceRunning(GeoService.class)));

        // если на мобиле отключена геолокация ставим offline

        if (!isLocationEnabled(getApplicationContext())) {
            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }

         // слушаем геолокацию на мобиле (включаем/отключаем), ставим в базу online/offline соответственно

        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


        // Toast.makeText(getApplication(), "HELLO!!", Toast.LENGTH_LONG).show();


        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dw =  (int)convertPixelsToDp(size.x, getApplicationContext());

        int dw2 = (int)convertDpToPixel(size.x, getApplicationContext());

        //Log.d("информация2: ",  "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2);

        //Toast.makeText(getApplication(), "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();

       // FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(true);


        setContentView(R.layout.activity_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);



        mPostReference = FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users);

        VideoText = (TextView) findViewById(R.id.text_view_id);


        // VideoText.setText(R.string.hello);

/*
        videoView = (VideoView) findViewById(R.id.video_id);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
*/
        final LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearlayout1);


        final LinearLayout linearLayout2 = (LinearLayout) this.findViewById(R.id.linearlayout2);




        if (getIntent().getExtras().containsKey("extram")) {

            curruser = getIntent().getExtras().getString("extram");
            currname = getIntent().getExtras().getString("extram2");




        } else {

            curruser = getIntent().getExtras().getString("curruser");
            currname = getIntent().getExtras().getString("currname");
        }

        // Смотрим фотку если мы админы

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2")) {

            final ImageView imageView = (ImageView) findViewById(R.id.UserPho);
            imageView.setVisibility(View.VISIBLE);

            final TextView textView = (TextView) findViewById(R.id.UserGeo);
            textView.setVisibility(View.VISIBLE);




            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    textView.setText(dataSnapshot.child("profile_country").getValue().toString());


                    Glide
                            .with(getApplicationContext())
                            .load(dataSnapshot.child("profile_photo").getValue())
                            .error(R.drawable.noavatar)
                            .into(imageView);


                    //Toast.makeText(getApplication(), String.valueOf(dataSnapshot.child("profile_photo").getValue()), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // Смотрим фотку если мы админы end



        setTitle(currname);

        toolbar.setTitleTextColor(Color.WHITE);

        if (getLastKnownLocation()!=null) {

            longitude = getLastKnownLocation().getLongitude();
            latitude = getLastKnownLocation().getLatitude();
        }
        else
        {
            longitude = 0;
            latitude = 0;

            //Toast.makeText(getApplication(), "Включите геолокацию!", Toast.LENGTH_SHORT).show();
        }


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName() + "_" + FirebaseAuth.getInstance().getCurrentUser().getUid());


        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);



        //Toast.makeText(getApplication(), curruser + "/OK" +currname, Toast.LENGTH_LONG).show();


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // if (dataSnapshot.hasChild("mess_count")) {

                MessStat2 Messstat2 = new MessStat2(0,"t");

                Log.i("test333",FirebaseAuth.getInstance().getCurrentUser().getUid() + " / " + curruser );


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).child("mess_count").setValue(0);
                   // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(mAuth.getCurrentUser().getUid()).child(curruser).child("readed").setValue("t");
                    // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(mAuth.getCurrentUser().getUid()).child(curruser).child("readed").setValue("t");
                    //FirebaseDatabase.getInstance().getReference().child("messagesStat").child(curruser).child(mAuth.getCurrentUser().getUid()).child("readed").setValue("y");


                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});



        numbersList = (RecyclerView) findViewById(R.id.rv_numbers);
        numbersList.setLayoutManager(new LinearLayoutManager(this));



        list = new ArrayList<Profile2>();


        ValueEventListener GetAllMessagesListener = new ValueEventListener() {


            //  FirebaseDatabase.getInstance().getReference().child("messagesPrivate").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {


                    Profile2 p = child.getValue(Profile2.class);
                    list.add(p);


                }

                numbersList.scrollToPosition(list.size()-1);

                adapter = new numbersAdapter(VideoActivityMess.this,list);

                numbersList.setAdapter(adapter);



                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };



        // Подгружаем все оообщения юзеров

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(GetAllMessagesListener);

        final ArrayList<String> data = new ArrayList<String>();

        // если нет еще сообщений вносим в массив значение чтобы он отображал первое сообщение

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()==0)
                    data.add("тима");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).limitToLast(1).addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {

                // Toast.makeText(getApplication(), String.valueOf(data2.size()), Toast.LENGTH_LONG).show();

                Mess messages = snapshot.getValue(Mess.class);

                final String CommText = String.valueOf(messages.mess);
                String user = String.valueOf(messages.user);
                final String name = messages.name;
                String page_for_comment = messages.page_for_comment;
                String avatar = messages.avatar;

                String type = messages.type;


                // if (mAuth.getCurrentUser().getUid()!=user)

                //Toast.makeText(getApplication(), mAuth.getCurrentUser().getUid() + ", " + user, Toast.LENGTH_LONG).show();


                // печатаем сообщения только если в массиве есть данные, иначе он выведет последнее введенное

                if (data.size() > 0) {

                    Profile2 p = snapshot.getValue(Profile2.class);
                    list.add(p);


                    numbersList.scrollToPosition(list.size()-1);

                    adapter = new numbersAdapter(VideoActivityMess.this,list);

                    adapter.notifyItemInserted(list.size()-1);

                }



                // pojoList.clear();
                // Toast.makeText(getApplication(), String.valueOf(data.size()), Toast.LENGTH_LONG).show();

                data.add("tima");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });


    }


    public void onClick(final View v){
        switch (v.getId()) {

            case R.id.send:

                Long tsLong = System.currentTimeMillis()/1000;
                final String ts = tsLong.toString();

                final EditText message = (EditText) findViewById(R.id.editText1);

/*
                message.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.charAt(s.length() - 1) == '\n') {
                      //  Toast.makeText(getApplication(), "Аватарка обновлена", Toast.LENGTH_LONG).show();
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplication().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(message.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);;
                    }
                }
            });
*/

                if (!message.getText().toString().matches("")) {

                    // сообщаем для слушателя, что это не видео или фото, чтобы он отправлял пуш

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putBoolean("sendnotific", false);
                    edit.commit();

                    // FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").setValue("no");

                    //  FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).removeValue();
                    // FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(ts);




                    InputMethodManager inputManager =
                            (InputMethodManager) getApplication().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(message.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);;
/*
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    */

//final String mUser = mAuth.getCurrentUser().getDisplayName();
final String mText = message.getText().toString();

// здесь
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotusers) {

                            final String mUser =  dataSnapshotusers.child("profile_name").getValue().toString();

                           // nextScreen.putExtra("name_profile", dataSnapshotusers.child("profile_name").getValue().toString());




                    final Messpriv messPriv = new Messpriv(mText,curruser,currname,mAuth.getCurrentUser().getUid(),mUser,mAuth.getCurrentUser().getPhotoUrl().toString(),"n","n","txt",dataSnapshotusers.child("profile_name").getValue().toString(),ts);


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.exists()) {

                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                    Integer child_name = key + 1;

                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).child(child_name.toString()).setValue(messPriv);
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).child(child_name.toString()).setValue(messPriv)

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                                                        //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot.hasChild("device_token")) {

                                                                sendPost(dataSnapshot.child("device_token").getValue().toString(),mAuth.getCurrentUser().getUid(), mUser, mText,"MyAction2");

                                                                Log.i("Key/DToken: ", String.valueOf(dataSnapshot.child("device_token").getValue()));

                                                            }

                                                            //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();




                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            // Getting Post failed, log a message

                                                            // ...
                                                        }
                                                    });

                                                    updateStat(ts);

                                                   // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(curruser).child(mAuth.getCurrentUser().getUid()).child("mess_count").setValue(1);



                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    // ...
                                                }
                                            }); // дубль для спросителя

                                   // FirebaseDatabase.getInstance().getReference().child("notific").child(child_name.toString()).setValue(mess);

                                }
                            }else
                            {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).child("1542770088").setValue(messPriv);
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).child("1542770088").setValue(messPriv)

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                                            //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("device_token")) {

                                                    sendPost(dataSnapshot.child("device_token").getValue().toString(),mAuth.getCurrentUser().getUid(), mUser, mText,"MyAction2");

                                                    //Log.i("Key/DToken: ", String.valueOf(dataSnapshot.child("device_token").getValue()));

                                                }

                                                //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();




                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Getting Post failed, log a message

                                                // ...
                                            }
                                        });

                                        updateStat(ts);

                                        // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(curruser).child(mAuth.getCurrentUser().getUid()).child("mess_count").setValue(1);



                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Write failed
                                                // ...
                                            }
                                        });  // дубль для спросителя

                               // FirebaseDatabase.getInstance().getReference().child("notific").child("1542770088").setValue(mess);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }});
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }});


                    // FirebaseDatabase.getInstance().getReference().child("messagesPrivate").child(ts).child("mess").setValue(message.getText().toString());
                    // FirebaseDatabase.getInstance().getReference().child("messagesPrivate").child(ts).child("page_for_comment").setValue(curruser);
                    //FirebaseDatabase.getInstance().getReference().child("messagesPrivate").child(ts).child("user").setValue(mAuth.getCurrentUser().getUid());

                    message.getText().clear();


                    // печатаем новые сообщения (после общего подгруженного общего списка)


                }




                break;


        }
    }

    public int dp(int px)
    {
        final float scale = this.getResources().getDisplayMetrics().density;

        int pixels = (int) (px * scale + 0.5f);

        return pixels;
    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public String getDatatime (String unixtime)
    {
        String restime;
        String today;
        String yestoday;

        String data;

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        java.util.Date currenTimeZone=new java.util.Date((long)Long.parseLong(unixtime)*1000);
        java.util.Date currenTimeZone2=new java.util.Date((long)tsLong*1000);


        yestoday = sdf.format(yesterday());

        today = sdf.format(currenTimeZone2);

        if (today.equals(sdf.format(currenTimeZone)))
        {
            data = "сегодня";
        }
        else if (yestoday.equals(sdf.format(currenTimeZone)))
        {
            data = "вчера";
        }
        else
        {
            data = sdf.format(currenTimeZone);
        }

        restime = data + " " + sdf2.format(currenTimeZone);

        return restime;
    }



    void UploadPicture()
    {
        final File photo = new File(filesdir,"photo.jpg");
        File photo2;

        if (photo.exists()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(filesdir + "/photo.jpg", options);
            int width = options.outWidth;
            int height = options.outHeight;

            if (width>1000) {

                int nh = 1000 * height / width;

                BitmapFactory.Options options2 = new BitmapFactory.Options();

                Bitmap mybit = BitmapFactory.decodeFile(filesdir + "/photo.jpg", options2);
                options2.inJustDecodeBounds = false;

                Bitmap resized = Bitmap.createScaledBitmap(mybit, 1000, nh, true);

                FileOutputStream fOut;
                try {
                    File small_picture = new File(filesdir, "photo.jpg");
                    fOut = new FileOutputStream(small_picture);
                    // 0 = small/low quality, 100 = large/high quality
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    resized.recycle();

                    photo2 = new File(filesdir,"photo.jpg");

                } catch (Exception e) {
                    Log.e("file err:", "Failed to save/resize image due to: " + e.toString());
                    photo2 = photo;
                }
            }
            else
            {
                photo2 = photo;
            }
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();


            photoRef = storageRef.child("pho_" + ts + ".jpg");


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

                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        Long tsLong = System.currentTimeMillis()/1000;
                        final String ts = tsLong.toString();


                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotusers) {

                                final String mUser =  dataSnapshotusers.child("profile_name").getValue().toString();



                               // final String mUser = mAuth.getCurrentUser().getDisplayName();

                        final Messpriv messPriv = new Messpriv(downloadUrl.toString(),curruser,currname,mAuth.getCurrentUser().getUid(),mUser,mAuth.getCurrentUser().getPhotoUrl().toString(),"n","n","pic",mUser,ts);



                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                        Integer child_name = key + 1;

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).child(child_name.toString()).setValue(messPriv);
                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).child(child_name.toString()).setValue(messPriv)

                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                                                            //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                if (dataSnapshot.hasChild("device_token")) {

                                                                    sendPost(dataSnapshot.child("device_token").getValue().toString(),mAuth.getCurrentUser().getUid(), mUser, "Фото", "MyAction2");

                                                                    //Log.i("Key/DToken: ", String.valueOf(dataSnapshot.child("device_token").getValue()));

                                                                }

                                                                //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();




                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                // Getting Post failed, log a message

                                                                // ...
                                                            }
                                                        });

                                                        updateStat(ts);



                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Write failed
                                                        // ...
                                                    }
                                                }); // дубль для спросителя;

                                       // FirebaseDatabase.getInstance().getReference().child("notific").child(child_name.toString()).setValue(mess);

                                    }
                                }else {
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(curruser).child(mAuth.getCurrentUser().getUid()).child("1542770088").setValue(messPriv);
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).child(curruser).child("1542770088").setValue(messPriv)



                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                                                //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.hasChild("device_token")) {

                                                        sendPost(dataSnapshot.child("device_token").getValue().toString(),mAuth.getCurrentUser().getUid(), mUser, "Фото","MyAction2");

                                                        //Log.i("Key/DToken: ", String.valueOf(dataSnapshot.child("device_token").getValue()));

                                                    }

                                                    //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();




                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message

                                                    // ...
                                                }
                                            });

                                            updateStat(ts);

                                            // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(curruser).child(mAuth.getCurrentUser().getUid()).child("mess_count").setValue(1);



                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    // ...
                                                }
                                            });  // дубль для спросителя// дубль для спросителя

                                   // FirebaseDatabase.getInstance().getReference().child("notific").child("1542770088").setValue(mess);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }});

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message

                                // ...
                            }
                        });

                        if (photo.delete()) {

                            Toast.makeText(getApplication(), "Фото успешно добавлено!", Toast.LENGTH_LONG).show();

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

        File image = new File(filesdir,fileName);

        // Save a file: path for use with ACTION_VIEW intents
        return image;
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
    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

                //Toast.makeText(getApplication(), "TEST", Toast.LENGTH_SHORT).show();

                //Log.d("Locchange: ", "WORK");

                if (isLocationEnabled(getApplicationContext()))
                {
                    //Toast.makeText(getApplication(), "ON", Toast.LENGTH_SHORT).show();

                    //Log.d("Locchange: ", "OK");

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");

                }else
                {
                    //Toast.makeText(getApplication(), "OFF", Toast.LENGTH_SHORT).show();

                    //Log.d("Locchange: ", "OFF");
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
                }

               // Log.d("Locchange: ", String.valueOf(isLocationEnabled(getApplicationContext())));

            }
        }
    };
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
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
    public void sendPost(final String registration_ids, final String extra, final String extra2, final String title, final String action) {


        //Toast.makeText(getApplication(), extra + "/" + extra2, Toast.LENGTH_SHORT).show();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");


                    conn.setRequestProperty("Authorization","key=AAAAIF01ca4:APA91bGX0kMaXMAl3QNyq_QxiRZFari8jb43cVHtktYXgKuFdmnfBzcPF1V89nNf9Otz8xY3aG0ADA5Xo9axCeijovWIlIgWKrYEEs0AYTrfPmp6sD1CDW3Y16tSsY1C5vHqdIiQfYMy");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject to = new JSONObject();

                    // Для отправки на одно устройство

                    to.put("to", registration_ids);

                    JSONObject notification = new JSONObject();

                    notification.put("click_action", action);
                    notification.put("sound", "default");
                    notification.put("icon", "mess");
                    notification.put("title", extra2);
                    notification.put("body", title);

                    JSONObject data = new JSONObject();

                    data.put("extram", extra);
                    data.put("extram2", extra2);

                  //  to.put("registration_ids",registration_ids);

                    to.put("notification",notification);

                    to.put("data",data);

                    String json = to.toString().replace("\"[", "[").replace("]\"", "]").replace("'", "\"");

                    // Log.i("JSON", json);

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                    bw.write(json);
                    bw.flush();
                    bw.close();

                    Log.i("STATUS77", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG77" , conn.getResponseMessage());

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void updateStat(final String ts){
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(curruser).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if (dataSnapshot.hasChild("mess_count")) {






                    Integer curr_messes_count = Integer.parseInt(dataSnapshot.child("mess_count").getValue().toString()); // then it has the value "4:"
                    final Integer messes_count = curr_messes_count + 1;

                    //MessStat messStat = new MessStat(messes_count.toString(),"n");


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).child("curr_activity").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Log.i("cuss:",curruser + " / " + dataSnapshot.getValue());

                            if (dataSnapshot.getValue().toString().equals("VideoActivityMess_" + curruser)) // Если удаленный юзер находится в сообщениях, то счет не открываем, чтобы галочка была "прочитано"
                            {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(curruser).child(mAuth.getCurrentUser().getUid()).setValue(new MessStat(0,"y",ts));

                            }else
                            {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(curruser).child(mAuth.getCurrentUser().getUid()).setValue(new MessStat(messes_count,"y",ts));

                            }
                            //Toast.makeText(getApplication(), dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }});






                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).child("readed").setValue("f");

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).child("currtime").setValue(ts);


                    Log.i("teststat:", dataSnapshot.getValue().toString());





                    //Log.i("teststat:", dataSnapshot.getValue().toString());
                }else
                {

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).child("curr_activity").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            //Log.i("cuss:",curruser + " / " + dataSnapshot.getValue());

                            if (dataSnapshot.getValue().toString().equals("VideoActivityMess_" + curruser)) // Если удаленный юзер находится в сообщениях, то счет не открываем, чтобы галочка была "прочитано"
                            {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(curruser).child(mAuth.getCurrentUser().getUid()).setValue(new MessStat(0,"y",ts));

                            }else
                            {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(curruser).child(mAuth.getCurrentUser().getUid()).setValue(new MessStat(1,"y",ts));

                            }
                            //Toast.makeText(getApplication(), dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }});



                  // FirebaseDatabase.getInstance().getReference().child("messagesStat").child(curruser).child(mAuth.getCurrentUser().getUid()).child("readed").setValue("y");
                   FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).child("readed").setValue("f");

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mAuth.getCurrentUser().getUid()).child(curruser).child("currtime").setValue(ts);



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

    }

    private void takeScreenshot(final String zal_user, final String zal_name) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = filesdir + "/zal.jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();










            final File zal = new File(filesdir,"zal.jpg");


            if (zal.exists()) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(filesdir + "/zal.jpg", options);



                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();


                photoRef = storageRef.child("zal_" + ts + ".jpg");


                try {

                    InputStream stream2 = new FileInputStream(zal);

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

                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Log.i("Жалоба: ", "Успешно, ссылка на жалобу - " + downloadUrl + " Жалоба на " + zal_name + " / " + zal_user);

                            Long tsLong = System.currentTimeMillis()/1000;
                            final String ts = tsLong.toString();

                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(zal_user).child("zaloba").setValue(downloadUrl.toString());





                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child("1qMMra5pItbJOtbIKcyQPHCaS7Q2").addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Log.i("tags2",dataSnapshot.child("device_token").getValue().toString());

                                    sendPost(dataSnapshot.child("device_token").getValue().toString(), zal_user, zal_name,"Жалоба","MyAction3");



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message

                                    // ...
                                }
                            });




                            if (zal.delete()) {

                                Toast.makeText(getApplication(), "Жалоба успешно добавлена!", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                } catch (Exception e) {
                    //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.d("Ошибка файла: ", e.getMessage());
                    e.printStackTrace();
                }


            }















            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}