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
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.style.ForegroundColorSpan;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        menu.findItem(R.id.friends).setTitle(new Languages().MenuFriends());
        menu.findItem(R.id.index).setTitle(new Languages().MenuIndex());
        menu.findItem(R.id.messages).setTitle(new Languages().MenuMessages());



        String title = new Languages().MenuComplain();
        if (title != null) {
            SpannableString spannable = new SpannableString(title);
            spannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#D32F2F")), // 🔴 Красный (Material Red 700)
                    0,
                    title.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            menu.findItem(R.id.zal).setTitle(spannable);
        }


        menu.findItem(R.id.about).setTitle(new Languages().MenuAbout());

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





        }

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {


        final MenuItem delphoto = menu.findItem(R.id.delphoto);
        final MenuItem banuser = menu.findItem(R.id.banuser);
        final MenuItem sendprofile = menu.findItem(R.id.sendprofile);

        Log.d("EBAY:",FirebaseAuth.getInstance().getCurrentUser().getUid());


              if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1"))

       // if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2"))
                {
                    delphoto.setVisible(true);
                    banuser.setVisible(true);
                    sendprofile.setVisible(true);

                }else
                {
                    delphoto.setVisible(false);
                    banuser.setVisible(false);
                    sendprofile.setVisible(false);
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
  //  private String filesdir =  Environment.getExternalStorageDirectory().toString() + "/Znak/files";



    public static final int REQUEST_IMAGE_CAPTURE = 777;

  //  private StorageReference storageRef;
   // private FirebaseStorage storage;
  //  private StorageReference photoRef;

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

        DatabaseReference scoresRef =  FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate);
       // scoresRef.keepSynced(true);

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

                    scoresRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

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

            case R.id.sendprofile:


                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {

                        if (dataSnapshot2.hasChild("coords")) {

                            //Log.i("coords3",dataSnapshot2.child("coords").child("lat").getValue().toString() + ", " + dataSnapshot2.child("coords").child("lng").getValue().toString());

                            final Location loc1 = new Location("");
                            loc1.setLatitude(Double.parseDouble(dataSnapshot2.child("coords").child("lat").getValue().toString()));
                            loc1.setLongitude(Double.parseDouble(dataSnapshot2.child("coords").child("lng").getValue().toString()));

                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    ArrayList subscribers = new ArrayList();

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                                        if (data.hasChild("profile_gender") && data.hasChild("coords") && data.hasChild("profile_photo")) {

                                            //Profile3 profile3 = data.getValue(Profile3.class);

                                            // Log.i("tags3",String.valueOf(data.child("profile_gender").getValue()));


                                            //if (data.getKey().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2")) {

                                            if (data.getKey().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1")) {
                                                subscribers.add(data.child("device_token").getValue());
                                            }


                                            Location loc2 = new Location("");

                                            loc2.setLatitude(Double.parseDouble(data.child("coords").child("lat").getValue().toString()));
                                            loc2.setLongitude(Double.parseDouble(data.child("coords").child("lng").getValue().toString()));

                                            float distance = loc1.distanceTo(loc2) / 1000;


                                            if (distance < 100 && data.hasChild("profile_photo") && data.child("profile_gender").getValue().equals("m")) {

                                                subscribers.add(data.child("device_token").getValue());
                                            }


                                            // Log.i("tags2", data.child("profile_name").getValue().toString() + ", " + data.child("device_token").getValue().toString());


                                            // Log.i("tags2", dataSnapshot.child("profile_name").getValue().toString() + ", " + dataSnapshot.child("device_token").getValue().toString());

                                            //sendPost(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), downloadUrl.toString(),dataSnapshot.child("device_token").getValue().toString());
                                        }
                                    }
                                    Log.i("tags2", curruser + ", " + currname + ", " + String.valueOf(subscribers.size()) + ", " + String.valueOf(subscribers));


                                   sendPost2(curruser, currname, new Languages().RassilkaLookphoto(),subscribers);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message

                                    // ...
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message

                        // ...
                    }
                });

                return  true;

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

                scoresRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

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
            case R.id.zal:// 1. Сначала скриншот (синхронно, пока экран актуален)
                View rootView = findViewById(android.R.id.content);
                File screenFile = ScreenshotUtils.captureToCache(rootView, VideoActivityMess.this);

                if (screenFile == null) {
                    Toast.makeText(this, "Не удалось сделать скриншот", Toast.LENGTH_SHORT).show();
                    return true; // Прерываем выполнение
                }

