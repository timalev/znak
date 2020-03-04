package io.cordova.test2_6720b;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {


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

    SharedPreferences sPref;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        String curruserM;


        if (getIntent().getExtras().containsKey("extra")) {

            curruserM = getIntent().getExtras().getString("extra");


        }
        else {

            curruserM = getIntent().getExtras().getString("curruser");

        }
        Log.d("curruserM: ", curruserM);

        //item.setEnabled(false);


        //drawable.setColorFilter(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled), PorterDuff.Mode.SRC_ATOP);





        for(int i = 0; i < menu.size(); i++){

            if (menu.getItem(i).toString().equals("написать письмо"))
            {
                MenuItem item=menu.getItem(i);

                if (FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(curruserM)) {

                    item.setVisible(false);
                }else
                {
                    item.setVisible(true);
                }
            }

            if (menu.getItem(i).toString().equals("показать на карте"))
            {
                final MenuItem item=menu.getItem(i);
                final Drawable drawable = item.getIcon();

                item.setEnabled(false);
                drawable.setAlpha(130);

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
                       // Log.d("Status: ", String.valueOf(dataSnapshot.getValue()));

                        if (dataSnapshot.getValue()!=null) {

                            if (dataSnapshot.getValue().equals("online")) {

                                item.setEnabled(true);
                                drawable.setAlpha(255);

                                if (!isMyServiceRunning(GeoService.class)) {

                                    Intent serviceIntent2 = new Intent(getApplicationContext(), GeoService.class);
                                    getApplication().startService(serviceIntent2);
                                }

                            } else {
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


/*
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {


                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled), PorterDuff.Mode.SRC_ATOP);
            }
            */
        }

        return true;

    }

    private void initiatePopupWindow() {
        try {


            View convertView = getLayoutInflater().inflate(R.layout.popap, null);
            // ImageButton CloBtn = (ImageButton) convertView.findViewById(R.id.clo);
            Button CloBtn = (Button) convertView.findViewById(R.id.clo);

            // final PopupWindow popUpWindow = new PopupWindow(convertView,400,550,false);

            final PopupWindow popUpWindow = new PopupWindow(convertView, getResources().getInteger(R.integer.popup_height),getResources().getInteger(R.integer.popup_width),false);

            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.relLayout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            Toast.makeText(getApplication(), "Не верный формат видео", Toast.LENGTH_SHORT).show();

            return true;
        }
    };




    //private String filesdir = "Android/data/io.cordova.bizone2/files";
    private String filesdir =  Environment.getExternalStorageDirectory().toString() + "/Android/data/io.cordova.bizone2/files";


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

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());

        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);



// Создаем директорию для фото

        /*
        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName());
        if (!externalAppDir.exists()) {
            //Toast.makeText(this, "Папки НЕТ!", Toast.LENGTH_SHORT).show();

            externalAppDir.mkdir();
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

        // если на мобиле отключена геолокация ставим offline

        if (!isLocationEnabled(getApplicationContext())) {
            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }

        // слушаем геолокацию на мобиле (включаем/отключаем), ставим в базу online/offline соответственно

        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


        if (getIntent().getExtras().containsKey("extra")) {

            curruser = getIntent().getExtras().getString("extra");
            currname = getIntent().getExtras().getString("extra2");

        }
        else {

            curruser = getIntent().getExtras().getString("curruser");
            currname = getIntent().getExtras().getString("currname");
        }

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




            storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dw =  (int)convertPixelsToDp(size.x, getApplicationContext());

        int dw2 = (int)convertDpToPixel(size.x, getApplicationContext());

        //Log.d("информация: ",  "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2);

        //Toast.makeText(getApplication(), "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //GeoBtn.setOnClickListener(this);

        // final ImageButton Button3 = (ImageButton) findViewById(R.id.Button3);
        // Button3.setOnClickListener(this);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);


        // LinearLayout ll = (LinearLayout) findViewById(R.id.videolay);

        // ll.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        mPostReference = FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users);

        LocationManager lm = (LocationManager)getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gps_enabled) {
                mPostReference.child(mAuth.getCurrentUser().getUid()).child("status").setValue("offline");

            }
        }
        catch(Exception ex) {

            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();

        }

        VideoText = (TextView) findViewById(R.id.text_view_id);


        // VideoText.setText(R.string.hello);

/*
        videoView = (VideoView) findViewById(R.id.video_id);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
*/

        numbersList = (RecyclerView) findViewById(R.id.rv_numbers);
        numbersList.setLayoutManager(new LinearLayoutManager(this));



        list = new ArrayList<Profile2>();



        ValueEventListener GetAllMessagesListener = new ValueEventListener() {


            //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                   // Отображение через адаптер

                    Profile2 p = child.getValue(Profile2.class);
                    list.add(p);

                    // Отображение через адаптер end

/*

                    String CommText = String.valueOf(child.child("mess").getValue());

                    String CommType = String.valueOf(child.child("type").getValue());


                    switch (CommType) {

                        case "pic":

                            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(100, 100);


                            //LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                            ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setLayoutParams(params2);
                            imageView.setImageResource(R.drawable.pho); //
                            imageView.setRotation(90);

                            linearLayout.addView(imageView, params2);

                            Glide
                                    .with(getApplicationContext())
                                    .load(CommText)
                                    .into(imageView);

                            break;

                        default:

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            // Log.d("COUNT: ", child.getKey() + ", mess: " + child.child("mess").getValue() + ", COUNT:--------------------" + dataSnapshot2.getChildrenCount());

                            TextView quote = new TextView(getApplicationContext());
                            params.setMargins(dp(10), dp(10), dp(10), dp(1));
                            quote.setPadding(dp(10), dp(10), dp(10), dp(1));    //left,top,right,bottom
                            quote.setText(CommText);
                            quote.setTextColor(Color.rgb(0, 0, 0));
                            linearLayout.addView(quote, params);

                    }

                    TextView quote3 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    params3.setMargins(dp(10), dp(1), dp(10), dp(10));
                    quote3.setPadding(dp(10), dp(1), dp(10), dp(10));    //left,top,right,bottom

                    // String unixtime = child.getKey();

                    String data = getDatatime(child.getKey());

                    // sdf.format(currenTimeZone)

                    quote3.setText(child.child("name").getValue() + " " + data);
                    quote3.setTextColor(Color.rgb(0, 0, 0));
                    //quote3.setGravity(Gravity.RIGHT);
                    linearLayout.addView(quote3, params3);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
                    View v = new View(getApplicationContext());
                    v.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    linearLayout.addView(v, params2);

                    */



                }


                //Toast.makeText(getApplication(), String.valueOf(list.size()), Toast.LENGTH_SHORT).show();


                numbersList.scrollToPosition(list.size()-1);

                adapter = new numbersAdapter(VideoActivity.this,list);

                numbersList.setAdapter(adapter);



                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);
