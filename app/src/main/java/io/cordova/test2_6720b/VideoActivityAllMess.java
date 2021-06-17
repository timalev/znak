package io.cordova.test2_6720b;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VideoActivityAllMess extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //private String curruser;
    //private String currname;

    private RecyclerView recyclerView;
    private MessesAdapter adapter;

    private ArrayList<HashMap> mylist = new ArrayList<HashMap>();

    private HashMap<String, String> hashCount = new HashMap<String, String>();


    String name_var = "";
    String user_var = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());

        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);

        //curruser = getIntent().getExtras().getString("curruser");
        //currname = getIntent().getExtras().getString("currname");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


            setTitle(new Languages().TitleMymessages());


        toolbar.setTitleTextColor(Color.WHITE);


        recyclerView = (RecyclerView) findViewById(R.id.messes_list);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        adapter = new MessesAdapter(mylist);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

       // final LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearlayout1);

        final ValueEventListener GetAllMessagesListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!String.valueOf(dataSnapshot.getValue()).equals("null")) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        final String Key = child.getKey();

                        //Log.d("Snap38: ", String.valueOf(Key));

                        String name = dataSnapshot.child(child.getKey()).child("name").getValue().toString();

                        final HashMap<String, String> map = new HashMap<String, String>();

                        map.put("name", name);
                        map.put("key", Key);

                        mylist.add(map);

                        // Log.d("COUNT4: ",Key  + "/" +name);

                    }
                    Collections.reverse(mylist);
                    adapter.notifyDataSetChanged();


                }else
                {

                    TextView nomessages = (TextView) findViewById(R.id.nomessages);


                        nomessages.setText(new Languages().NoMessages());


                    nomessages.setVisibility(View.VISIBLE);

                    //Toast.makeText(getApplication(), "Нет мессаджей!", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        // Toast.makeText(this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).orderByChild("lmess").addListenerForSingleValueEvent(GetAllMessagesListener);
        //  FirebaseDatabase.getInstance().getReference().child("users6").orderByChild("subscribers/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).equalTo(1).addListenerForSingleValueEvent(GetAllMessagesListener);


        recyclerView.addOnItemTouchListener(
                new UsersActivity.RecyclerItemClickListener(this, recyclerView ,new UsersActivity.RecyclerItemClickListener.OnItemClickListener() {


                    @Override public void onItemClick(View view, int position) {

// если test = 0, то значит массив выбран поумолчани и к нему лепим переход на страницу юзера, если test = 1, значит лепим переход + добавление в подписчики

                        //Toast.makeText(getApplication(), "ПЕРЕХОД И ДОБАВЛЕНИЕ ПОДПИСЧИКА: позиция -" + position + ", ключ -" + mylist3.get(position).get("key").toString(), Toast.LENGTH_LONG).show();

                             Intent nextScreen = new Intent(getApplication(), VideoActivityMess.class);
// Здесь
                            nextScreen.putExtra("curruser", mylist.get(position).get("key").toString());
                            nextScreen.putExtra("currname", mylist.get(position).get("name").toString());

                            startActivity(nextScreen);

                            finish();



                        //Toast.makeText(getApplication(), mylist.get(position).get("name").toString(), Toast.LENGTH_LONG).show();

                    }


                    @Override public void onLongItemClick(View view, final int position) {


                      //  Log.i("stiraem","1." + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" +mylist.get(position).get("key").toString() + " 2." +mylist.get(position).get("key").toString() + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mylist.get(position).get("key").toString()).removeValue()

                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mylist.get(position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()

                                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mylist.get(position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()

                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mylist.get(position).get("key").toString()).removeValue()

                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(mylist.get(position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()

                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {


                                                                                                        mylist.remove(position);
                                                                                                        adapter.notifyItemRemoved(position);

                                                                                                     //   finish();
                                                                                                      //  startActivity(getIntent());




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
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        // Write failed
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
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Write failed
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
                                }); // д








                        // если test = 0, то при долгом нажатии удаляем


                            //Toast.makeText(getApplication(), "УДАЛЕНИЕ ПОДПИСЧИКА: позиция -" + position + ", ключ -" + mylist.get(position).get("key").toString(), Toast.LENGTH_LONG).show();
/*
                            FirebaseDatabase.getInstance().getReference().child("users6").child(mylist.get(position).get("key").toString()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
                            mylist.remove(position);
                            adapter.notifyItemRemoved(position);
*/

                    }

                })
        );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


            getMenuInflater().inflate(R.menu.menu_allmess, menu);

        menu.findItem(R.id.index).setTitle(new Languages().MenuIndex());
        menu.findItem(R.id.likes).setTitle(new Languages().MenuLikes());
        menu.findItem(R.id.friends).setTitle(new Languages().MenuFriends());
        menu.findItem(R.id.about).setTitle(new Languages().MenuAbout());


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.index:

            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
            startActivity(Profile);

            finish();

            return true;

        case R.id.friends:

            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
            startActivity(usersScreen);


            //Toast.makeText(this, "Аватарка обновлена", Toast.LENGTH_LONG).show();
            return true;

        case R.id.likes:

            Intent likesScreen = new Intent(getApplication(), ActivityLikes.class);
            startActivity(likesScreen);

            //Toast.makeText(this, "Аватарка обновлена", Toast.LENGTH_LONG).show();
            return true;

        case R.id.about:

            Functions functions = new Functions();

            functions.About(VideoActivityAllMess.this);

            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    public int dp(int px)
    {
        final float scale = this.getResources().getDisplayMetrics().density;

        int pixels = (int) (px * scale + 0.5f);

        return pixels;
    }

}