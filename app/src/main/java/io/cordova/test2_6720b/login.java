package io.cordova.test2_6720b;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static io.cordova.test2_6720b.UsersActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    private static int RC_SIGN_IN = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        /*
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
 */





        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    Log.d("профиль: ", user.getPhotoUrl().toString());

                    // Toast.makeText(getApplication(),  user.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();


                    String displayName = user.getDisplayName();
                    String PhotoUrl = user.getPhotoUrl().toString();


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(displayName);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("avatar").setValue(PhotoUrl);




                    final String device_token = FirebaseInstanceId.getInstance().getToken();


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token").setValue(device_token);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);




                   // Intent Profile = new Intent(getApplication(), ProfileActivity.class);

                    //startActivity(Profile);


                    if (device_token!=null) {


                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_banlist).addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString().equals("0")) {


                                        //Log.i("tags:", "не забанен");

                                        sendPost();

                                    } else {
                                        Log.i("tags:", "забанен");

                                        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                                        findViewById(R.id.ban_text).setVisibility(View.VISIBLE);
                                    }
                                }else
                                {
                                    sendPost();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }



                    //sendPost();






                    //Toast.makeText(getApplication(), "Юзер авторизован", Toast.LENGTH_SHORT).show();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_in_button:

                signIn();


                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



           mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1252: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mAuth.addAuthStateListener(mAuthListener);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    Toast.makeText(this, "БЕЗ ЛОКАЦИИ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                Log.i("Результат: ", String.valueOf(grantResults[0]));

                if (grantResults[0]<0)
                {
                    closeNow();

                }

                return;

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void signIn() {

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);


        progressBar.setVisibility(ProgressBar.VISIBLE);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        signInButton.setVisibility(SignInButton.GONE);



       Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
       startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Result: ", requestCode + ", " + resultCode);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Result: ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //Toast.makeText(this, acct.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();

            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

            //updateUI(true);
        } else {
            Log.d("Result2: ", "auth faild");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

       // Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Auth: ", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("auth:", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("err: ", "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }


    public void sendPost() {





        //Toast.makeText(getApplication(), extra + "/" + extra2, Toast.LENGTH_SHORT).show();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("https://api.myip.com/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");


                    //conn.setRequestProperty("Authorization","key=AAAAIF01ca4:APA91bGX0kMaXMAl3QNyq_QxiRZFari8jb43cVHtktYXgKuFdmnfBzcPF1V89nNf9Otz8xY3aG0ADA5Xo9axCeijovWIlIgWKrYEEs0AYTrfPmp6sD1CDW3Y16tSsY1C5vHqdIiQfYMy");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                           // Log.i("JSON", json);



                    Log.i("STATUS77", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG77" ,String.valueOf(conn.getResponseMessage()));

                    if (String.valueOf(conn.getResponseCode()).equals("200")) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        //Log.i("TEST77", String.valueOf(sb.toString()));

                        final JSONObject obj = new JSONObject(sb.toString());

                        Log.i("TEST77", obj.getString("ip"));

                        //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").setValue(obj.getString("country"));




                        Thread thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    URL url2 = new URL("http://api.ipstack.com/"+ obj.getString("ip") +"?access_key=6d1514e36dc8fe2ee14a27e8044c71db");
                                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                                    conn2.setRequestMethod("GET");
                                    conn2.setRequestProperty("Content-Type", "application/json");


                                    //conn.setRequestProperty("Authorization","key=AAAAIF01ca4:APA91bGX0kMaXMAl3QNyq_QxiRZFari8jb43cVHtktYXgKuFdmnfBzcPF1V89nNf9Otz8xY3aG0ADA5Xo9axCeijovWIlIgWKrYEEs0AYTrfPmp6sD1CDW3Y16tSsY1C5vHqdIiQfYMy");
                                    conn2.setDoOutput(true);
                                    conn2.setDoInput(true);

                                    // Log.i("JSON", json);



                                    Log.i("STATUS88", String.valueOf(conn2.getResponseCode()));
                                    Log.i("MSG88" ,String.valueOf(conn2.getResponseMessage()));

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

                                        Log.i("TEST99", obj.getString("latitude"));




                                    final double lng;
                                    final double lat;

                                    String country;
                                    String city;

                                    if (
                                            !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2") &&
                                            !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("iFGT3BWSN1UYC7z2wbbrUrDewzz1") &&
                                            !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("H43g4MEO2pVKppLYUfSIZwKACB93")
                                    )
                                    {


                                        lng = Double.valueOf(obj.getString("longitude"));
                                        lat = Double.valueOf(obj.getString("latitude"));

                                        country = obj.getString("country_name");
                                        city = obj.getString("city");

                                    }else
                                    {
                                        lng = 37.617635;
                                        lat = 55.755814;

                                        country = "Russia";
                                        city = "Moscow";
                                    }

                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").setValue(country + ", " + city);




                                    if (lat>0 && lng>0) {

                                            Coords coords = new Coords(lat, lng);

                                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").setValue(coords)

                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {



                                                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    List<String> array = new ArrayList<String>();

                                                                    if (dataSnapshot.hasChild("profile_name"))
                                                                    {
                                                                        // на досуге добавим если поле есть, но пустое

                                                                    }
                                                                    else {
                                                                        array.add("имя");
                                                                    }


                                                                    if (dataSnapshot.hasChild("profile_age"))
                                                                    {


                                                                    }
                                                                    else {
                                                                        array.add("возраст");
                                                                    }


                                                                    if (dataSnapshot.hasChild("profile_photo"))
                                                                    {

                                                                    }
                                                                    else {
                                                                        array.add("фото");
                                                                    }

                                                                    if (dataSnapshot.hasChild("profile_gender"))
                                                                    {

                                                                    }
                                                                    else {
                                                                        array.add("пол");
                                                                    }


                                                                    if (array.size()!=0) {

                                                                        Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                        startActivity(Profile);
                                                                    }
                                                                    else
                                                                    {
                                                                        if (dataSnapshot.hasChild("profile_active"))
                                                                        {
                                                                            if (dataSnapshot.child("profile_active").getValue().toString().equals("on"))
                                                                            {
                                                                                Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                                                                startActivity(usersScreen);
                                                                            }
                                                                            else
                                                                            {
                                                                                Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                                startActivity(Profile);
                                                                            }

                                                                        }
                                                                        else {
                                                                            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                            startActivity(Profile);
                                                                        }



                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }});





                                                            /*

                                                            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);


                                                            //usersScreen.putExtra("currlng", currlng);
                                                            //usersScreen.putExtra("currlat", currlat);

                                                            startActivity(usersScreen);
*/
                                                            //finish();




                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Write failed
                                                            // ...
                                                        }
                                                    }); // дубль
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplication(), "Проблемы с определением локации.", Toast.LENGTH_SHORT).show();
                                        }



                                        //http://api.ipstack.com/134.201.250.155?access_key=6d1514e36dc8fe2ee14a27e8044c71db




                                  //  }




                                    conn2.disconnect();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread2.start();



                        //http://api.ipstack.com/134.201.250.155?access_key=6d1514e36dc8fe2ee14a27e8044c71db




                    }




                    conn.disconnect();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


}