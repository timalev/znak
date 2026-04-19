package io.cordova.test2_6720b;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


//import customfonts.MyTextView;

import static android.view.View.VISIBLE;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("Загрузка видео: ", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK) {

                //Toast.makeText(this, "Take picture OK", Toast.LENGTH_SHORT).show();

                UploadPicture();

            } else {
                Toast.makeText(this, "Take picture Error", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 1) // выбранное фото, проверить на поворот
        {
            if (resultCode == RESULT_OK) {

                String filesdir;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    filesdir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/photo.jpg";
                } else {
                    filesdir = Environment.getExternalStorageDirectory().toString() + "/Znak/files/photo.jpg";
                }
                //Toast.makeText(this, "Gallery picture OK", Toast.LENGTH_SHORT).show();

                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(filesdir);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    UploadPicture();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("pic_err:",e.getMessage());
                } catch (IOException e) {
                    Log.d("pic_err2:",e.getMessage());
                    e.printStackTrace();
                }


                //UploadPicture();

            } else {
                Toast.makeText(this, "Gallery picture Error", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Common error: requestCode: " + requestCode + ", resultCode: " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        Log.d("screen_px",String.valueOf(width));

        if (getIntent().getExtras()!=null && getIntent().getExtras().containsKey("updloc")) {

            Toast.makeText(this, "Местоположение изменено", Toast.LENGTH_SHORT).show();

        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
            File externalFiles = new File(this.getExternalFilesDir(null).toString());

            externalFiles.mkdir();

            File externalCache = new File(this.getExternalCacheDir()  + "/debug");

            externalCache.mkdir();


        } else {
            File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Znak");

            Log.i("Опять с файлами гемор:",getPackageName() + " путь: " + Environment.getExternalStorageDirectory() + "/Znak");

            if (!externalAppDir.exists()) {

                if (externalAppDir.mkdir()) {

                    File externalFiles = new File(Environment.getExternalStorageDirectory() + "/Znak/files");
                    externalFiles.mkdir();

                    File externalCache = new File(Environment.getExternalStorageDirectory() + "/Znak/cache");

                    if (externalCache.mkdir())
                    {
                        File externalDebug = new File(Environment.getExternalStorageDirectory() + "/Znak/cache/debug");
                        externalDebug.mkdir();

                    }
                }else {
                    Log.i("Опять с файлами гемор3:","Нет ни хуя");
                }
            }else
            {
                Log.i("Опять с файлами гемор2:","все ок");
            }

        }




// Условие (например, если в базе стоит флаг "deleted")


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  final ImageView r_right = (ImageView) findViewById(R.id.r_right);
                //  final ImageView r_left = (ImageView) findViewById(R.id.r_left);
                ScrollView mainContent = findViewById(R.id.scrollView1);
                LinearLayout deletedLayout = findViewById(R.id.deleted_profile_layout);

                if (dataSnapshot.hasChild("profile_delete")) {

                    mainContent.setVisibility(View.GONE);    // Прячем всё
                    deletedLayout.setVisibility(View.VISIBLE); // Показываем н

                    //Toast.makeText(getApplication(), "Анкета удалена", Toast.LENGTH_LONG).show();


                }else
                {
                    mainContent.setVisibility(View.VISIBLE);
                    deletedLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





        setContentView(R.layout.activity_profile);

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  final ImageView r_right = (ImageView) findViewById(R.id.r_right);
                //  final ImageView r_left = (ImageView) findViewById(R.id.r_left);

                if (!dataSnapshot.hasChild("profile_polic_add")) {

                    //Toast.makeText(getApplication(), "Галочки нет", Toast.LENGTH_LONG).show();





                    try {
                        PackageInfo pInfo = ProfileActivity.this.getPackageManager().getPackageInfo("io.cordova.inv", 0);
                        String version = pInfo.versionName;

                        //final TextView message = new TextView(EventActivity.this);

                        // "Test this dialog following the link to dtmilano.blogspot.com"
                        //  final SpannableString s =
                        //        new SpannableString(EventActivity.this.getText(R.string.cool_link));
                        // Linkify.addLinks(s, Linkify.WEB_URLS);

                        // message.setText(s);
                        // message.setMovementMethod(LinkMovementMethod.getInstance());

                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                        alertDialog.setTitle("Пользовательское соглашение");
                        //  alertDialog.setView(message);

                        alertDialog.setMessage(Html.fromHtml("Запрещено публиковать любые материалы связанные с:<br><ul><li>тематикой сексуального характера</li><li>запугиванием, угрозами или призывами к насилию</li><li>насмешками или оскорблениями на почве религиозной или национальной неприязни</li></ul>Пользователи нарушившие данные правила будут незамедлительно заблокированы модераторами<br><br>Также, нажимая на кнопку \"Принять\" Вы соглашаетесь с <a href=\"\n" +
                                "https://yadi.sk/d/ZlwjDQDEGKuaOw\">политикой конфиденциальности</a>"));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Принять",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int which) {

                                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_polic_add").setValue(1)






                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        dialog.dismiss();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Write failed
                                                        // ...
                                                    }
                                                });



                                        //  dialog.dismiss();
                                    }
                                });
                        //alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());



                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });










        final EditText editname   = (EditText)findViewById(R.id.editname);
        //editname.setSelection(editname.getText().length());

        final EditText editage   = (EditText)findViewById(R.id.editage);


        TextView chgpho = (TextView)findViewById(R.id.chgpho);
        chgpho.setText(new Languages().ProfileCheckPhoto());



        TextView activeTexview = (TextView) findViewById(R.id.active22);
        activeTexview.setText(new Languages().ProfileActive());

        TextView nameTexview = (TextView) findViewById(R.id.lname);
        nameTexview.setText(new Languages().ProfileTextviewName());




        editname.setHint(new Languages().ProfileEditName());

        TextView ageTexview = (TextView) findViewById(R.id.lage);
        ageTexview.setText(new Languages().ProfileTextviewAge());

        editage.setHint(new Languages().ProfileEditAge());

        TextView sexTexview = (TextView) findViewById(R.id.sex);
        sexTexview.setText(new Languages().ProfileTextviewSex());

        TextView maleTexview = (TextView) findViewById(R.id.male);
        maleTexview.setText(new Languages().ProfileTextviewMale());

        TextView femaleTexview = (TextView) findViewById(R.id.female);
        femaleTexview.setText(new Languages().ProfileTextviewFemale());

        TextView changegeo = (TextView) findViewById(R.id.active32);
        changegeo.setText(new Languages().ProfileChangegeo());


        TextView delank = (TextView) findViewById(R.id.delank);
        delank.setText(new Languages().ProfileDelank());


        delank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Создаем диалоговое окно
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Удаление анкеты")
                        .setMessage("Вы уверены, что хотите удалить свою анкету? Это действие скроет ваш профиль от других пользователей.")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Код удаления (создание поля profile_delete)
                                FirebaseDatabase.getInstance().getReference()
                                        .child(new Config2().tab_users)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("profile_delete")
                                        .setValue(1);

                                Toast.makeText(getApplicationContext(), "Анкета успешно удалена", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Просто закрываем диалог, ничего не делая
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert) // Иконка предупреждения
                        .show();
            }
        });






        TextView restoreank = (TextView) findViewById(R.id.restore_profile_btn);


        restoreank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Путь к полю profile_delete для текущего пользователя
                FirebaseDatabase.getInstance().getReference()
                        .child(new Config2().tab_users)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("profile_delete")
                        .removeValue() // ЭТО УДАЛЯЕТ ПОЛЕ ПОЛНОСТЬЮ
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // После удаления сработает ваш ValueEventListener,
                                // и интерфейс переключится сам (или можно вызвать recreate())
                                //recreate();
                                Toast.makeText(getApplicationContext(), "Профиль восстановлен", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        changegeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getloc = new Intent(getApplication(), getloc.class);
                getloc.putExtra("getloc", "ok");
                startActivity(getloc);
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        setTitle(new Languages().TitleProfile());


        toolbar.setTitleTextColor(Color.WHITE);

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());

        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);


        // TypedArray imgs = getResources().obtainTypedArray(R.array.kamran2);

        //imgs.getResourceId(0, -1);

        //  img.setImageResource(new Languages().ImagesUploadPhoto());



        // RadioButtonDrawable male =  findViewById(R.id.male);

        // Log.i("RadioButt:", String.valueOf(male.isChecked()));



        editage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0) {

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_age").setValue(editage.getText().toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_age").setValue(editage.getText().toString());

            }
            @Override
            public void afterTextChanged(Editable s) {
                // FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_age").setValue(editage.getText().toString());

                // TODO Auto-generated method stub
            }
        });


        editname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_name").setValue(editname.getText().toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });



        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("imginfo:",String.valueOf(dataSnapshot));

                if (dataSnapshot.hasChild("profile_name"))
                {
                    //editname.setText(dataSnapshot.child("profile_name").getValue().toString());
                    editname.setHint(dataSnapshot.child("profile_name").getValue().toString());
                }

                if (dataSnapshot.hasChild("profile_age"))
                {
                    //editname.setText(dataSnapshot.child("profile_name").getValue().toString());
                    editage.setHint(dataSnapshot.child("profile_age").getValue().toString());
                }
                final ImageView img = (ImageView) findViewById(R.id.img);
                final ImageView r_right = (ImageView) findViewById(R.id.r_right);
                final ImageView r_left = (ImageView) findViewById(R.id.r_left);

                if (dataSnapshot.hasChild("profile_photo"))
                {


                    String str = dataSnapshot.child("profile_photo").getValue().toString();
                    final String url_file_name;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
                        url_file_name = getExternalCacheDir() + "/"  + URLUtil.guessFileName(str, null, null);
                    }else {

                        url_file_name = Environment.getExternalStorageDirectory().toString() + "/Znak/cache/" + URLUtil.guessFileName(str, null, null);
                    }
                    final File file = new File(url_file_name);

                    if(file.exists())
                    {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        String photopath = new File(file.getPath()).getAbsolutePath();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(photopath, options);

                        Log.d("tester:",photopath);

                        int i_width;
                        int i_height;

                        int imageHeight;
                        int imageWidth;

                        imageHeight = options.outHeight;
                        imageWidth = options.outWidth;

                        //  Log.d("res_file",url_file_name_res);

                        if (imageWidth>width) {

                            i_width = width;
                            i_height = (width*imageHeight)/imageWidth;

                        }else
                        {
                            i_width = imageWidth;
                            i_height = imageHeight;
                        }

                        img.requestLayout();
                        img.getLayoutParams().height = i_height;
                        img.getLayoutParams().width = i_width;

                        img.setImageResource(new Languages().ImagesUploadPhoto());

                        Glide.with(getApplicationContext())
                                .load(url_file_name)
                                .apply(new RequestOptions()
                                        .signature(new ObjectKey(url_file_name))
                                        .fitCenter()
                                        .format(DecodeFormat.PREFER_ARGB_8888)
                                        .override(Target.SIZE_ORIGINAL))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        Log.d("glide_ex:",e.getMessage());
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {

                                        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
                                        progressBar2.setVisibility(View.GONE);

                                        return false;
                                    }
                                })
                                .into(img);

                        r_left.setVisibility(View.VISIBLE);
                        r_right.setVisibility(View.VISIBLE);

                        // imageview.setImageResource(0);

                    }else {

                        Log.d("хуйня с фоткой","загрузка хуйни");

                        new FileLoadingTask(
                                str,
                                new File(url_file_name),
                                new TinderCard.FileLoadingListener() {
                                    @Override
                                    public void onBegin() {
                                    }

                                    @Override
                                    public void onSuccess() {

                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(new File(file.getPath()).getAbsolutePath(), options);
                                        int imageHeight = options.outHeight;
                                        int imageWidth = options.outWidth;

                                        int i_width;
                                        int i_height;

                                        if (imageWidth>width) {

                                            i_width = width;
                                            i_height = (width*imageHeight)/imageWidth;

                                        }else
                                        {
                                            i_width = imageWidth;
                                            i_height = imageHeight;
                                        }

                                        img.requestLayout();
                                        img.getLayoutParams().height = i_height;
                                        img.getLayoutParams().width = i_width;

                                        img.setImageResource(new Languages().ImagesUploadPhoto());


                                        Glide.with(getApplicationContext())
                                                .load(url_file_name)
                                                .apply(new RequestOptions()
                                                        .signature(new ObjectKey(url_file_name))
                                                        .fitCenter()
                                                        .format(DecodeFormat.PREFER_ARGB_8888)
                                                        .override(Target.SIZE_ORIGINAL))
                                                .listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                                        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
                                                        progressBar2.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .into(img);

                                        r_left.setVisibility(View.VISIBLE);
                                        r_right.setVisibility(View.VISIBLE);

                                        // Log.d("Файл:","Загрузка завершена, путь файла - " + url_file_name);
                                    }

                                    @Override
                                    public void onFailure(Throwable cause) {
                                    }

                                    @Override
                                    public void onEnd() {

                                    }
                                }).execute();
                    }
                }else
                {
                    ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
                    progressBar2.setVisibility(View.GONE);

                    img.setImageResource(new Languages().ImagesUploadPhoto());
                    r_left.setVisibility(View.GONE);
                    r_right.setVisibility(View.GONE);
                }

                if (dataSnapshot.hasChild("profile_gender"))
                {
                    com.rey.material.widget.RadioButton male = (com.rey.material.widget.RadioButton) findViewById(R.id.male);
                    com.rey.material.widget.RadioButton female = (com.rey.material.widget.RadioButton) findViewById(R.id.female);

                    if (dataSnapshot.child("profile_gender").getValue().toString().equals("m"))
                    {
                        male.setChecked(true);
                        female.setChecked(false);
                    }else
                    {
                        male.setChecked(false);
                        female.setChecked(true);
                    }

                    Log.i("gender:", dataSnapshot.child("profile_gender").getValue().toString());
                }

                com.rey.material.widget.Switch switcher = (com.rey.material.widget.Switch) findViewById(R.id.switcher);
                //Log.i("Swich:", String.valueOf(switcher.isChecked()));

                ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
                imageButton.setImageResource(new Languages().ImagesForward());

                TextView acttext = (TextView)findViewById(R.id.acttext);

                acttext.setText(new Languages().ProfileActtext());

                if (dataSnapshot.hasChild("profile_active"))
                {
                    if (dataSnapshot.child("profile_active").getValue().toString().equals("on"))
                    {
                        switcher.setChecked(true);
                        acttext.setVisibility(View.GONE);
                        imageButton.setVisibility(VISIBLE);

                    }else
                    {
                        switcher.setChecked(false);
                        acttext.setVisibility(VISIBLE);
                        imageButton.setVisibility(View.GONE);
                    }
                }else
                {
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_active").setValue("on");
                    switcher.setChecked(true);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        // setSupportActionBar(toolbar);





    }




    public void onClick(View v) {

        com.rey.material.widget.RadioButton male = (com.rey.material.widget.RadioButton) findViewById(R.id.male);
        com.rey.material.widget.RadioButton female = (com.rey.material.widget.RadioButton) findViewById(R.id.female);
        com.rey.material.widget.Switch switcher = (com.rey.material.widget.Switch) findViewById(R.id.switcher);



        switch (v.getId()) {

            case R.id.imageButton:

                Log.i("imageButton: ","OK");

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        List<String> array = new ArrayList<String>();

                        if (dataSnapshot.hasChild("profile_name"))
                        {
                            // на досуге добавим если поле есть, но пустое

                        }
                        else {
                            array.add(new Languages().ProfileCheckName());
                        }


                        if (dataSnapshot.hasChild("profile_age"))
                        {

                            if (Integer.parseInt(dataSnapshot.child("profile_age").getValue().toString())<18)
                            {
                                Log.d("curr_age: ", dataSnapshot.child("profile_age").getValue().toString());
                                array.add(new Languages().ProfileCheckAge());
                            }
                        }
                        else {
                            array.add(new Languages().ProfileCheckAge());
                        }


                        if (dataSnapshot.hasChild("profile_photo"))
                        {

                        }
                        else {
                            array.add(new Languages().ProfileCheckPhoto());
                        }

                        if (dataSnapshot.hasChild("profile_gender"))
                        {

                        }
                        else {
                            array.add(new Languages().ProfileCheckSex());
                        }

                        com.rey.material.widget.Switch switcher = (com.rey.material.widget.Switch) findViewById(R.id.switcher);
                        //Log.i("Swich:", String.valueOf(switcher.isChecked()));


                        Log.i("arraysize2:",String.valueOf(array.size()));

                        if (array.size()!=0) {

                            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                            alertDialog.setTitle(new Languages().ProfileWarning());
                            alertDialog.setMessage(new Languages().ProfileRequired() + array.toString());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else
                        {


                            // "https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg"

                            Log.i("идем знакомиться!", "GOGOGO!");

                            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);


                            //usersScreen.putExtra("currlng", currlng);
                            //usersScreen.putExtra("currlat", currlat);

                            startActivity(usersScreen);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }});
                return;



            case R.id.r_right:

                RotateImage(90);

                return;

            case R.id.r_left:

                RotateImage(270);

                return;

            case R.id.switcher:




                //female.setChecked(true);

                if (switcher.isChecked())
                {
                    Log.i("SwchButt2:", "ON");

                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_active").setValue("on");

                }else
                {
                    Log.i("SwchButt2:", "OFF");
                    FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_active").setValue("off");

                }

                //Log.i("SwchButt2:", String.valueOf(male.isChecked()));

                return;



            case R.id.male:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_gender").setValue("m");


                female.setChecked(false);


                Log.i("RadioButt2:", String.valueOf(male.isChecked()));

                return;

            case R.id.female:

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_gender").setValue("f");



                male.setChecked(false);




                Log.i("RadioButt2:", String.valueOf(male.isChecked()));

                return;



            case R.id.chgpho:

                Log.i("ChangePhoto: ","OK");



                try {

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);



                    builder.setTitle(new Languages().ProfileAddPhoto());

                    final CharSequence[] options = new CharSequence[]{new Languages().ProfileTakePicture(),new Languages().ProfileSelectPhoto(), new Languages().ProfileCancelPhoto()};

                    //  options = new CharSequence[] {"Take a picture", "Select photo","Cancel"};





                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals(new Languages().ProfileTakePicture())) {
                                dialog.dismiss();

                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //startActivityForResult(intent, 0);

                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {

                                        Toast.makeText(getApplicationContext(), "rcreateImageFile error.", Toast.LENGTH_LONG).show();

                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {

                                        Uri photoURI;

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
                                            photoURI = FileProvider.getUriForFile(ProfileActivity.this, "io.cordova.test2_6720b.fileprovider", photoFile);
                                            Log.d("pic_e:", photoFile.toString() + " # " + getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                                        }else {
                                            photoURI = Uri.fromFile(photoFile);
                                        }
                                        //Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".io.cordova.bizone2.provider", photoFile);

                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(takePictureIntent, 0);
                                    }
                                }else
                                {
                                    Toast.makeText(getApplicationContext(), "resolveActivity error.", Toast.LENGTH_LONG).show();
                                }


                            } else if (options[item].equals(new Languages().ProfileSelectPhoto())) {
                                dialog.dismiss();

                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                if (pickPhoto.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go

                                    startActivityForResult(pickPhoto, 1);
                                }





                            } else if (options[item].equals(new Languages().ProfileCancelPhoto())) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } catch (Exception e) {
                    Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return;
        }
    }

    void UploadPicture()
    {
        String filesdir;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
            filesdir =  getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }else {
            filesdir =  Environment.getExternalStorageDirectory().toString() + "/Znak/files";
        }
        final File photo = new File(filesdir,"photo.jpg");
        File photo2;


        if (photo.exists()) {

            Log.i("Phores:","Фото существует, путь - " + filesdir);



            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(filesdir + "/photo.jpg", options);
            int width = options.outWidth;
            int height = options.outHeight;

            if (width>1000) {

                int nh = 1000 * height / width;

                BitmapFactory.Options options2 = new BitmapFactory.Options();

                Bitmap mybit = BitmapFactory.decodeFile(filesdir + "/photo.jpg", options2);
                options2.inJustDecodeBounds = false;

                Bitmap resized = Bitmap.createScaledBitmap(mybit, 1000, nh, true);

                FileOutputStream fOut;
                try {
                    File small_picture = new File(filesdir, "photo.jpg");
                    fOut = new FileOutputStream(small_picture);
                    // 0 = small/low quality, 100 = large/high quality
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    resized.recycle();

                    photo2 = new File(filesdir,"photo.jpg");

                } catch (Exception e) {
                    Log.e("file err:", "Failed to save/resize image due to: " + e.toString());
                    photo2 = photo;
                }
            }
            else
            {
                photo2 = photo;
            }
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();


            File file = new File(photo2.getPath());

            if (!file.exists()) {
                Log.e("UPLOAD", "ФАЙЛ НЕ НАЙДЕН ПО ПУТИ: " + file.getAbsolutePath());
                return; // Останавливаем, если файла нет
            }


            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "pho_" + ts + ".jpg", requestFile);





            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Видеть всё тело запроса

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://rieltorov.net")
                    .client(client) // Добавляем логгер
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();




            ApiService service = retrofit.create(ApiService.class);





