package io.cordova.test2_6720b;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;


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




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        setTitle("Поиск анкет");

        toolbar.setTitleTextColor(Color.WHITE);


        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


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





                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Integer> array = new ArrayList<Integer>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    final String Key = child.getKey();

                    final String CommText = String.valueOf(child.child("name").getValue());

                    if (
                            child.child("coords").child("lat").getValue() != "0" &&
                            child.child("coords").child("lng").getValue() != "0" &&
                            !Key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            )
                    {

                        Log.i("myrecords:",String.valueOf(child.child("coords").child("lat").getValue()));


                        Location loc2 = new Location("");

                        if (child.hasChild("coords") && child.hasChild("profile_photo"))
                        {

                        loc2.setLatitude(Double.parseDouble(child.child("coords").child("lat").getValue().toString()));
                        loc2.setLongitude(Double.parseDouble(child.child("coords").child("lng").getValue().toString()));

                        float distance = loc1.distanceTo(loc2) / 1000;

                        int limited_dist;

                        //Log.i("myrecords:",String.valueOf(child.child("coords").child("lat").getValue()));

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("1qMMra5pItbJOtbIKcyQPHCaS7Q2"))
                            {
                                limited_dist = 100;
                               // limited_dist = 3000;
                            }else
                            {
                                limited_dist = 100;
                            }

                            if (distance < limited_dist && child.hasChild("profile_photo")) { // отображение всех в радиусе 100 м

                            array.add(1);


                            String profile_name;


                                    if (child.hasChild("profile_name")) {

                                        profile_name = child.child("profile_name").getValue().toString();
                                    }else
                                    {
                                        profile_name = "n/a";
                                    }


                                       String profile_photo = child.child("profile_photo").getValue().toString();


                                    String profile_age;


                                    if (child.hasChild("profile_age")) {

                                        profile_age = child.child("profile_age").getValue().toString();
                                    }else
                                    {
                                        profile_age = "n/a";
                                    }

                            String profile_country;

                            if (child.hasChild("profile_country")) {

                                profile_country = child.child("profile_country").getValue().toString();
                            }else
                            {
                                profile_country = "";
                            }

                            Log.i("currlat/lng", profile_photo + "--" + currlat + "," + currlng + "-" + child.child("coords").child("lat").getValue().toString() + "," + child.child("coords").child("lng").getValue().toString() + " /" + CommText + ": " + String.valueOf(distance));

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("name", CommText);
                            map.put("key", Key);

                            Profile3 profile3 = new Profile3(profile_name, profile_photo, profile_age, profile_country, Key);

                            mSwipeView.addView(new TinderCard(mContext, profile3, mSwipeView));


                            /*

                            if (!Key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                mylist.add(map);
                            }
                            */
                        }
                        }
                    }
                }
                // adapter.notifyDataSetChanged();

                Log.i("Пользователей: ", String.valueOf(array.size()));

                if (array.size()==0)
                {
                    TextView textView = (TextView)findViewById(R.id.nousers);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });


/*
        for(Profile profile : Utils.loadProfiles(this.getApplicationContext())){
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));

            Log.i("profile:",String.valueOf(profile.getAge()));
        }
*/
        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });



        //Log.i("curr_act:",this.getClass().getSimpleName());


/*

        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.users_list);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);




        adapter = new UsersAdapter(mylist);
        recyclerView.setAdapter(adapter);


        updateList();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

// если test = 0, то значит массив выбран поумолчани и к нему лепим переход на страницу юзера, если test = 1, значит лепим переход + добавление в подписчики

                        //Toast.makeText(getApplication(), "ПЕРЕХОД И ДОБАВЛЕНИЕ ПОДПИСЧИКА: позиция -" + position + ", ключ -" + mylist3.get(position).get("key").toString(), Toast.LENGTH_LONG).show();

                        if (test==1) {  // добавляем в подписчики (историю) если находимся в режиме фильтра, в другом случае просто переходим на страницк подписчика (друга)

                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mylist3.get(position).get("key").toString()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);

                            Intent nextScreen = new Intent(getApplication(), VideoActivity.class);

                            nextScreen.putExtra("curruser", mylist3.get(position).get("key").toString());
                            nextScreen.putExtra("currname", mylist3.get(position).get("name").toString());

                            startActivity(nextScreen);

                            finish();
                        }
                        if (test==0) {  // добавляем в подписчики (историю) если находимся в режиме фильтра, в другом случае просто переходим на страницк подписчика (друга)

                            Intent nextScreen = new Intent(getApplication(), VideoActivity.class);

                            nextScreen.putExtra("curruser", mylist.get(position).get("key").toString());
                            nextScreen.putExtra("currname", mylist.get(position).get("name").toString());

                            startActivity(nextScreen);

                            finish();
                        }

                        //Toast.makeText(getApplication(), String.valueOf(mylist3), Toast.LENGTH_LONG).show();

                    }


                    @Override public void onLongItemClick(View view, int position) {

                        // если test = 0, то при долгом нажатии удаляем

                        if (test==0) {

                            //Toast.makeText(getApplication(), "УДАЛЕНИЕ ПОДПИСЧИКА: позиция -" + position + ", ключ -" + mylist.get(position).get("key").toString(), Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mylist.get(position).get("key").toString()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
                            mylist.remove(position);
                            adapter.notifyItemRemoved(position);

                        }
                    }

                })
        );

*/
        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName());
        if (!externalAppDir.exists()) {

            if (externalAppDir.mkdir()) {

                File externalFiles = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/files");
                externalFiles.mkdir();

                File externalCache = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/cache");

                if (externalCache.mkdir())
                {
                    File externalDebug = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/cache/debug");
                    externalDebug.mkdir();

                }
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_users2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.index:

            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
            startActivity(Profile);

            finish();

            return (true);


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

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }


}