// 2. Потом асинхронно берём UID из Firebase








                //scoresRef.child(mAuth.getCurrentUser().getUid())
                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    // 🔥 Читаем UID из БД (подставьте свой вариант)
                                    String reporterUid = mAuth.getCurrentUser().getUid();
                                    String reporterName = mAuth.getCurrentUser().getDisplayName();

                                    String name = "Неизвестно";

                                    if (dataSnapshot.hasChild("name"))
                                    {
                                        name = dataSnapshot.child("name").getValue().toString();
                                    }



                                    // 3. Открываем форму с готовыми данными
                                    Intent intent = new Intent(VideoActivityMess.this, ZalobaActivity.class);
                                    intent.putExtra("screenshot_path", screenFile.getAbsolutePath());
                                    intent.putExtra("reporter_uid", reporterUid);
                                    intent.putExtra("reporter_name", reporterName);
                                    intent.putExtra("target_uid", curruser);   // из вашего кода
                                    intent.putExtra("target_name", name); // из вашего кода
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(VideoActivityMess.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(VideoActivityMess.this, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;

               // Log.i("tags","ok");

                /*

                takeScreenshot(curruser, currname);

                return true;

                 */


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

                        Uri photoURI;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
                            photoURI = FileProvider.getUriForFile(VideoActivityMess.this, "io.cordova.test2_6720b.fileprovider", photoFile);
                            Log.d("pic_e:", photoFile.toString() + " # " + getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                        }else {
                            photoURI = Uri.fromFile(photoFile);
                        }



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

        Log.d("CURR ACTIV: ","messages");

        DatabaseReference scoresRef =  FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate);
        //scoresRef.keepSynced(true);

        //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("currtime").setValue(ts2);

        if (!isLocationEnabled(getApplicationContext())) {
            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }

        // слушаем геолокацию на мобиле (включаем/отключаем), ставим в базу online/offline соответственно

        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


        // Toast.makeText(getApplication(), "HELLO!!", Toast.LENGTH_LONG).show();


      //  storage = FirebaseStorage.getInstance();

       // storageRef = storage.getReference();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dw = (int) convertPixelsToDp(size.x, getApplicationContext());

        int dw2 = (int) convertDpToPixel(size.x, getApplicationContext());

        //Log.d("информация2: ",  "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2);

        //Toast.makeText(getApplication(), "экран: " + toString().valueOf(size.x) + "Х" + toString().valueOf(size.y) + ", " + dw + ", " + dw2, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();

        // FirebaseDatabase.getInstance().getReference().child("tech").child("sendnotific").child(mAuth.getCurrentUser().getUid()).setValue(true);


        setContentView(R.layout.activity_video);



        EditText message = (EditText) findViewById(R.id.editText1);

        message.setHint(new Languages().TexteditHint());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button send = (Button) findViewById(R.id.send);
        send.setText(new Languages().ButtonSend());
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


        if (getIntent().getExtras().containsKey("extram")) {

            curruser = getIntent().getExtras().getString("extram");
            currname = getIntent().getExtras().getString("extram2");


        } else {

            curruser = getIntent().getExtras().getString("curruser");
            currname = getIntent().getExtras().getString("currname");
        }

        // Смотрим фотку если мы админы

        //if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2")) {

        final TextView textView = (TextView) findViewById(R.id.UserGeo);


            final ImageView imageView = (ImageView) findViewById(R.id.UserPho);
            //imageView.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("profile_gender"))
                {


                    if (
                            dataSnapshot.child("profile_gender").getValue().equals("m") ||
                            dataSnapshot.child("profile_gender").getValue().equals("f") ||
                           // dataSnapshot.getKey().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2")

                                    dataSnapshot.getKey().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1")


                    )
                    {


                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(curruser).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {


                                    if (dataSnapshot2.hasChild("profile_photo")) {



                                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2")) {

                                            //if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2")) {

                                            if (dataSnapshot2.hasChild("profile_lang")) {
                                                textView.setText(dataSnapshot2.child("profile_country").getValue().toString() + ", " + dataSnapshot2.child("profile_lang").getValue().toString());


                                            }else {
                                                textView.setText(dataSnapshot2.child("profile_country").getValue().toString());
                                            }
                                        }

                                        imageView.setVisibility(View.VISIBLE);


                                        Glide
                                                .with(getApplicationContext())
                                                .load(dataSnapshot2.child("profile_photo").getValue())
                                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                                //.error(R.drawable.noavatar)
                                                .into(imageView);

                                    }


                                    //Toast.makeText(getApplication(), String.valueOf(dataSnapshot.child("profile_photo").getValue()), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

              //  MessStat2 Messstat2 = new MessStat2(0,"t");

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

        scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").addListenerForSingleValueEvent(GetAllMessagesListener);

        final ArrayList<String> data = new ArrayList<String>();

        // если нет еще сообщений вносим в массив значение чтобы он отображал первое сообщение

        scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()==0)
                    data.add("тима");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").limitToLast(1).addChildEventListener(new ChildEventListener() {
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

        final DatabaseReference scoresRef =  FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate);
        //scoresRef.keepSynced(true);

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




                    final Messpriv messPriv = new Messpriv(mText,curruser,currname,mAuth.getCurrentUser().getUid(),mUser,"","n","n","txt",dataSnapshotusers.child("profile_name").getValue().toString(),ts);


                            scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Log.i("глюк2",mAuth.getCurrentUser().getUid() + " / " + curruser);

                            if (dataSnapshot.exists()) {



                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                    Integer child_name = key + 1;

                                    Log.i("глюк3",String.valueOf(child_name));

                                    scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").child(child_name.toString()).setValue(messPriv);
                                    scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").child(child_name.toString()).setValue(messPriv)

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("lmess").setValue(ts);
                                                    scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("lmess").setValue(ts);

                                                    scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("name").setValue(mUser);
                                                    scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("name").setValue(currname);

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
                                scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").child("1542770088").setValue(messPriv);
                                scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").child("1542770088").setValue(messPriv)

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("lmess").setValue(ts);
                                        scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("lmess").setValue(ts);

                                        scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("name").setValue(mUser);
                                        scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("name").setValue(currname);

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


    void UploadPicture()
    {
        final DatabaseReference scoresRef =  FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate);
       // scoresRef.keepSynced(true);

        String  filesdir;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            filesdir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        } else {
            filesdir = Environment.getExternalStorageDirectory().toString() + "/Znak/files";
        }

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



            File file = new File(photo2.getPath());

            if (!file.exists()) {
                Log.e("UPLOAD", "ФАЙЛ НЕ НАЙДЕН ПО ПУТИ: " + file.getAbsolutePath());
                return; // Останавливаем, если файла нет
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "pho_" + ts + ".jpg", requestFile);


           // photoRef = storageRef.child("pho_" + ts + ".jpg");


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Видеть всё тело запроса

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://rieltorov.net")
                    .client(client) // Добавляем логгер
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();




            ApiService service = retrofit.create(ApiService.class);


            Log.d("OTVET: ", "TEST");




            // 3. Отправка на сервер
            service.uploadImage(body).enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {


                    // Log.d("OTVET: ", response.body().status);

                    if (response.isSuccessful() && response.body() != null) {
                        String downloadUrl = response.body().url; // URL от вашего сервера



                        Log.d("Загрузка: ", "Успешно, ссылка - " + downloadUrl);


                        Long tsLong = System.currentTimeMillis()/1000;
                        final String ts = tsLong.toString();


                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotusers) {

                                final String mUser =  dataSnapshotusers.child("profile_name").getValue().toString();



                                // final String mUser = mAuth.getCurrentUser().getDisplayName();

                                final Messpriv messPriv = new Messpriv(downloadUrl.toString(),curruser,currname,mAuth.getCurrentUser().getUid(),mUser,"","n","n","pic",mUser,ts);



                                scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            Log.d("tester4",dataSnapshot + "");

                                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                Log.d("tester2",data.getKey());
                                                Integer key = Integer.parseInt(data.getKey()); // then it has the value "4:"
                                                Log.d("tester",key + "");
                                                Integer child_name = key + 1;

                                                scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").child(child_name.toString()).setValue(messPriv);
                                                scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").child(child_name.toString()).setValue(messPriv)

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
                                            scoresRef.child(curruser).child(mAuth.getCurrentUser().getUid()).child("messages").child("1542770088").setValue(messPriv);
                                            scoresRef.child(mAuth.getCurrentUser().getUid()).child(curruser).child("messages").child("1542770088").setValue(messPriv)



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

                            Toast.makeText(getApplication(), new Languages().PhotoAdded(), Toast.LENGTH_LONG).show();

                        }


                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Log.d("Ошибка загрузки: ", t.getMessage());
                }
            });


        }
    }

    private File createImageFile(String fileName) throws IOException {
        // Create an image file name

        String filesdir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            filesdir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        } else {
            filesdir = Environment.getExternalStorageDirectory().toString() + "/Znak/files";
        }

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


        Thread thread = new Thread(() -> {
            try {
                // 🔗 Адрес вашего PHP-скрипта
                URL url = new URL("https://rieltorov.net/push.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                // 🔑 Формируем POST-параметры (как ждёт ваш PHP: $_POST['...'])
                String postData = "device_token=" + URLEncoder.encode(registration_ids, "UTF-8") +
                        "&title=" + URLEncoder.encode(extra2, "UTF-8") +
                        "&body=" + URLEncoder.encode(title, "UTF-8") +
                        "&action=" + URLEncoder.encode(action, "UTF-8") +
                        "&extra=" + URLEncoder.encode(extra, "UTF-8") +
                        "&extra2=" + URLEncoder.encode(extra2, "UTF-8");

                Log.d("POST_DATA: ",postData);

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.i("PUSH_SEND", "Response code: " + responseCode);

                // Читаем ответ для отладки
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.i("PUSH_SEND", "Response: " + response.toString());

                conn.disconnect();

            } catch (Exception e) {
                Log.e("PUSH_SEND", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        thread.start();
    }
    public void sendPost2(final String extra, final String extra2, final String title, final ArrayList registration_ids) {

        //Toast.makeText(getApplication(), extra + "/" + extra2, Toast.LENGTH_SHORT).show();

        /*
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url2 = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    conn2.setRequestMethod("POST");
                    conn2.setRequestProperty("Content-Type", "application/json");


                    conn2.setRequestProperty("Authorization","key=AAAAIF01ca4:APA91bGX0kMaXMAl3QNyq_QxiRZFari8jb43cVHtktYXgKuFdmnfBzcPF1V89nNf9Otz8xY3aG0ADA5Xo9axCeijovWIlIgWKrYEEs0AYTrfPmp6sD1CDW3Y16tSsY1C5vHqdIiQfYMy");
                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);

                    JSONObject to2 = new JSONObject();
                    JSONObject notification2 = new JSONObject();

                    notification2.put("click_action", "MyAction2");
                    notification2.put("sound", "default");
                    notification2.put("icon", "mess");
                    notification2.put("title", new Languages().RassilkaNewprofile());
                    notification2.put("body", title);

                    JSONObject data2 = new JSONObject();

                    data2.put("extram", extra);
                    data2.put("extram2", extra2);

                    to2.put("registration_ids",registration_ids);

                    to2.put("notification",notification2);

                    to2.put("data",data2);

                    String json = to2.toString().replace("\"[", "[").replace("]\"", "]").replace("'", "\"");

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn2.getOutputStream(), "UTF-8"));
                    bw.write(json);
                    bw.flush();
                    bw.close();

                    Log.i("STATUS7798", String.valueOf(conn2.getResponseCode()));

                    if (conn2.getResponseCode()==200)
                    {
                        Log.i("tester4", String.valueOf(conn2.getResponseCode()));
                        Toast.makeText(getApplication(), "Анкета успешно разослана!", Toast.LENGTH_LONG).show();
                    }
                    Log.i("MSG77" , conn2.getResponseMessage());

                    conn2.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread2.start();

         */

        Thread thread = new Thread(() -> {
            try {
                // 🔗 Адрес вашего PHP-скрипта
                URL url = new URL("https://rieltorov.net/push_new_ank.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// Формируем POST-параметры
                String postData = "extra=" + URLEncoder.encode(extra, "UTF-8") +
                        "&extra2=" + URLEncoder.encode(extra2, "UTF-8") +
                        "&title=" + URLEncoder.encode(title, "UTF-8") +
                        "&registration_ids=" + URLEncoder.encode(
                        new JSONArray(registration_ids).toString(), "UTF-8");

                Log.d("POST_DATA: ",postData);

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.i("PUSH_SEND", "Response code: " + responseCode);

                // Читаем ответ для отладки
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.i("PUSH_SEND", "Response: " + response.toString());

                conn.disconnect();

            } catch (Exception e) {
                Log.e("PUSH_SEND", "Error: " + e.getMessage());
                e.printStackTrace();
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



}