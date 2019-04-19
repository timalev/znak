package io.cordova.test2_6720b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class zaloba extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaloba);

        Button button = (Button) findViewById(R.id.button1);







        if (getIntent().getExtras().containsKey("extram")) {


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent Ban = new Intent(getApplication(), VideoActivityMess.class);

                    Ban.putExtra("curruser", getIntent().getExtras().getString("extram"));
                    Ban.putExtra("currname", getIntent().getExtras().getString("extram2"));

                    startActivity(Ban);

                    finish();




                    //mSwipeView.doSwipe(false);
                }
            });

           // Toast.makeText(getApplication(), getIntent().getExtras().getString("extram"), Toast.LENGTH_LONG).show();


            Log.i("test",getIntent().getExtras().getString("extram"));
           // currname = getIntent().getExtras().getString("extram2");



                final ImageView imageView = (ImageView) findViewById(R.id.UserPho);

/*
                final TextView textView = (TextView) findViewById(R.id.UserGeo);
                textView.setVisibility(View.VISIBLE);


*/

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(getIntent().getExtras().getString("extram")).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //textView.setText(dataSnapshot.child("profile_country").getValue().toString());


                        Glide
                                .with(getApplicationContext())
                                .load(dataSnapshot.child("zaloba").getValue())
                                .error(R.drawable.noavatar)
                                .into(imageView);


                        //Toast.makeText(getApplication(), String.valueOf(dataSnapshot.child("profile_photo").getValue()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }

    }


}
