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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VideoActivityAllMess extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private String curruser;
    private String currname;

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

        curruser = getIntent().getExtras().getString("curruser");
        currname = getIntent().getExtras().getString("currname");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        setTitle("Мои сообщения");

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

                    Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                    Collection<Object> values = td.values();

                    Log.d("TEST: ", currname);

                    //Log.d("Collection2: ", values.getClass().getName());

                    List list = new ArrayList(values);

                    Map<String, String> hashMap2 = new HashMap<String, String>();

                    for (int l = 0; l < list.size(); l++) {

                        // Log.d("List2: ", list.get(l).getClass().getName() + ", размер: " + list.toString());

                        HashMap<String, Object> itemsList = (HashMap<String, Object>) list.get(l);

                        for (Map.Entry<String, Object> entry : itemsList.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            // do stuff

                            HashMap<String, String> itemsList2 = (HashMap<String, String>) value;


                            if (!itemsList2.get("user").equals(mAuth.getCurrentUser().getUid())) {

                                hashMap2.put(itemsList2.get("user"), itemsList2.get("name"));

                            } else // если сам юзер, то ставим тому кому писал, данные которого также пишутся в таблицу при составлении письма
                            {
                                hashMap2.put(itemsList2.get("page_for_comment"), itemsList2.get("currname"));
                            }

                            //Log.d("COUNT6: ",String.valueOf(itemsList2.get("user")) + "/" + itemsList2.get("avatar"));


                            // }
                        }
                        //   int count = Collections.frequency(new ArrayList<String>(hashMap.values()), user_var);

                        //Log.d("test_var2: ", user_var + " - юзеры," + name_var + " (" + count + ")");

                    }

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        // Map<String, Object> map = (Map<String, Object>) child.getValue();

                        final String Key = child.getKey();

                        final String name = hashMap2.get(Key);


                        final HashMap<String, String> map = new HashMap<String, String>();


                        map.put("name", name);
                        map.put("key", Key);


                        mylist.add(map);


                        // Log.d("COUNT4: ",String.valueOf(map) + "/"  + "/" +name);


                    }
                    adapter.notifyDataSetChanged();


                }else
                {

                    TextView nomessages = (TextView) findViewById(R.id.nomessages);
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

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(GetAllMessagesListener);
        //  FirebaseDatabase.getInstance().getReference().child("users6").orderByChild("subscribers/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).equalTo(1).addListenerForSingleValueEvent(GetAllMessagesListener);


        recyclerView.addOnItemTouchListener(
                new UsersActivity.RecyclerItemClickListener(this, recyclerView ,new UsersActivity.RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

// если test = 0, то значит массив выбран поумолчани и к нему лепим переход на страницу юзера, если test = 1, значит лепим переход + добавление в подписчики

                        //Toast.makeText(getApplication(), "ПЕРЕХОД И ДОБАВЛЕНИЕ ПОДПИСЧИКА: позиция -" + position + ", ключ -" + mylist3.get(position).get("key").toString(), Toast.LENGTH_LONG).show();

                             Intent nextScreen = new Intent(getApplication(), VideoActivityMess.class);

                            nextScreen.putExtra("curruser", mylist.get(position).get("key").toString());
                            nextScreen.putExtra("currname", mylist.get(position).get("name").toString());

                            startActivity(nextScreen);

                            finish();



                        //Toast.makeText(getApplication(), mylist.get(position).get("name").toString(), Toast.LENGTH_LONG).show();

                    }


                    @Override public void onLongItemClick(View view, int position) {

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

        case R.id.about:
            //add the function to perform here
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