// 3. Отправка на сервер
            service.uploadImage(body).enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {


                    Log.d("OTVET3: ", response.body().status);

                    if (response.isSuccessful() && response.body() != null) {

                        Log.d("OTVET3: ", response.body().status);

                        String downloadUrl = response.body().url; // URL от вашего сервера

                        // 4. Сохранение ссылки в Firebase Database (как и раньше)
                        FirebaseDatabase.getInstance().getReference()
                                .child(new Config2().tab_users)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("profile_photo")
                                .setValue(downloadUrl);

                        Log.d("Загрузка: ", "Успешно, ссылка - " + downloadUrl);






                      //  FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photo").setValue(downloadUrl.toString());

                        if (photo.delete()) {




                            Toast.makeText(getApplication(), new Languages().PhotoAdded(), Toast.LENGTH_LONG).show();

                            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Simh5X1gVCbqkH0qPJ5N6ouqKTx1")) {

                                // if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals("HJyDKc1CmUOp3o1yvtaSAg6Zecv2")) {

                                Log.i("tags","уведомление отправлено, юзер - " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child("Simh5X1gVCbqkH0qPJ5N6ouqKTx1").addListenerForSingleValueEvent(new ValueEventListener() {

                                    //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child("HJyDKc1CmUOp3o1yvtaSAg6Zecv2").addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Log.i("tags2",dataSnapshot.child("device_token").getValue().toString());

                                        sendPost(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), downloadUrl.toString(),dataSnapshot.child("device_token").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message

                                        // ...
                                    }
                                });
                            }


                        }else   Log.i("проблема с удалением","не могу удать фото");





                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Log.d("Ошибка загрузки: ", t.getMessage());
                }
            });










        }else {
            Log.i("Phores:","Фото отсутствует!, путь - " + filesdir);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_anketa, menu);


        menu.findItem(R.id.friends).setTitle(new Languages().MenuFriends());
        menu.findItem(R.id.logout).setTitle(new Languages().MenuSignout());
        menu.findItem(R.id.messages).setTitle(new Languages().MenuMessages());
        menu.findItem(R.id.likes).setTitle(new Languages().MenuLikes());
        menu.findItem(R.id.about).setTitle(new Languages().MenuAbout());






        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {


        final MenuItem friends_item = menu.findItem(R.id.friends);
        final MenuItem messages_item = menu.findItem(R.id.messages);


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //    Log.i("imginfo:",String.valueOf(dataSnapshot));

                if (dataSnapshot.hasChild("profile_photo"))
                {
                    friends_item.setEnabled(true);
                    messages_item.setEnabled(true);

                }else
                {
                    friends_item.setEnabled(false);
                    messages_item.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});


        friends_item.setEnabled(false);
        messages_item.setEnabled(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.friends:

            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
            startActivity(usersScreen);


            //Toast.makeText(this, "Аватарка обновлена", Toast.LENGTH_LONG).show();
            return true;


        case R.id.likes:

            Intent likesScreen = new Intent(getApplication(), ActivityLikes.class);
            startActivity(likesScreen);


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

            functions.About(ProfileActivity.this);

            return(true);

        case R.id.logout:

            signOut();

            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    public void RotateImage(final int degrees)
    {
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("profile_photo"))
                {
                    Long tsLong2 = System.currentTimeMillis()/1000;
                    String ts2 = tsLong2 + ".jpg";

                    String str = dataSnapshot.child("profile_photo").getValue().toString();

                    final String url_file_name;
                    final String url_file_name_res;


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");

                        url_file_name = getExternalCacheDir() + "/"  + URLUtil.guessFileName(str, null, null);
                        url_file_name_res = getExternalCacheDir() + "/"  + ts2;
                    }else {

                        url_file_name = Environment.getExternalStorageDirectory().toString() + "/Znak/cache/" + URLUtil.guessFileName(str, null, null);
                        url_file_name_res = Environment.getExternalStorageDirectory().toString() + "/Znak/cache/" + ts2;
                    }
                    final File file = new File(url_file_name);

                    if(file.exists())
                    {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        String photopath = new File(file.getPath()).getAbsolutePath();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(photopath, options);

                        Log.d("tester:", photopath);

                        Bitmap bmp = BitmapFactory.decodeFile(url_file_name);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(degrees);

                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                        FileOutputStream fOut;
                        try {
                            File rotatedFile = new File(url_file_name_res);

                            fOut = new FileOutputStream(url_file_name_res);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                            fOut.flush();
                            fOut.close();








                            // 1. Создаем RequestBody из нового файла
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), rotatedFile);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("image", ts2, requestFile);

                            // 2. Настройка Retrofit (используем ваш домен)
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://rieltorov.net")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            ApiService service = retrofit.create(ApiService.class);


                            service.uploadImage(body).enqueue(new Callback<UploadResponse>() {
                                @Override
                                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String newServerUrl = response.body().url;

                                        // 4. Обновляем ссылку в Firebase Database
                                        FirebaseDatabase.getInstance().getReference()
                                                .child(new Config2().tab_users)
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("profile_photo")
                                                .setValue(newServerUrl);

                                        Log.d("Rotate", "Успешно повернуто и загружено: " + newServerUrl);

                                        // Удаляем временный файл после загрузки
                                        if (rotatedFile.exists()) rotatedFile.delete();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UploadResponse> call, Throwable t) {
                                    Log.e("Rotate Error", "Ошибка загрузки: " + t.getMessage());
                                }
                            });


















                        } catch (FileNotFoundException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});
    }
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public void sendPost(final String extra, final String extra2, final String title, final String device_token) {




/*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");


                    conn.setRequestProperty("Authorization","key=AAAAIF01ca4:APA91bGX0kMaXMAl3QNyq_QxiRZFari8jb43cVHtktYXgKuFdmnfBzcPF1V89nNf9Otz8xY3aG0ADA5Xo9axCeijovWIlIgWKrYEEs0AYTrfPmp6sD1CDW3Y16tSsY1C5vHqdIiQfYMy");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject to = new JSONObject();

                    // Для отправки на одно устройство


                    to.put("to", device_token);

                    JSONObject notification = new JSONObject();

                    notification.put("click_action", "MyAction2");
                    notification.put("sound", "default");
                    notification.put("icon", "mess");
                    notification.put("title", "Новая анкета");
                    notification.put("body", title);

                    JSONObject data = new JSONObject();

                    data.put("extram", extra);
                    data.put("extram2", extra2);

                    //  to.put("registration_ids",registration_ids);

                    to.put("notification",notification);

                    to.put("data",data);

                    String json = to.toString().replace("\"[", "[").replace("]\"", "]").replace("'", "\"");

                    // Log.i("JSON", json);

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                    bw.write(json);
                    bw.flush();
                    bw.close();

                    Log.i("STATUS77", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG77" , conn.getResponseMessage());

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

 */
        Thread thread = new Thread(() -> {
            try {
                // 🔗 Адрес вашего PHP-скрипта
                URL url = new URL("https://rieltorov.net/push_new_ank.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// Формируем POST-параметры
                String postData = "extra=" + URLEncoder.encode(extra, "UTF-8") +
                        "&extra2=" + URLEncoder.encode(extra2, "UTF-8") +
                        "&title=" + URLEncoder.encode(title, "UTF-8") +
                        "&registration_ids=" + URLEncoder.encode(device_token, "UTF-8");

                Log.d("POST_DATA: ",postData);

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.i("PUSH_SEND", "Response code: " + responseCode);

                // Читаем ответ для отладки
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.i("PUSH_SEND", "Response: " + response.toString());

                conn.disconnect();

            } catch (Exception e) {
                Log.e("PUSH_SEND", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        thread.start();
    }


    private File createImageFile() throws IOException {
        // Create an image file name

        File image;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
            image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"photo.jpg");
        }else {
            image = new File(Environment.getExternalStorageDirectory().toString() + "/Znak/files", "photo.jpg");

        }
        return image;
    }

    private void signOut() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = String.valueOf(tsLong);
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid == null) {
            // Если пользователь уже не в Firebase — просто идём на логин
            goToLogin();
            return;
        }

        // 1. Обновляем last_activity в Firebase
        FirebaseDatabase.getInstance()
                .getReference()
                .child(new Config2().tab_users)
                .child(uid)
                .child("curr_activity")
                .setValue(ts)
                .addOnSuccessListener(aVoid -> {
                    // 2. Выход из Firebase Auth
                    FirebaseAuth.getInstance().signOut();

                    // 3. 🔥 Современный выход из Google (НЕ требует mGoogleApiClient!)
                    GoogleSignIn.getClient(ProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .signOut()
                            .addOnCompleteListener(ProfileActivity.this, task -> {
                                Log.i("GOOGLE_AUTH", "✅ Google signOut completed: " +
                                        (task.isSuccessful() ? "success" : task.getException()));

                                // 4. Очистка локальных данных
                                clearLocalSession();

                                // 5. Переход на экран входа
                                goToLogin();
                            });
                })
                .addOnFailureListener(e -> {
                    // Даже если не удалось обновить timestamp — всё равно выходим
                    Log.w("SIGN_OUT", "Failed to update curr_activity: " + e.getMessage());
                    FirebaseAuth.getInstance().signOut();
                    clearLocalSession();
                    goToLogin();
                });
    }

    // 🔹 Вспомогательный метод: переход на логин
    private void goToLogin() {
        Intent login = new Intent(ProfileActivity.this, loginEmail.class);
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 🔥 Сброс стека активностей
        startActivity(login);
        finish(); // Закрываем текущую, чтобы нельзя было вернуться назад
    }

    // 🔹 Вспомогательный метод: полная очистка сессии
    private void clearLocalSession() {
        // Очистка SharedPreferences
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().clear().apply();

        // Сброс глобальных переменных (если есть)
        // currentUser = null;
        // userToken = null;

        Log.d("SESSION", "🧹 Local session cleared");
    }
}
