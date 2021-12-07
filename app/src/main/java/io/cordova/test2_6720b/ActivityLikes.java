package io.cordova.test2_6720b;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityLikes extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LikesAdapter adapter;

    private ArrayList<HashMap> mylist = new ArrayList<HashMap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        setTitle(new Languages().MenuLikes());




        toolbar.setTitleTextColor(Color.WHITE);


        recyclerView = (RecyclerView) findViewById(R.id.messes_list2);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        adapter = new LikesAdapter(mylist);
        recyclerView.setAdapter(adapter);

       // Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

        // FirebaseAuth.getInstance().getCurrentUser().getUid()

        FirebaseDatabase.getInstance().getReference().child("zn_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Log.d("alllikes5", child.getKey() + " / " +String.valueOf(child.getValue()));

                    Log.d("alllikes7", child.getKey() + " / " +String.valueOf(child.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue()));


                    if (String.valueOf(child.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue()).equals("1"))
                    {
                        Log.d("alllikes8", child.getKey() + " / " +String.valueOf(child.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue()));
                    }

                    final HashMap<String, String> map = new HashMap<String, String>();


                    map.put("name", child.getValue().toString());
                    map.put("key", child.getKey());


                    mylist.add(map);


                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            ;
        });







/*
        recyclerView.addOnItemTouchListener(
                new UsersActivity.RecyclerItemClickListener(this, recyclerView ,new UsersActivity.RecyclerItemClickListener.OnItemClickListener() {


                    @Override public void onItemClick(View view, int position) {


                        Intent nextScreen = new Intent(getApplication(), VideoActivityMess.class);

                        nextScreen.putExtra("curruser", mylist.get(position).get("key").toString());
                        nextScreen.putExtra("currname", mylist.get(position).get("name").toString());

                        startActivity(nextScreen);

                        finish();



                    }
                    @Override public void onLongItemClick(View view, final int position) {

                    }



                })
        );

*/


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.menu_likes, menu);

        menu.findItem(R.id.index).setTitle(new Languages().MenuIndex());
        menu.findItem(R.id.friends).setTitle(new Languages().MenuFriends());
        menu.findItem(R.id.messages).setTitle(new Languages().MenuMessages());
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

        case R.id.messages:

            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {

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


        case R.id.about:

            Functions functions = new Functions();

            functions.About(ActivityLikes.this);

            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
}
