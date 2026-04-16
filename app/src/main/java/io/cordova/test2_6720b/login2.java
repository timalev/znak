package io.cordova.test2_6720b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class login2 extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    // UI
    private Button sendcode, incode;
    private EditText phone, code;
    private TextView sev, rescod, demoenter, permissionsStr, ban_text;
    private ProgressBar pbAuth;

    public static final int MULTIPLE_PERMISSIONS = 10;
    private boolean waitingForPermsReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // ✅ 1 раз при старте

        setContentView(R.layout.activity_login2);
        initViews();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure").invoke(null);
            } catch (Exception e) { e.printStackTrace(); }
        }

        mAuth = FirebaseAuth.getInstance();

        // ✅ При холодном старте: если сессия жива -> сразу шлюз прав
        if (mAuth.getCurrentUser() != null) {
            checkPermissionsAndProceed(mAuth.getCurrentUser());
        }
    }

    private void initViews() {
        sendcode = findViewById(R.id.sendcode);
        phone = findViewById(R.id.apho);
        code = findViewById(R.id.acod);
        incode = findViewById(R.id.incode);
        sev = findViewById(R.id.sev);
        rescod = findViewById(R.id.resendcode);
        demoenter = findViewById(R.id.demotxt);
        pbAuth = findViewById(R.id.progressBarAuth);
        permissionsStr = findViewById(R.id.permissions);
        ban_text = findViewById(R.id.ban_text);

        sendcode.setOnClickListener(this);
        incode.setOnClickListener(this);
        rescod.setOnClickListener(v -> resendCode());
        demoenter.setOnClickListener(v -> {
            phone.setText("7777777777");
            startPhoneNumberVerification("+77777777777");
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendcode) sendCode();
        else if (v.getId() == R.id.incode) verifyCode();
    }

    private void sendCode() {
        String raw = phone.getText().toString().trim().replaceAll("[\\s()-]", "");
        if (raw.isEmpty()) { Toast.makeText(this, "Введите номер", Toast.LENGTH_SHORT).show(); return; }
        final String phoneNumber = raw.startsWith("+") ? raw : "+7" + raw.replaceFirst("^8", "");

        pbAuth.setVisibility(View.VISIBLE);
        sendcode.setEnabled(false);
        startPhoneNumberVerification(phoneNumber);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        runOnUiThread(() -> {
                            pbAuth.setVisibility(View.GONE);
                            if (credential.getSmsCode() != null) code.setText(credential.getSmsCode());
                        });
                        signInWithPhoneAuthCredential(credential);
                    }
                    @Override public void onVerificationFailed(@NonNull FirebaseException e) {
                        runOnUiThread(() -> {
                            pbAuth.setVisibility(View.GONE);
                            sendcode.setEnabled(true);
                            String msg = e instanceof FirebaseAuthInvalidCredentialsException ? "Неверный формат номера" : "Ошибка: " + e.getMessage();
                            Toast.makeText(login2.this, msg, Toast.LENGTH_LONG).show();
                        });
                    }
                    @Override public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        mVerificationId = verificationId;
                        mResendToken = token;
                        runOnUiThread(() -> {
                            pbAuth.setVisibility(View.GONE);
                            phone.setVisibility(View.GONE);
                            sendcode.setVisibility(View.GONE);
                            sev.setVisibility(View.VISIBLE);
                            sev.setText("Код отправлен");
                            code.setVisibility(View.VISIBLE);
                            incode.setVisibility(View.VISIBLE);
                            rescod.setVisibility(View.VISIBLE);
                            code.requestFocus();
                        });
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode() {
        String verificationCode = code.getText().toString().trim();
        if (verificationCode.length() != 6) { Toast.makeText(this, "Введите 6 цифр", Toast.LENGTH_SHORT).show(); return; }
        pbAuth.setVisibility(View.VISIBLE);
        incode.setEnabled(false);
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, verificationCode));
    }

    private void resendCode() {
        if (mResendToken != null) {
            String raw = phone.getText().toString().trim().replaceAll("[\\s()-]", "");
            String phoneNumber = raw.startsWith("+") ? raw : "+7" + raw;
            startPhoneNumberVerification(phoneNumber);
            Toast.makeText(this, "Код отправлен повторно", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                if (user != null) {
                    // 🔥 ИСПРАВЛЕНО: больше не вызывает updateUI напрямую. Идёт через шлюз прав.
                    checkPermissionsAndProceed(user);
                }
            } else {
                runOnUiThread(() -> {
                    pbAuth.setVisibility(View.GONE);
                    incode.setEnabled(true);
                    Toast.makeText(login2.this, "Неверный код", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // 🔥 ЕДИНСТВЕННЫЙ шлюз: проверяет права. Если нет -> запрашивает. Если да -> идёт в БД.
    private void checkPermissionsAndProceed(FirebaseUser user) {
        if (user == null) return;
        if (hasAllPermissions()) {
            waitingForPermsReturn = false;
            updateUI(user);
        } else {
            waitingForPermsReturn = true; // Помечаем, что ждём выдачи прав
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), MULTIPLE_PERMISSIONS);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) return;
        String uid = user.getUid();
        String displayName = "Пользователь";
        Long ts = System.currentTimeMillis() / 1000;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child(new Config2().tab_users).child(uid).child("name").setValue(displayName);
        db.getReference().child(new Config2().tab_users).child(uid).child("last_mess").setValue(ts.toString());

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                db.getReference().child(new Config2().tab_users).child(uid).child("device_token").setValue(task.getResult());
            }
            proceedWithBanCheck(uid);
        });
    }

    private void proceedWithBanCheck(String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child(new Config2().tab_users).child(uid).child("status").setValue("offline");
        db.getReference().child(new Config2().tab_users).child(uid).child("curr_activity").setValue("login2");
        db.getReference().child(new Config2().tab_users).child(uid).child("subscribers").child(uid).setValue(1);

        db.getReference().child(new Config2().tab_banlist).child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snap) {
                        boolean banned = snap.exists() && !"0".equals(snap.getValue().toString());
                        if (banned) {
                            runOnUiThread(() -> {
                                if (ban_text != null) { ban_text.setText(new Languages().LoginBantext()); ban_text.setVisibility(View.VISIBLE); }
                                if (sendcode != null) sendcode.setVisibility(View.GONE);
                                if (incode != null) incode.setVisibility(View.GONE);
                            });
                        } else {
                            sendPost();
                        }
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) { sendPost(); }
                });
    }

    // 🔥 Возврат из настроек: только проверка, НИКАКОГО запроса
    @Override protected void onResume() {
        super.onResume();
        if (waitingForPermsReturn && mAuth.getCurrentUser() != null) {
            if (hasAllPermissions()) {
                waitingForPermsReturn = false;
                updateUI(mAuth.getCurrentUser());
            }
        }
    }

    private boolean hasAllPermissions() {
        for (String p : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    private String[] getRequiredPermissions() {
        List<String> list = new ArrayList<>();
        list.add(android.Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(android.Manifest.permission.READ_MEDIA_IMAGES);
            list.add(android.Manifest.permission.POST_NOTIFICATIONS);
        } else {
            list.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            list.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            boolean allGranted = true;
            for (int r : grantResults) if (r != PackageManager.PERMISSION_GRANTED) allGranted = false;

            if (allGranted) {
                waitingForPermsReturn = false;
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) updateUI(user);
            } else {
                waitingForPermsReturn = true; // Ждём ручного включения в настройках
                if (permissionsStr != null) permissionsStr.setVisibility(View.VISIBLE);
                showSettingsDialog();
            }
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Требуется доступ")
                .setMessage("Разрешите Камеру и Хранилище в настройках приложения, иначе вход невозможен.")
                .setPositiveButton("Настройки", (d, w) -> {
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(android.net.Uri.fromParts("package", getPackageName(), null));
                    startActivity(i);
                })
                .setNegativeButton("Отмена", (d, w) -> finish())
                .setCancelable(false).show();
    }

    public void sendPost() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snap) {
                        List<String> array = new ArrayList<>();
                        List<String> geo = new ArrayList<>();
                        if (!snap.hasChild("profile_country")) geo.add("c");
                        if (!snap.hasChild("coords")) geo.add("g");
                        if (!snap.hasChild("profile_name")) array.add("n");
                        if (!snap.hasChild("profile_age")) array.add("a");
                        if (!snap.hasChild("profile_photo")) array.add("p");
                        if (!snap.hasChild("profile_gender")) array.add("s");

                        Intent next;
                        if (!array.isEmpty()) {
                            next = "YaX1oIibZshc97sZ8Ulsh9nUq5m1".equals(uid) ? new Intent(getApplication(), UsersActivity.class)
                                    : !geo.isEmpty() ? new Intent(getApplication(), getloc.class) : new Intent(getApplication(), ProfileActivity.class);
                        } else {
                            boolean active = snap.hasChild("profile_active") && "on".equals(snap.child("profile_active").getValue().toString());
                            next = active ? (!geo.isEmpty() ? new Intent(getApplication(), getloc.class) : new Intent(getApplication(), UsersActivity.class))
                                    : ("YaX1oIibZshc97sZ8Ulsh9nUq5m1".equals(uid) ? new Intent(getApplication(), UsersActivity.class)
                                    : !geo.isEmpty() ? new Intent(getApplication(), getloc.class) : new Intent(getApplication(), ProfileActivity.class));
                        }
                        next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(next);
                        finish();
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}