/*
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.myscroll));

                scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //replace this line to scroll up or down
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100L);
                */

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };




        // Подгружаем все оообщения юзеров

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByChild("page_for_comment").equalTo(curruser).addListenerForSingleValueEvent(GetAllMessagesListener);



        final ArrayList<String> data = new ArrayList<String>();

        // если нет еще сообщений вносим в массив значение чтобы он отображал первое сообщение

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByChild("page_for_comment").equalTo(curruser).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()==0)
                    data.add("тима");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByChild("page_for_comment").equalTo(curruser).limitToLast(1).addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {


                // if (mAuth.getCurrentUser().getUid()!=user)

                //Toast.makeText(getApplication(), mAuth.getCurrentUser().getUid() + ", " + user, Toast.LENGTH_LONG).show();


                // печатаем сообщения только если в массиве есть данные, иначе он выведет последнее введенное

                if (data.size() > 0) {



                    // чтобы себе не слать пуши

                    /*

                    if (mAuth.getCurrentUser().getUid()!=user) {


                        final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent notificationIntent = new Intent(context, VideoActivity.class);
                        notificationIntent.putExtra("NotificationMessage", page_for_comment);
                        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // устанавливаем объект PendingIntent, большую иконку, заголовки и контент

                        Glide.with(context)

                                .load(avatar)  // грузим фото на которое заменим маркер
                                .asBitmap()  // переводим его в нужный формат
                                .fitCenter()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                                        // xml-сборка страницы маркеров, шаблон в файле cluster_view.xml

                                        BitmapDescriptor icon3 = BitmapDescriptorFactory.fromBitmap(bitmap); // готовый маркер, можно одного его подгрузить


                                        android.support.v4.app.NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(context)
                                                        .setSmallIcon(R.drawable.mess)
                                                        .setLargeIcon(bitmap)
                                                        .setWhen(System.currentTimeMillis())
                                                        .setContentTitle("Новое сообщение")
                                                        .setTicker("Новое сообщение от " + name)
                                                        .setContentText(CommText)
                                                        .setNumber(1)
                                                        .setVibrate(new long[]{1000, 1000})
                                                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                                        .setContentIntent(contentIntent)
                                                        .setAutoCancel(true);

                                        mNotificationManager.notify("App Name", 228, mBuilder.build());

                                    }
                                });
                    }
                    */


                    // Toast.makeText(getApplication(), "Размер таблицы:" + String.valueOf(list.size()), Toast.LENGTH_LONG).show();



                    Profile2 p = snapshot.getValue(Profile2.class);
                    list.add(p);


                    numbersList.scrollToPosition(list.size()-1);

                    adapter = new numbersAdapter(VideoActivity.this,list);

                    adapter.notifyItemInserted(list.size()-1);

                   // numbersList.setAdapter(adapter);

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

            case R.id.send:

                //Toast.makeText(this, curruser, Toast.LENGTH_LONG).show();

                Intent nextScreen = new Intent(getApplication(), VideoActivityMess.class);

                nextScreen.putExtra("curruser", curruser);
                nextScreen.putExtra("currname", currname);

                startActivity(nextScreen);

                finish();


               return true;
/*
            case R.id.mapmode:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Log.d("LAT/LNG: ", String.valueOf(dataSnapshot.child("coords/lat").getValue()));
                        Intent prevScreen = new Intent(getApplicationContext(),MapsActivity.class);

                        prevScreen.putExtra("videolat", String.valueOf(dataSnapshot.child("coords/lat").getValue()));
                        prevScreen.putExtra("videolng", String.valueOf(dataSnapshot.child("coords/lng").getValue()));

                        prevScreen.putExtra("curruser", curruser);
                        prevScreen.putExtra("currname", currname);
                        prevScreen.putExtra("activity", "videoactivity");


                        startActivity(prevScreen);

                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                return true;

                */
/*
            case R.id.myplace:


                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mAuth.getCurrentUser().getUid()).child("status").addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        //  Log.d("status: ", String.valueOf(dataSnapshot.getValue()));

if (dataSnapshot.getValue()!=null)
{

                        if (dataSnapshot.getValue().equals("online")) {


                            mPostReference.child(mAuth.getCurrentUser().getUid()).child("status").setValue("offline");

                        }
                        else {
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
                                    displayPromptForEnablingGPS(VideoActivity.this);
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
            case R.id.about:

                initiatePopupWindow();

                //Toast.makeText(this, "О приложении", Toast.LENGTH_LONG).show();
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
*/

            case R.id.index:
                Intent toIndex = new Intent(getApplication(), VideoActivity.class);

                toIndex.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                toIndex.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                startActivity(toIndex);

                finish();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void onClick(final View v){
        switch (v.getId()) {

            case R.id.send:


                final LinearLayout linearLayout =(LinearLayout) this.findViewById(R.id.linearlayout1);

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                final EditText message = (EditText) findViewById(R.id.editText1);
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.myscroll));

// мотаем скролл в конец

                scrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace this line to scroll up or down
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 100L);

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
                    inputManager.hideSoftInputFromWindow(message.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    final String mUser = mAuth.getCurrentUser().getDisplayName();
                    final String mText = message.getText().toString();


                    final Mess mess = new Mess(mText,curruser,mAuth.getCurrentUser().getUid(),mUser,mAuth.getCurrentUser().getPhotoUrl().toString(),"txt",ts);

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                    Integer child_name = key + 1;


                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).child(child_name.toString()).setValue(mess)

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("subscribers/" + curruser).equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                                //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    ArrayList subscribers = new ArrayList();


                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                        String Key = child.getKey();
                                                        String Device_token = String.valueOf(child.child("device_token").getValue());

                                                        if (child.child("device_token").getValue()!=null) {

                                                            subscribers.add("'" + Device_token + "'");
                                                        }
                                                       Log.i("LAT/LNG: ", Key);

                                                    }
                                                    //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();
                                                    sendPost(subscribers,curruser, mUser, mText);



                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message

                                                    // ...
                                                }
                                            });


                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    // ...
                                                }
                                            });
                                }
                            }else {
                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).child("1").setValue(mess);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }});


                    // FirebaseDatabase.getInstance().getReference().child("messages").child(ts).child("mess").setValue(message.getText().toString());
                    // FirebaseDatabase.getInstance().getReference().child("messages").child(ts).child("page_for_comment").setValue(curruser);
                    //FirebaseDatabase.getInstance().getReference().child("messages").child(ts).child("user").setValue(mAuth.getCurrentUser().getUid());

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
        // String ts = tsLong.toString();

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

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();

                        final String mUser = mAuth.getCurrentUser().getDisplayName();

                        final Mess mess = new Mess(downloadUrl.toString(),curruser,mAuth.getCurrentUser().getUid(),mUser,mAuth.getCurrentUser().getPhotoUrl().toString(),"pic",ts);




                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        //if you call methods on dataSnapshot it gives you the required values

                                        Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                        Integer child_name = key + 1;

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).child(child_name.toString()).setValue(mess).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                // После того как вставили данные ищем подписчиков данной страницы (curruser) и найденным подписчикам рассылаем сообщения о новом сообении

                                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("subscribers/" + curruser).equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                                    //  FirebaseDatabase.getInstance().getReference().child("messages").orderByChild("page_for_comment").equalTo(curruser).limitToFirst((int) (dataSnapshot2.getChildrenCount() - 1)).addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        ArrayList subscribers = new ArrayList();

                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                            String Key = child.getKey();
                                                            String Device_token = String.valueOf(child.child("device_token").getValue());

                                                            if (child.child("device_token").getValue()!=null) {

                                                                subscribers.add("'" + Device_token + "'");
                                                            }
                                                            //Log.i("LAT/LNG: ", Key);

                                                        }
                                                        //Toast.makeText(getApplication(), String.valueOf(subscribers.size()), Toast.LENGTH_SHORT).show();
                                                        //sendPost(subscribers);

                                                        sendPost(subscribers,curruser, mUser, "Фото");

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        // Getting Post failed, log a message
                                                    }
                                                });

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Write failed
                                                        // ...
                                                    }
                                                });


                                        //Toast.makeText(getApplication(), child_name.toString(), Toast.LENGTH_LONG).show();

                                    }
                                }else
                                {
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messages).child("1").setValue(mess);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }});



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

                if (isLocationEnabled(getApplicationContext()))
                {
                    //Log.d("Locchange: ", "OK");

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");

                }else
                {
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
    public void sendPost(final ArrayList registration_ids, final String extra, final String extra2, final String title) {

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

                       // to.put("to", "e8sAdRIsnBw:APA91bHBRlzXznqvnxhzfG-Bmy59Kd0k52mvNFQPGwe4DJdU6Ks_3seZ9FpoqHL-bMfZ5QdYKdvRbf7iW6CpG5HyFaYVckifySJjgD6_UwXyNOyobg8_TZtXl0ZDJkpw8Z-UKPXsjt9Y");

                    JSONObject notification = new JSONObject();

                        notification.put("click_action", "MyAction");
                        notification.put("sound", "default");
                        notification.put("icon", "mess");
                        notification.put("title", extra2);
                        notification.put("body", title);

                    JSONObject data = new JSONObject();

                        data.put("extra", extra);
                        data.put("extra2", extra2);

                        to.put("registration_ids",registration_ids);

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

}
