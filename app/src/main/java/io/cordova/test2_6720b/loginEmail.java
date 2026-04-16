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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class loginEmail extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    // UI
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvPermissions, tvBan;
    private ProgressBar pbAuth;

    public static final int MULTIPLE_PERMISSIONS = 10;
    private boolean waitingForPermsReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         FirebaseDatabase.getInstance().setPersistenceEnabled(true); // ✅ 1 раз при старте

        setContentView(R.layout.activity_login_email);

        initViews();

        if (Build.VERSION.SDK_INT >= 24) {
            try { StrictMode.class.getMethod("disableDeathOnFileUriExposure").invoke(null); }
            catch (Exception e) { e.printStackTrace(); }
        }

        mAuth = FirebaseAuth.getInstance();
        // При холодном старте: если сессия жива -> сразу шлюз прав
        if (mAuth.getCurrentUser() != null) {
            checkPermissionsAndProceed(mAuth.getCurrentUser());
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        pbAuth = findViewById(R.id.progressBarAuth);
        tvPermissions = findViewById(R.id.permissions);
        tvBan = findViewById(R.id.ban_text);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        // 🔹 Обработчик клика по ссылке на Google
        TextView tvGoogleLink = findViewById(R.id.tv_google_link);

        tvGoogleLink.setText(new Languages().GoogleEnterLink());

        if (tvGoogleLink != null) {
            tvGoogleLink.setOnClickListener(v -> {
                Intent intent = new Intent(loginEmail.this, login.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onClick(View v) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Пароль минимум 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        if (v.getId() == R.id.btn_login) {
            signIn(email, password);
        } else if (v.getId() == R.id.btn_register) {
            register(email, password);
        }
    }

    private void signIn(String email, String password) {
        pbAuth.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnRegister.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> handleAuthResult(task));
    }

    private void register(String email, String password) {
        pbAuth.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> handleAuthResult(task));
    }

    private void handleAuthResult(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.d("EmailAuth", "Auth success");
            checkPermissionsAndProceed(task.getResult().getUser());
        } else {
            Log.w("EmailAuth", "Auth failed", task.getException());
            runOnUiThread(() -> {
                pbAuth.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                btnRegister.setEnabled(true);
                Toast.makeText(loginEmail.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    // 🔥 ЕДИНСТВЕННЫЙ шлюз: проверяет права. Если нет -> запрашивает. Если да -> идёт в БД.
    private void checkPermissionsAndProceed(FirebaseUser user) {
        if (user == null) return;
        if (hasAllPermissions()) {
            waitingForPermsReturn = false;
            updateUI(user);
        } else {
            waitingForPermsReturn = true;
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), MULTIPLE_PERMISSIONS);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) return;
        String uid = user.getUid();
        String email = user.getEmail();
        String displayName = email != null ? email.split("@")[0] : "User";
        Long ts = System.currentTimeMillis() / 1000;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child(new Config2().tab_users).child(uid).child("name").setValue(displayName);
        db.getReference().child(new Config2().tab_users).child(uid).child("email").setValue(email);
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
        db.getReference().child(new Config2().tab_users).child(uid).child("curr_activity").setValue("loginEmail");
        db.getReference().child(new Config2().tab_users).child(uid).child("subscribers").child(uid).setValue(1);

        db.getReference().child(new Config2().tab_banlist).child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snap) {
                        boolean banned = snap.exists() && !"0".equals(snap.getValue().toString());
                        if (banned) {
                            runOnUiThread(() -> {
                                if (tvBan != null) { tvBan.setText(new Languages().LoginBantext()); tvBan.setVisibility(View.VISIBLE); }
                                if (btnLogin != null) btnLogin.setVisibility(View.GONE);
                                if (btnRegister != null) btnRegister.setVisibility(View.GONE);
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
                waitingForPermsReturn = true;
                if (tvPermissions != null) tvPermissions.setVisibility(View.VISIBLE);
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