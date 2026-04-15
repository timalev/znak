package io.cordova.test2_6720b;

// XIsxaLxoRmhJHtMYhFJQ2HBeGYD2 - admin (7777777777) for phone auth
// Simh5X1gVCbqkH0qPJ5N6ouqKTx1 - admin for google auth (tim cox)
// YaX1oIibZshc97sZ8Ulsh9nUq5m1 - google play moder (5555555555)


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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
import android.widget.ProgressBar;
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
import com.google.firebase.messaging.FirebaseMessaging;


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

    private TextView permissionsStr;

    private ProgressBar pbAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    public static final int MULTIPLE_PERMISSIONS = 10;




    private boolean checkPermissions() {

        List<String> permissionList = new ArrayList<>();
        permissionList.add(android.Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13+ (API 33+) используем новые разрешения
            permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES);
            permissionList.add(android.Manifest.permission.POST_NOTIFICATIONS);
        } else {
            // Для Android 12 и ниже используем старые
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        String[] permissions = permissionList.toArray(new String[0]);

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                    p.equals(android.Manifest.permission.POST_NOTIFICATIONS)) {
                continue;
            }

            result = ContextCompat.checkSelfPermission(login2.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        Log.d("CURR_PERMISSIONS:", listPermissionsNeeded.toString());
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
                    Log.d("VSEGO_PERMISSIONS:",res_arr.toString());

                    if (res_arr.size()>0) {

                        permissionsStr.setVisibility(View.VISIBLE);
                        pbAuth.setVisibility(View.GONE);

                        showSettingsDialog();



                        Toast.makeText(this, "Недостаточно разрешений для запуска приложения", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("RES_PERMISSIONS:","OK");

                        FirebaseUser currentUser = mAuth.getCurrentUser();


                        if (currentUser != null) {
                            Log.d("Curr admin: ", currentUser.getUid());

                            //  if (checkPermissions()) {
                            updateUI(currentUser);
                            //   }
                        } else {
                            Log.d("ERR_е: ", "опять авторизация");
                            startPhoneNumberVerification("+75555555555");
                        }

                    }


                }
                return;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login2);





        // [END phone_auth_callbacks]


        sendcode = (Button) findViewById(R.id.sendcode);
        phone = (EditText) findViewById(R.id.apho);
        code = (EditText) findViewById(R.id.acod);
        incode = (Button) findViewById(R.id.incode);
        sev = (TextView) findViewById(R.id.sev);
        rescod = (TextView) findViewById(R.id.resendcode);
        demoenter = (TextView) findViewById(R.id.demotxt);
        pbAuth = (ProgressBar) findViewById(R.id.progressBarAuth) ;
        permissionsStr = (TextView) findViewById(R.id.permissions);

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

                //  code.setVisibility(View.VISIBLE);
                // incode.setVisibility(View.VISIBLE);
                // rescod.setVisibility(View.VISIBLE);


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

                // code.setVisibility(View.VISIBLE);
                // incode.setVisibility(View.VISIBLE);
                // rescod.setVisibility(View.VISIBLE);


                Log.d(TAG, "onCodeSent:" + verificationId);


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                rescod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(getApplication(), phone.getText().toString(), Toast.LENGTH_LONG).show();

                        if (!code.getText().toString().matches("")) {

                            resendVerificationCode("+7" + phone.getText().toString().trim(), mResendToken);

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

                    // verifyPhoneNumberWithCode(mVerificationId, "777777");

                }


                verifyPhoneNumberWithCode(mVerificationId, "555555");
            }
        };


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


        //startPhoneNumberVerification("+75555555555");


        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (checkPermissions()) {


            if (currentUser != null) {
                Log.d("Curr admin: ", currentUser.getUid());

                //  if (checkPermissions()) {
                updateUI(currentUser);
                //   }
            } else {
                Log.d("ERR_е: ", "опять авторизация");
                startPhoneNumberVerification("+75555555555");
            }
        }else {Log.d("PERMISSIONS_:","NONE");}


/*
        if (mAuth != null && mCallbacks != null && checkPermissions()) {
            // запускаем только если всё готово
            startPhoneNumberVerification("+75555555555");
        }
*/

       // throw new RuntimeException("Pidory");


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
    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Permissions Required");
        builder.setMessage("This app requires Camera and Storage permissions to function properly. Please enable them in the app settings.");

        builder.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // Открываем настройки конкретно нашего приложения
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                android.net.Uri uri = android.net.Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // Если пользователь отказался — закрываем приложение, так как работать оно не будет
                finish();
            }
        });

        builder.show();
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

        Log.d("PIZDEN","BLYA");


        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d("persis",FirebaseDatabase.getInstance().toString());
        }catch (Exception e){
            Log.w("persis","SetPresistenceEnabled:Fail"+FirebaseDatabase.getInstance().toString());
            e.printStackTrace();
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


                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Получаем новый токен
                            String mToken = task.getResult();


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

    @Override
    protected void onResume() {
        super.onResume();



        // Как только пользователь вернулся из настроек, проверяем права еще раз
        List<String> listPermissionsNeeded = new ArrayList<>();
        // ... здесь та же логика проверки, что в твоем методе checkPermissions()

        // Если теперь всё разрешено (список пуст) — пускаем дальше
        if (checkUserPermissionsStatus()) {
            Log.d("RES_PERMISSIONS:", "OK after return from settings");

            pbAuth.setVisibility(View.VISIBLE);

            // Твоя логика входа:
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                updateUI(currentUser);
            } else {
                startPhoneNumberVerification("+75555555555");
            }
        }
    }

    // Вспомогательный метод, чтобы не дублировать код
    private boolean checkUserPermissionsStatus() {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(android.Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13+ (API 33+) используем новые разрешения
            permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES);
            permissionList.add(android.Manifest.permission.POST_NOTIFICATIONS);
        } else {
            // Для Android 12 и ниже используем старые
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        String[] permissions = permissionList.toArray(new String[0]);

        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    public void sendPost() {



        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_country").removeValue();
        // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").removeValue();


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> array = new ArrayList<String>();
                List<String> geo = new ArrayList<String>();


                if (dataSnapshot.hasChild("coords"))
                {
                    // на досуге добавим если поле есть, но пустое





                if (dataSnapshot.hasChild("profile_country"))
                {
                    // на досуге добавим если поле есть, но пустое

                    // Toast.makeText(getApplication(), "Est strana" + dataSnapshot.getKey() + " / " + dataSnapshot.child("last_mess").getValue().toString(), Toast.LENGTH_SHORT).show();


                }
                else {
                    //Toast.makeText(getApplication(), "Net strana", Toast.LENGTH_SHORT).show();
                    geo.add("страна");
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


                    Intent Profile = new Intent(getApplication(), ProfileActivity.class);
                    startActivity(Profile);

                    //finish();

                }
                else
                {
                    Log.d("GO_USERS","YES!");

                    Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
                    startActivity(usersScreen);


                }
                }
                else {

                    Log.d("tester:","MO GRO");

                    Intent Geo = new Intent(getApplication(), getloc.class);
                    startActivity(Geo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});




        //http://api.ipstack.com/134.201.250.155?access_key=6d1514e36dc8fe2ee14a27e8044c71db


        //  }




    }


    //http://api.ipstack.com/134.201.250.155?access_key=6d1514e36dc8fe2ee14a27e8044c71db



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