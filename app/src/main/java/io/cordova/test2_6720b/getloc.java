package io.cordova.test2_6720b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class getloc extends AppCompatActivity {

    public static final String WHERE_MY_CAT_ACTION = "ru.alexanderklimov.action.CAT";
    public static final String ALARM_MESSAGE = "Срочно пришлите кота!";

    /*
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

            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesPrivate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

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

            functions.About(getloc.this);

            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getloc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_loc);
        setSupportActionBar(toolbar);

        setTitle("Местоположение");
        toolbar.setTitleTextColor(Color.WHITE);

      //  toolbar.setTitleTextColor(Color.WHITE);


        final EditText message = (EditText) findViewById(R.id.eventtext);



/*

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("profile_country"))
                {
                    // на досуге добавим если поле есть, но пустое


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});
*/




        final Button gocal = (Button) findViewById(R.id.to_cal);
        gocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Toast.makeText(getApplicationContext(), category, Toast.LENGTH_LONG).show();


                if (!message.getText().toString().matches("")) {

      //              Toast.makeText(getApplicationContext(), message.getText().toString(), Toast.LENGTH_LONG).show();

                    final String geo = message.getText().toString();

                    final String lang = "ru_RU";

                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                // String lang = "en_US";



                                URL url2 = new URL("https://inpickup.ru/getloc2.php?lang=" + lang + "&geo=" + geo);

                                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                                conn2.setRequestMethod("GET");
                                conn2.setRequestProperty("Content-Type", "application/json");


                                conn2.setDoOutput(true);
                                conn2.setDoInput(true);

                                // Log.i("JSON", json);


                                Log.i("STATUS88", String.valueOf(conn2.getResponseCode()));
                                Log.i("MSG88", String.valueOf(conn2.getResponseMessage()));

                                //  if (String.valueOf(conn2.getResponseCode()).equals("200")) {

                                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                                StringBuilder sb2 = new StringBuilder();
                                String line;
                                while ((line = br2.readLine()) != null) {
                                    sb2.append(line + "\n");
                                }
                                br2.close();

                                Log.i("TEST88", String.valueOf(sb2.toString()));

                                final JSONObject obj = new JSONObject(sb2.toString());

                                String city = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("metaDataProperty").getJSONObject("GeocoderMetaData").getJSONObject("Address").getJSONArray("Components").getJSONObject(2).getString("name");
                                String country = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("metaDataProperty").getJSONObject("GeocoderMetaData").getJSONObject("Address").getJSONArray("Components").getJSONObject(0).getString("name");

                                String pos = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("Point").getString("pos");

                                String[] separated = pos.trim().split(" ");
                                String lng = separated[0]; // this will contain "Fruit"
                                String lat = separated[1]; // this will contain " they taste good"

                                Log.d("json_res:",FirebaseAuth.getInstance().getUid() + " / " + lng + " / " + lat + " / " + country + " / " + city);


                                double lngd= Double.parseDouble(lng);
                                double latd= Double.parseDouble(lat);

                                  Coords coords = new Coords(latd, lngd);

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").setValue(coords);
                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").setValue(country + ", " + city);
                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_lang").setValue(Locale.getDefault().getLanguage());

                               // Toast.makeText(getloc.this, "Местоположение установлено", Toast.LENGTH_SHORT).show();



                                Intent gofromloc;

                                if (getIntent().getExtras()!=null && getIntent().getExtras().containsKey("getloc")) {

                                    Log.d("loc_from:","ok");
/*
                                    Intent intent = new Intent();
                                    intent.setAction(WHERE_MY_CAT_ACTION);
                                    intent.putExtra("ru.alexanderklimov.broadcast.Message", ALARM_MESSAGE);
                                    sendBroadcast(intent);
*/
                                    gofromloc = new Intent(getApplication(), ProfileActivity.class);
                                    gofromloc.putExtra("updloc", "ok");






                                }else {
                                    gofromloc = new Intent(getApplication(), login2.class);
                                }

                               startActivity(gofromloc);






                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("err_res:",e.toString() + " / " + "https://inpickup.ru/getloc2.php?lang=" + lang + "&geo=" + Uri.encode(geo));
                            }
                        }
                    });
                    thread2.start();


                  /*
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("iv_mess").setValue(message.getText().toString())

                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("iv_cate").setValue(category)

                                            .addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                   // Intent nextScreen = new Intent(getApplication(), CalendarActivity.class);


                                                    // nextScreen.putExtra("category", category);
                                                    //nextScreen.putExtra("message", message.getText().toString());

                                                    // startActivity(nextScreen);
                                                }
                                            });

                                }
                            });
     */
                }



            }
        });
    }
    public void sendMessage(View view) {
        Intent intent = new Intent();
        intent.setAction(WHERE_MY_CAT_ACTION);
        intent.putExtra("ru.alexanderklimov.broadcast.Message", ALARM_MESSAGE);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

}