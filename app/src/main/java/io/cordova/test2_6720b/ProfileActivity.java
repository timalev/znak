package io.cordova.test2_6720b;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.drawable.RadioButtonDrawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static io.cordova.test2_6720b.UsersActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity {

    private String filesdir =  Environment.getExternalStorageDirectory().toString() + "/Android/data/io.cordova.test2_6720b/files";


    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private StorageReference photoRef;



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
        else if (requestCode == 1)
        {
            if (resultCode == RESULT_OK) {

                //Toast.makeText(this, "Gallery picture OK", Toast.LENGTH_SHORT).show();

                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(filesdir + "/photo.jpg");
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    UploadPicture();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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






        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName());

        Log.i("Опять с файлами гемор:",getPackageName() + " путь: " + Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName());

        if (!externalAppDir.exists()) {

            if (externalAppDir.mkdir()) {

                File externalFiles = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/files");
                externalFiles.mkdir();

                File externalCache = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/cache");

                if (externalCache.mkdir())
                {
                    File externalDebug = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/cache/debug");
                    externalDebug.mkdir();

                }
            }else {
                Log.i("Опять с файлами гемор3:","Нет ни хуя");
            }
        }else
        {
            Log.i("Опять с файлами гемор2:","все ок");
        }


        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        setTitle("Моя анкета");

        toolbar.setTitleTextColor(Color.WHITE);

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").setValue(this.getClass().getSimpleName());

        Long tsLong2 = System.currentTimeMillis()/1000;
        String ts2 = tsLong2.toString();
        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("curr_activity").onDisconnect().setValue(ts2);


        final EditText editname   = (EditText)findViewById(R.id.editname);
        //editname.setSelection(editname.getText().length());

        final EditText editage   = (EditText)findViewById(R.id.editage);





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

                if (dataSnapshot.hasChild("profile_photo"))
                {
                    ImageView img = (ImageView) findViewById(R.id.img);

                Glide
                        .with(getApplicationContext())
                        .load(dataSnapshot.child("profile_photo").getValue().toString())
                        .asBitmap()
                        .error(R.drawable.noavatar)
                        .centerCrop()
                        .into(img);
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
                TextView acttext = (TextView)findViewById(R.id.acttext);

                if (dataSnapshot.hasChild("profile_active"))
                {
                    if (dataSnapshot.child("profile_active").getValue().toString().equals("on"))
                    {
                        switcher.setChecked(true);
                        acttext.setVisibility(View.GONE);
                        imageButton.setVisibility(View.VISIBLE);

                    }else
                    {
                        switcher.setChecked(false);
                        acttext.setVisibility(View.VISIBLE);
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

                        com.rey.material.widget.Switch switcher = (com.rey.material.widget.Switch) findViewById(R.id.switcher);
                        //Log.i("Swich:", String.valueOf(switcher.isChecked()));


                        Log.i("arraysize2:",String.valueOf(array.size()));

                        if (array.size()!=0) {

                            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                            alertDialog.setTitle("Внимание!");
                            alertDialog.setMessage("Нужно добавить: " + array.toString());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else
                        {
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

                    // Log.i("ChangePhoto: ","OK");

                    try {

                            final CharSequence[] options = {"Сделать фото", "Выбрать фото","Отменить"};
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setTitle("Добавление фотографии");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (options[item].equals("Сделать фото")) {
                                        dialog.dismiss();

                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        //startActivityForResult(intent, 0);

                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            // Create the File where the photo should go
                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile("photo.jpg");
                                            } catch (IOException ex) {

                                                Toast.makeText(getApplicationContext(), "rcreateImageFile error.", Toast.LENGTH_LONG).show();

                                            }
                                            // Continue only if the File was successfully created
                                            if (photoFile != null) {

                                                Uri photoURI = Uri.fromFile(photoFile);

                                                //Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".io.cordova.bizone2.provider", photoFile);

                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                startActivityForResult(takePictureIntent, 0);
                                            }
                                        }else
                                        {
                                            Toast.makeText(getApplicationContext(), "resolveActivity error.", Toast.LENGTH_LONG).show();
                                        }


                                    } else if (options[item].equals("Выбрать фото")) {
                                        dialog.dismiss();

                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                        if (pickPhoto.resolveActivity(getPackageManager()) != null) {
                                            // Create the File where the photo should go

                                            startActivityForResult(pickPhoto, 1);
                                        }





                                    } else if (options[item].equals("Отменить")) {
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


            photoRef = FirebaseStorage.getInstance().getReference().child("pho_" + ts + ".jpg");


            try {

                InputStream stream2 = new FileInputStream(photo2);

                UploadTask uploadTask = photoRef.putStream(stream2);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("Ошибка загрузки: ", "не успешно");

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photo").setValue(downloadUrl.toString());

                        if (photo.delete()) {

                            Toast.makeText(getApplication(), "Фото успешно добавлено!", Toast.LENGTH_LONG).show();

                        }

                    }
                });

            } catch (Exception e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("Ошибка файла: ", e.getMessage());
                e.printStackTrace();
            }


        }else {
            Log.i("Phores:","Фото отсутствует!, путь - " + filesdir);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anketa, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.friends:

            Intent usersScreen = new Intent(getApplication(), UsersActivity.class);
            startActivity(usersScreen);


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
            //add the function to perform here
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    private File createImageFile(String fileName) throws IOException {
        // Create an image file name

        File image = new File(filesdir,fileName);

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
