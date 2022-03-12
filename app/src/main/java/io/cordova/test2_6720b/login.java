package io.cordova.test2_6720b;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (checkPermissions()) {

        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

//                    Log.d("профиль: ", user.getPhotoUrl().toString());

                    // Toast.makeText(getApplication(),  user.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();

                    String displayName = user.getDisplayName();
//                    String PhotoUrl = user.getPhotoUrl().toString();

                    Long tsLong2 = System.currentTimeMillis() / 1000;
                    String ts2 = tsLong2.toString();

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(displayName);
                   // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("avatar").setValue(PhotoUrl);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("last_mess").setValue(ts2);

                    final String device_token = FirebaseInstanceId.getInstance().getToken();

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token").setValue(device_token);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);

                    if (device_token!=null) {

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_banlist).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString().equals("0")) {

                                        Log.i("tags:", "не забанен");

                                        sendPost();

                                    } else {
                                        Log.i("tags:", "забанен");

                                        TextView ban_text = (TextView) findViewById(R.id.ban_text);
                                        ban_text.setText(new Languages().LoginBantext());

                                        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                                        ban_text.setVisibility(View.VISIBLE);
                                    }
                                }else
                                {
                                    Log.i("tags2:", "не забанен");
                                    sendPost();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                }
            }
        };





            mAuth.addAuthStateListener(mAuthListener);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(LocationServices.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);

            findViewById(R.id.sign_in_button).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_in_button:

                signIn();


                break;
        }
    }


    public static final int MULTIPLE_PERMISSIONS = 10;

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(login.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10:

                ArrayList res_arr = new ArrayList<>();

                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED )
                        {
                            res_arr.add(1);
                        }
                    }

                    if (res_arr.size()>0) {

                        Toast.makeText(this, "Недостаточно разрешений для запуска приложения", Toast.LENGTH_SHORT).show();

                      //  android.os.Process.killProcess(android.os.Process.myPid());
                        //System.exit(1);
                    }else
                    {
                        Intent refr = new Intent(getApplication(), login.class);
                        startActivity(refr);
                    }


                }
                return;

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

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();



        mAuth.addAuthStateListener(mAuthListener);



    }
*/


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

            Log.d("Result2: ", "ok");

            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

            //updateUI(true);
        } else {
            Log.d("Result2: ", "auth faild " + result.getStatus());
             Toast.makeText(this, "Error: " +result.getStatus(), Toast.LENGTH_SHORT).show();
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

                            mAuth.addAuthStateListener(mAuthListener);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("err: ", "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }


    public void sendPost() {

Log.d("send_post","act");

        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").removeValue();
        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").removeValue();


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> array = new ArrayList<String>();
                List<String> geo = new ArrayList<String>();


                if (dataSnapshot.hasChild("profile_country"))
                {
                    // на досуге добавим если поле есть, но пустое

                    // Toast.makeText(getApplication(), "Est strana" + dataSnapshot.getKey() + " / " + dataSnapshot.child("last_mess").getValue().toString(), Toast.LENGTH_SHORT).show();


                }
                else {
                    //Toast.makeText(getApplication(), "Net strana", Toast.LENGTH_SHORT).show();
                    geo.add("страна");
                }
                if (dataSnapshot.hasChild("coords"))
                {
                    // на досуге добавим если поле есть, но пустое

                }
                else {
                    geo.add("гео");
                }

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

                Log.d("geo_size",geo.size() + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (array.size()!=0) {



                        if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                        {
                            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                            startActivity(usersScreen);

                        }
                        else
                        {


                            if (geo.size()!=0) {

                                Intent Geo = new Intent(getApplication(), getloc.class);
                                startActivity(Geo);

                            }else {


                                Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                startActivity(Profile);
                            }
                        }
                        //finish();

                }
                else
                {
                    if (dataSnapshot.hasChild("profile_active"))
                    {
                        if (dataSnapshot.child("profile_active").getValue().toString().equals("on"))
                        {

                                if (geo.size()!=0) {

                                    Intent Geo = new Intent(getApplication(), getloc.class);
                                    startActivity(Geo);

                                }else {
                                    Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                    startActivity(usersScreen);
                                }

                        }
                        else
                        {

                                if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                                {
                                    Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                    startActivity(usersScreen);

                                }
                                else {

                                    if (geo.size()!=0) {

                                        Intent Geo = new Intent(getApplication(), getloc.class);
                                        startActivity(Geo);

                                    }else {
                                        Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                        startActivity(Profile);
                                    }
                                    //finish();
                                }



                        }

                    }
                    else {


                            if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                            {
                                Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                startActivity(usersScreen);

                            }
                            else {

                                if (geo.size()!=0) {

                                    Intent Geo = new Intent(getApplication(), getloc.class);
                                    startActivity(Geo);

                                }else {
                                    Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                    startActivity(Profile);
                                }
                            }
                            //finish();


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});




        //http://api.ipstack.com/134.201.250.155?access_key=6d1514e36dc8fe2ee14a27e8044c71db


        //  }




    }


}