package io.cordova.test2_6720b;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;


public class UsersActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    private RecyclerView recyclerView;
    private UsersAdapter adapter;

    private int test = 0;

    private ArrayList<HashMap> mylist = new ArrayList<HashMap>();
    private ArrayList<HashMap> mylist3 = new ArrayList<HashMap>();

    private String curruser;
    private String currname;

    private boolean isToUndo = false;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("Curr admin: ", FirebaseAuth.getInstance().getUid());








        // БАня

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_banlist).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString().equals("0")) {

                        //Toast.makeText(context, "не забанен", Toast.LENGTH_LONG).show();
                    }else
                    {
                        closeNow();
                        //Toast.makeText(context, "забанен", Toast.LENGTH_LONG).show();
                    }
                }else {
                    //Toast.makeText(context, "не забанен", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});

        // БАня end

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());
        Long tsLong2 = System.currentTimeMillis() / 1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);






        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  final ImageView r_right = (ImageView) findViewById(R.id.r_right);
                //  final ImageView r_left = (ImageView) findViewById(R.id.r_left);
                mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);

                LinearLayout deletedLayout = findViewById(R.id.deleted_profile_layout);

                Log.d("GONDON22","BLJA!");

                if (dataSnapshot.hasChild("profile_delete")) {

                    mSwipeView.setVisibility(View.GONE);    // Прячем всё
                    deletedLayout.setVisibility(View.VISIBLE); // Показываем н

                    //Toast.makeText(getApplication(), "Анкета удалена", Toast.LENGTH_LONG).show();


                }else
                {
                    mSwipeView.setVisibility(View.VISIBLE);
                    deletedLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });






        setContentView(R.layout.activity_main);





        TextView restoreank = (TextView) findViewById(R.id.restore_profile_btn);


        restoreank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Путь к полю profile_delete для текущего пользователя
                FirebaseDatabase.getInstance().getReference()
                        .child(new Config2().tab_users)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("profile_delete")
                        .removeValue() // ЭТО УДАЛЯЕТ ПОЛЕ ПОЛНОСТЬЮ
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // После удаления сработает ваш ValueEventListener,
                                // и интерфейс переключится сам (или можно вызвать recreate())
                                //recreate();
                                Toast.makeText(getApplicationContext(), "Профиль восстановлен", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });




        ImageButton imbt = (ImageButton) findViewById(R.id.writeBtn);

        if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1")) imbt.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        final LinearLayout pg_layout = (LinearLayout) findViewById(R.id.pg_layout);

        final TextView seach_text = (TextView)findViewById(R.id.text_sch);

        final LinearLayout bottom_control = (LinearLayout) findViewById(R.id.bottom_control);

        //  android:text="Идет поиск анкет в Вашем радиусе.."

        seach_text.setText(new Languages().SearchText());


            setTitle(new Languages().TitleSearching());



        toolbar.setTitleTextColor(Color.WHITE);


        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(new Languages().SwipeNext())
                        .setSwipeOutMsgLayoutId(new Languages().SwipeNext()));


       // final String currlng = getIntent().getExtras().getString("currlng");
       // final String currlat = getIntent().getExtras().getString("currlat");



        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("subscribers/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("thiscoords:", dataSnapshot.child("lat").getValue().toString() + ", user: " +FirebaseAuth.getInstance().getCurrentUser().getUid());

                final String currlng = dataSnapshot.child("lng").getValue().toString();
                final String currlat = dataSnapshot.child("lat").getValue().toString();


                final Location loc1 = new Location("");
                loc1.setLatitude(Double.parseDouble(currlat));
                loc1.setLongitude(Double.parseDouble(currlng));


                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {

                        String curr_sex;

                        // Log.i("sex:", dataSnapshot2.getKey() + "/");


                        final String Sex = dataSnapshot2.child("profile_gender").getValue().toString();

                        if (Sex.equals("m")) {
                            curr_sex = "f";
                        } else
                            curr_sex = "m";

                        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).addListenerForSingleValueEvent(new ValueEventListener() {
// moder

                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1")) {








                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("last_mess").limitToLast(3000).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    List<Integer> array = new ArrayList<Integer>();

                                    //final ArrayList catNames = new ArrayList();


                                    ArrayList<String> catNames;
                                    catNames = new ArrayList<String>();

                                    HashMap<String, String> M_profile_photo = new HashMap<>();
                                    HashMap<String, String> M_profile_country = new HashMap<>();
                                    HashMap<String, String> M_profile_name = new HashMap<>();
                                    HashMap<String, String> M_profile_age = new HashMap<>();
                                    HashMap<String, String> M_profile_likes = new HashMap<>();
                                    HashMap<String, String> M_device_token = new HashMap<>();
                                    HashMap<String, String> M_profile_gender = new HashMap<>();

                                    Log.d("PIDR","HUYNJA");

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        final String Key = child.getKey();

                                            if (child.hasChild("profile_photo") && !child.hasChild("profile_delete")) {

                                                    array.add(1);

                                                    final String device_token;

                                                    if (child.hasChild("device_token")) {
                                                        device_token = child.child("device_token").getValue().toString();
                                                    } else {
                                                        device_token = "";
                                                    }

                                                    final String profile_likes;

                                                    if (child.hasChild("likes")) {
                                                        profile_likes = child.child("likes").getValue().toString();
                                                    } else {
                                                        profile_likes = "0";
                                                    }

                                                    final String profile_name;


                                                    if (child.hasChild("profile_name")) {

                                                        profile_name = child.child("profile_name").getValue().toString();
                                                    } else {
                                                        profile_name = "n/a";
                                                    }


                                                    final String profile_photo = child.child("profile_photo").getValue().toString();
Log.d("GONFDO",profile_photo + ", " + Key);

                                                    final String profile_age;


                                                    if (child.hasChild("profile_age")) {

                                                        profile_age = child.child("profile_age").getValue().toString();
                                                    } else {
                                                        profile_age = "n/a";
                                                    }

                                                    final String profile_country;

                                                    if (child.hasChild("profile_country")) {

                                                        profile_country = child.child("profile_country").getValue().toString();

                                                    } else {
                                                        profile_country = "";
                                                    }
                                                    String profile_gender;

                                                    if (child.hasChild("profile_gender")) {

                                                        profile_gender = child.child("profile_gender").getValue().toString();
                                                    } else {
                                                        profile_gender = "";
                                                    }

                                                        M_profile_photo.put(Key, profile_photo);
                                                        M_profile_country.put(Key, profile_country);
                                                        M_profile_name.put(Key, profile_name);
                                                        M_profile_age.put(Key, profile_age + " " + Key);
                                                        M_profile_likes.put(Key, profile_likes);
                                                        M_device_token.put(Key, device_token);
                                                        M_profile_gender.put(Key, profile_gender);


                                                        catNames.add(Key);



                                            }
                                    }

                                    Collections.reverse(catNames);

                                    String a_age;

                                    Integer i;
                                    int next_object;

                                    for (String object : catNames) {


                                        i = catNames.indexOf(object) + 1;


                                        next_object = catNames.size();

                                            a_age = M_profile_age.get(object);


                                        int first_object = i;

                                        Profile3 profile3 = new Profile3(M_profile_name.get(object), M_profile_photo.get(object), a_age, M_profile_country.get(object), M_profile_likes.get(object), M_device_token.get(object), M_profile_gender.get(object), object, next_object, first_object);
                                        mSwipeView.addView(new TinderCard(mContext, profile3, mSwipeView));


                                    }
                                    pg_layout.setVisibility(View.GONE);
                                    bottom_control.setVisibility(View.VISIBLE);

                                    Log.d("VSEGO_USERS:", "->" + array.size());


                                    if (catNames.size() == 0) {
                                        LinearLayout control_b = findViewById(R.id.bottom_control);
                                        control_b.setVisibility(View.GONE);

                                        TextView textView = (TextView) findViewById(R.id.nousers);
                                        textView.setText(new Languages().NoUsers());
                                        textView.setVisibility(View.VISIBLE);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message

                                    // ...
                                }
                            });




























                        }else {

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("last_mess").limitToLast(3000).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                List<Integer> array = new ArrayList<Integer>();

                                //final ArrayList catNames = new ArrayList();


                                ArrayList<String> catNames;
                                catNames = new ArrayList<String>();

                                HashMap<String, String> M_profile_photo = new HashMap<>();
                                HashMap<String, String> M_profile_country = new HashMap<>();
                                HashMap<String, String> M_profile_name = new HashMap<>();
                                HashMap<String, String> M_profile_age = new HashMap<>();
                                HashMap<String, String> M_profile_likes = new HashMap<>();
                                HashMap<String, String> M_device_token = new HashMap<>();
                                HashMap<String, String> M_profile_gender = new HashMap<>();

                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                    final String Key = child.getKey();

                                    final String CommText = String.valueOf(child.child("name").getValue());

                                    if (
                                            child.child("coords").child("lat").getValue() != "0" &&
                                                    child.child("coords").child("lng").getValue() != "0" &&
                                                    !Key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    ) {

                                        //Log.i("myrecords:",String.valueOf(child.child("coords").child("lat").getValue()));


                                        Location loc2 = new Location("");

                                        if (child.hasChild("coords") && child.hasChild("profile_photo")) {
                                            //loc2.setLatitude(77.00);
                                            // loc2.setLongitude(77.00);


                                            loc2.setLatitude(Double.parseDouble(child.child("coords").child("lat").getValue().toString()));
                                            loc2.setLongitude(Double.parseDouble(child.child("coords").child("lng").getValue().toString()));

                                            float distance = loc1.distanceTo(loc2) / 1000;

                                            int limited_dist;

                                            //Log.i("myrecords:",String.valueOf(child.child("coords").child("lat").getValue()));

                                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2")) {
                                                limited_dist = 100;
                                                // limited_dist = 3000;
                                            } else {
                                                limited_dist = 100;
                                            }

                                            // главное условие
// условие 2
                                            if (distance < limited_dist && child.hasChild("profile_photo") && !child.hasChild("profile_delete") ) { // отображение всех в радиусе 100 м

                                                //Log.d("GONDON:",Key);

                                                array.add(1);

                                                final String device_token;

                                                if (child.hasChild("device_token")) {
                                                    device_token = child.child("device_token").getValue().toString();
                                                } else {
                                                    device_token = "";
                                                }

                                                final String profile_likes;

                                                if (child.hasChild("likes")) {
                                                    profile_likes = child.child("likes").getValue().toString();
                                                } else {
                                                    profile_likes = "0";
                                                }

                                                final String profile_name;


                                                if (child.hasChild("profile_name")) {

                                                    profile_name = child.child("profile_name").getValue().toString();
                                                } else {
                                                    profile_name = "n/a";
                                                }


                                                final String profile_photo = child.child("profile_photo").getValue().toString();


                                                final String profile_age;


                                                if (child.hasChild("profile_age")) {

                                                    profile_age = child.child("profile_age").getValue().toString();
                                                } else {
                                                    profile_age = "n/a";
                                                }

                                                final String profile_country;

                                                if (child.hasChild("profile_country")) {

                                                    profile_country = child.child("profile_country").getValue().toString();

                                                } else {
                                                    profile_country = "";
                                                }
                                                String profile_gender;

                                                if (child.hasChild("profile_gender")) {

                                                    profile_gender = child.child("profile_gender").getValue().toString();
                                                } else {
                                                    profile_gender = "";
                                                }

                                                //  Log.i("currlat/lng", profile_photo + "--" + currlat + "," + currlng + "-" + child.child("coords").child("lat").getValue().toString() + "," + child.child("coords").child("lng").getValue().toString() + " /" + CommText + ": " + String.valueOf(distance));

                                                //  Log.i("sex:", Sex + "/" + profile_gender + " / " + Key);


                                                //  for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                //  HashMap<String, String> map = new HashMap<String, String>();

                                                if (!Sex.equals(profile_gender)) {


                                                    M_profile_photo.put(Key, profile_photo);
                                                    M_profile_country.put(Key, profile_country);
                                                    M_profile_name.put(Key, profile_name);
                                                    M_profile_age.put(Key, profile_age);
                                                    M_profile_likes.put(Key, profile_likes);
                                                    M_device_token.put(Key, device_token);
                                                    M_profile_gender.put(Key, profile_gender);


                                                    catNames.add(Key);

                                                    // map.put("name", CommText);
                                                    // map.put("key", Key);

                                                    // Profile3 profile3 = new Profile3(profile_name, profile_photo, profile_age, profile_country, Key);

                                                    //mSwipeView.addView(new TinderCard(mContext, profile3, mSwipeView));
                                                }


                                            }
                                        }
                                    }
                                }

                                Log.d("mass_size2:", String.valueOf(catNames.size()));

                                //Log.d("MyAndroidClass", Arrays.toString(catNames));

                                // https://stackoverflow.com/questions/4401850/how-to-create-a-multidimensional-arraylist-in-java

                                Collections.reverse(catNames);

                                String a_age;

                                Integer i;
                                int next_object;

                                for (String object : catNames) {


                                    i = catNames.indexOf(object) + 1;


                                    next_object = catNames.size();


                                    Log.i("my_arr2: ", String.valueOf(object) + " / " + catNames.indexOf(object) + "/" + i + " / " + next_object + " / " + catNames.get(0));

                                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1")) {

                                        a_age = M_profile_age.get(object);

                                        //  a_age =  M_profile_age.get(object) + " " + object;
                                    } else {
                                        a_age = M_profile_age.get(object);
                                    }

                                    int first_object = i;

                                    Profile3 profile3 = new Profile3(M_profile_name.get(object), M_profile_photo.get(object), a_age, M_profile_country.get(object), M_profile_likes.get(object), M_device_token.get(object), M_profile_gender.get(object), object, next_object, first_object);
                                    mSwipeView.addView(new TinderCard(mContext, profile3, mSwipeView));


                                }
                                pg_layout.setVisibility(View.GONE);
                                bottom_control.setVisibility(View.VISIBLE);

                                // adapter.notifyDataSetChanged();

                                Log.i("Пользователей: ", String.valueOf(array.size()));

                                if (catNames.size() == 0) {
                                    LinearLayout control_b = findViewById(R.id.bottom_control);
                                    control_b.setVisibility(View.GONE);

                                    TextView textView = (TextView) findViewById(R.id.nousers);
                                    textView.setText(new Languages().NoUsers());
                                    textView.setVisibility(View.VISIBLE);
                                }

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSwipeView.doSwipe(false);

               // Toast.makeText(getApplicationContext(), String.valueOf("PIDR"), Toast.LENGTH_LONG).show();
             //   mSwipeView.onResetView(SwipeViewBinder.class);

                //mSwipeView.undoLastSwipe();
            }
        });
        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                if (isToUndo) {
                    isToUndo = false;
                    mSwipeView.undoLastSwipe();
                }
            }
        });
        findViewById(R.id.writeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View toWrite = findViewById(R.id.towrite);
                if (toWrite!=null) {
                    toWrite.performClick();
                }
            }
        });
        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSwipeView.doSwipe(true);

              // Toast.makeText(mContext, String.valueOf(mSwipeView.isClickable()), Toast.LENGTH_SHORT).show();

            //   mSwipeView.addView();

                View myView = findViewById(R.id.s_like);

                if (myView!=null)
                {
                    myView.performClick();
                }
            }
        });

        //Log.i("curr_act:",this.getClass().getSimpleName());


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

           getMenuInflater().inflate(R.menu.menu_users2, menu);
        menu.findItem(R.id.friends).setTitle(new Languages().MenuFriends());
        menu.findItem(R.id.index).setTitle(new Languages().MenuIndex());
        menu.findItem(R.id.messages).setTitle(new Languages().MenuMessages());
        menu.findItem(R.id.likes).setTitle(new Languages().MenuLikes());
        menu.findItem(R.id.about).setTitle(new Languages().MenuAbout());



        Log.i("curr_lang", Locale.getDefault().getLanguage());

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.index:

            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
            startActivity(Profile);

            finish();

            return (true);

        case R.id.whatshot:
            Intent usersScreen2 = new Intent(getApplication(), UsersActivity.class);
            startActivity(usersScreen2);

        case R.id.friends:

            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
            startActivity(usersScreen);


            //Toast.makeText(this, "Аватарка обновлена", Toast.LENGTH_LONG).show();
            return true;


        case R.id.messages:

            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    // Log.d("LAT/LNG: ", String.valueOf(dataSnapshot.getKey()));

                        Intent allmess = new Intent(getApplication(), VideoActivityAllMess.class);

                        //allmess.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        //allmess.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                        startActivity(allmess);

                        finish();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            return true;

        case R.id.likes:

                    Intent likes = new Intent(getApplication(), ActivityLikes.class);

                    //allmess.putExtra("curruser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //allmess.putExtra("currname", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    startActivity(likes);

                    finish();

            return true;

        case R.id.about:

            Functions functions = new Functions();

            functions.About(UsersActivity.this);

            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    /*

private void updateList(){

    final String currlng = getIntent().getExtras().getString("currlng");
    final String currlat = getIntent().getExtras().getString("currlat");


    final Location loc1 = new Location("");
    loc1.setLatitude(Double.parseDouble(currlat));
    loc1.setLongitude(Double.parseDouble(currlng));




   // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).orderByChild("subscribers/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {

    FirebaseDatabase.getInstance().getReference().child("users6").addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot child : dataSnapshot.getChildren()) {

                final String Key = child.getKey();

                final String CommText = String.valueOf(child.child("name").getValue());

                if (child.child("coords").child("lat").getValue()!="0") {



                    Location loc2 = new Location("");

                    loc2.setLatitude(Double.parseDouble(child.child("coords").child("lat").getValue().toString()));
                    loc2.setLongitude(Double.parseDouble(child.child("coords").child("lng").getValue().toString()));

                    float distance = loc1.distanceTo(loc2)/1000;

                    //Log.i("myrecords:",String.valueOf(child.child("coords").child("lat").getValue()));

                    if (distance<100) { // отображение всех в радиусе 100 м

                        Log.i("currlat/lng", currlat + "," + currlng + "-" + child.child("coords").child("lat").getValue().toString() + "," + child.child("coords").child("lng").getValue().toString() + " /" + CommText + ": " + String.valueOf(distance));

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("name", CommText);
                        map.put("key", Key);

                        if (!Key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            mylist.add(map);
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message

            // ...
        }
    });


}
*/

    public int dp(int px)
    {
        final float scale = this.getResources().getDisplayMetrics().density;

        int pixels = (int) (px * scale + 0.5f);

        return pixels;
    }




    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }



    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }


}