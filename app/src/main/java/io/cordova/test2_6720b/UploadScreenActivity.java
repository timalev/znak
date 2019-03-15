package io.cordova.test2_6720b;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class UploadScreenActivity extends AppCompatActivity {

    private String filesdir = "Android/data/com.levendeev.map.geostories4/files";
    private FirebaseAuth mAuth;
    private StorageReference videoRef;
    private DatabaseReference mPostReference;

    private Config config = new Config();

    private String tab = config.tab("users");
    private String tab_online = config.tab("online");
    private String tab_notific = config.tab("notific");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_screen);

        File video = new File("sdcard/" + filesdir,"story.mp4");

        video.delete();

/*
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,3);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("sdcard/" + filesdir,"story.mp4")));

        startActivityForResult(cameraIntent,1);


*/






    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
       // Toast.makeText(getApplication(), requestCode + ", " + resultCode + ", " + data, Toast.LENGTH_LONG).show();

        File video = new File("sdcard/" + filesdir,"story.mp4");

        if (video.exists()) {

            mPostReference = FirebaseDatabase.getInstance().getReference().child(tab);

            mAuth = FirebaseAuth.getInstance();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            final StorageReference storageRef = storage.getReference();

            videoRef = storageRef.child(mAuth.getCurrentUser().getUid() + ".mp4");




            try {

                InputStream stream2 = new FileInputStream(video);

                UploadTask uploadTask = videoRef.putStream(stream2);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("Ошибка загрузки: ", "не успешно");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("video").setValue(downloadUrl.toString());


                        FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").child("user").setValue(mAuth.getCurrentUser().getUid());
                        FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").child("video").setValue(downloadUrl.toString());

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();


                        startActivity(new Intent(getApplication(), MapsActivity.class));



                        //Notific notific = new Notific(mAuth.getCurrentUser().getUid(),downloadUrl.toString());

                        //FirebaseDatabase.getInstance().getReference().child(tab_notific).child(ts).setValue(notific);





                    }
                });




            } catch (Exception e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("Ошибка файла: ", e.getMessage());
                e.printStackTrace();
            }

            //Toast.makeText(getApplication(), "Файл существует! Размер файла: " + video.length(), Toast.LENGTH_LONG).show();

        }
        /*
        else
            Toast.makeText(getApplication(), "Файл отсутствует!", Toast.LENGTH_LONG).show();
            */


    }

}
