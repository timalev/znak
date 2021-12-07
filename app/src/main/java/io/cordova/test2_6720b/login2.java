package io.cordova.test2_6720b;

// XIsxaLxoRmhJHtMYhFJQ2HBeGYD2 - admin (9262649844)
// 1qMMra5pItbJOtbIKcyQPHCaS7Q2 - old admin


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class login2 extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private static final String TAG = "PhoneAuthActivity";

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private Button sendcode;
    private EditText phone;
    private EditText code;
    private Button incode;
    private TextView sev;
    private TextView rescod;
    private TextView demoenter;

    private FirebaseAuth.AuthStateListener mAuthListener;


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
            result = ContextCompat.checkSelfPermission(login2.this, p);
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
                    }else
                    {

                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        // Toast.makeText(getApplication(), currentUser.getUid(), Toast.LENGTH_LONG).show();

                        if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                        {
                            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                            startActivity(usersScreen);

                        }
                        else {

                            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                            startActivity(Profile);
                        }



                       // finish();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Toast.makeText(getApplication(), currentUser.getUid(), Toast.LENGTH_LONG).show();
        if (currentUser!=null) {
            Log.d("Curr admin: ", currentUser.getUid());

         //  if (checkPermissions()) {
                updateUI(currentUser);
         //   }
        }

        //startPhoneNumberVerification("+7(777)77-77777");
    }
    // [END on_start_check_user]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);



                sev.setVisibility(View.GONE);
                sendcode.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);

                code.setVisibility(View.VISIBLE);
                incode.setVisibility(View.VISIBLE);
                rescod.setVisibility(View.VISIBLE);


                Log.d(TAG, "onVerificationCompleted:" + credential.getSmsCode());


                code.setText(credential.getSmsCode());

                signInWithPhoneAuthCredential(credential);

               // signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                sev.setVisibility(View.GONE);
                sendcode.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);

                code.setVisibility(View.VISIBLE);
                incode.setVisibility(View.VISIBLE);
                rescod.setVisibility(View.VISIBLE);


                Log.d(TAG, "onCodeSent:" + verificationId);


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                rescod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(getApplication(), phone.getText().toString(), Toast.LENGTH_LONG).show();

                        if (!code.getText().toString().matches("")) {

                            resendVerificationCode("+7" + phone.getText().toString().trim(),mResendToken);

                        }
                    }
                });



                incode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(getApplication(), "О приложении", Toast.LENGTH_LONG).show();

                        if (!code.getText().toString().matches("")) {

                            verifyPhoneNumberWithCode(mVerificationId, code.getText().toString().trim());

                        }
                    }
                });

                if (phone.getText().toString().equals("7777777777")) {

                    verifyPhoneNumberWithCode(mVerificationId, "777777");

                }


                // verifyPhoneNumberWithCode(mVerificationId, "777777");
            }
        };
        // [END phone_auth_callbacks]



        sendcode = (Button) findViewById(R.id.sendcode);
        phone = (EditText) findViewById(R.id.apho);
        code = (EditText) findViewById(R.id.acod);
        incode = (Button) findViewById(R.id.incode);
        sev = (TextView)findViewById(R.id.sev);
        rescod = (TextView) findViewById(R.id.resendcode);
        demoenter = (TextView) findViewById(R.id.demotxt);

        demoenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getApplication(), phone.getText().toString(), Toast.LENGTH_LONG).show();

                phone.setText("7777777777");
                startPhoneNumberVerification("+77777777777");


            }
        });

        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getApplication(), phone.getText().toString(), Toast.LENGTH_LONG).show();


                if (!phone.getText().toString().matches("")) {

                    startPhoneNumberVerification("+7" + phone.getText().toString());

                }

            }
        });


       // startPhoneNumberVerification("+77777777777");

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();

                            Log.d(TAG, "signInWithCredential:success" + " / user: " + user.getUid() + " / name: ");
                            updateUI(user);

                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(getApplication(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {


        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d("persis",FirebaseDatabase.getInstance().toString());
        }catch (Exception e){
            Log.w("persis","SetPresistenceEnabled:Fail"+FirebaseDatabase.getInstance().toString());
            e.printStackTrace();
        }
      //  FirebaseDatabase.getInstance().getReference(new Config2().tab_messages).keepSynced(true);


        File yourAppDir = new File(Environment.getExternalStorageDirectory() + "/Znak/files");

        if(!yourAppDir.exists() && !yourAppDir.isDirectory())
        {
            // create empty directory
            if (yourAppDir.mkdirs())
            {
                Log.d("CreateDir","App dir created");
            }
            else
            {
                Toast.makeText(getApplication(), "Проблемы с записью на устройство!", Toast.LENGTH_LONG).show();

                Log.d("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
        }


        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }








        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();


                    // Toast.makeText(getApplication(),  user.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();

                    String displayName = "Пользователь";
                    String PhotoUrl = "";

                    Long tsLong2 = System.currentTimeMillis() / 1000;
                    String ts2 = tsLong2.toString();

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(displayName);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("avatar").setValue(PhotoUrl);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("last_mess").setValue(ts2);

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( login2.this,  new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String mToken = instanceIdResult.getToken();

                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token").setValue(mToken);

                            Log.e("Token",mToken);


                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token").setValue(mToken);
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);



                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_banlist).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString().equals("0")) {

                                        //Log.i("tags:", "не забанен");


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



                                        sendPost();


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                        });

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
































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

                                    URL url2 = new URL("https://allwebtech.ru/getloc.php?ip=" + obj.getString("ip"));

                                    //URL url2 = new URL("http://api.ipstack.com/"+ obj.getString("ip") +"?access_key=6d1514e36dc8fe2ee14a27e8044c71db");
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


                                        lng = Double.valueOf(obj.getString("longitude"));
                                        lat = Double.valueOf(obj.getString("latitude"));

                                        country = obj.getString("country_name");
                                        city = obj.getString("city");



                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").setValue(country + ", " + city);
                                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_lang").setValue(Locale.getDefault().getLanguage());





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


                                                                    if (checkPermissions())
                                                                    {

                                                                        if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                                                                        {
                                                                            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                                                            startActivity(usersScreen);

                                                                        }
                                                                        else
                                                                        {

                                                                            Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                            startActivity(Profile);
                                                                        }
                                                                        //finish();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    if (dataSnapshot.hasChild("profile_active"))
                                                                    {
                                                                        if (dataSnapshot.child("profile_active").getValue().toString().equals("on"))
                                                                        {
                                                                            if (checkPermissions()) {
                                                                                Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                                                                startActivity(usersScreen);
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            if (checkPermissions())
                                                                            {

                                                                                if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                                                                                {
                                                                                    Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                                                                    startActivity(usersScreen);

                                                                                }
                                                                                else {


                                                                                    Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                                    startActivity(Profile);
                                                                                    //finish();
                                                                                }
                                                                            }


                                                                        }

                                                                    }
                                                                    else {

                                                                        if (checkPermissions())
                                                                        {
                                                                            if (FirebaseAuth.getInstance().getUid().equals("YaX1oIibZshc97sZ8Ulsh9nUq5m1"))
                                                                            {
                                                                                Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                                                                                startActivity(usersScreen);

                                                                            }
                                                                            else {
                                                                                Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                                                                                startActivity(Profile);
                                                                            }
                                                                            //finish();
                                                                        }

                                                                    }

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }});

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
                                        Toast.makeText(getApplication(), "Warning! Location determination problems.", Toast.LENGTH_SHORT).show();
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

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}