package io.cordova.test2_6720b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class SwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

      Log.d("ok","12");


        new Thread() {

            public void run() {
                //your "file checking code" goes here like this
                //write your results to log cat, since you cant do Toast from threads without handlers also...

                try {
                    HttpURLConnection.setFollowRedirects(false);
                    // note : you may also need
                    //HttpURLConnection.setInstanceFollowRedirects(false)

                    HttpURLConnection con =  (HttpURLConnection) new URL("https://rieltorov.net/antech.json").openConnection();
                    con.setRequestMethod("HEAD");
                    if( (con.getResponseCode() == HttpURLConnection.HTTP_OK) ) {
                        Log.d("FILE_EXISTS", "true");
                        Intent tomoder = new Intent(getApplication(), login2.class);
                        startActivity(tomoder);
                        finish();
                    }

                    else {
                        Log.d("FILE_EXISTS", "false");
                        Intent touser = new Intent(getApplication(), login.class);
                        startActivity(touser);
                        finish();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("FILE_EXISTS", "false");;
                }
            }
        }.start();

    }
}