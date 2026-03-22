package io.cordova.test2_6720b;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadService extends Service {


    private String filesdir = "Znak/files";

    private FirebaseAuth mAuth;
    private StorageReference videoRef;
    private DatabaseReference mPostReference;

    private Config config = new Config();

    private String tab = config.tab("users");
    private String tab_online = config.tab("online");
    private String tab_notific = config.tab("notific");

    private String tab_messages = config.tab("messages");





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        final double lat = intent.getExtras().getDouble("latitude");
        final double lng = intent.getExtras().getDouble("longitude");

       // Toast.makeText(getApplication(), String.valueOf(lat) + ", " + String.valueOf(lng), Toast.LENGTH_LONG).show();



      // mHandler.postDelayed(ToastRunnable, 5000);



        final File video = new File("sdcard/" + filesdir,"story.mp4");

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource("sdcard/" + filesdir + "/story.mp4");
        final int vwidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        final int vheight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        try {
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (video.exists()) {

          // Toast.makeText(getApplication(), String.valueOf(vheight) + ", " + String.valueOf(vwidth), Toast.LENGTH_LONG).show();

            mPostReference = FirebaseDatabase.getInstance().getReference().child(tab);

            mAuth = FirebaseAuth.getInstance();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            final StorageReference storageRef = storage.getReference();

            videoRef = storageRef.child(mAuth.getCurrentUser().getUid() + ".mp4");

            // Toast.makeText(getApplication(), "Файл существует! Размер файла: " + video.length(), Toast.LENGTH_LONG).show();

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
                        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadUrl = uri;

                        Log.d("Загрузка: ", "Успешно, ссылка на видео - " + downloadUrl);

                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("video").setValue(downloadUrl.toString());
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("type").setValue("mp4");

                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("video_w").setValue(vwidth);
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("video_h").setValue(vheight);


                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("coords2").child("lat").setValue(lat);
                        mPostReference.child(mAuth.getCurrentUser().getUid()).child("coords2").child("lng").setValue(lng);

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();

                       // FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").child("user").setValue(mAuth.getCurrentUser().getUid());
                       // FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").child("video").setValue(downloadUrl.toString());
                       // FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").child("time").setValue(ts);

                        /*
                        Notific notific = new Notific(downloadUrl.toString(),mAuth.getCurrentUser().getUid(),ts,"mp4",mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getPhotoUrl().toString());

                        FirebaseDatabase.getInstance().getReference().child(tab_notific).child("uid").setValue(notific);
*/

                        // Удаление комментов при загрузки нового видео

                        FirebaseDatabase.getInstance().getReference().child(tab_messages).orderByChild("page_for_comment").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            FirebaseDatabase.getInstance().getReference().child(tab_messages).child(child.getKey()).removeValue();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                                    }
                                });



                      //  Toast.makeText(getApplication(), "Видео успешно добавлено!", Toast.LENGTH_LONG).show();


                        if (video.delete()) {

                            Toast.makeText(getApplication(), "Видео успешно добавлено!", Toast.LENGTH_LONG).show();

                            stopSelf();

                        }

                            }
                        });
                        //Notific notific = new Notific(mAuth.getCurrentUser().getUid(),downloadUrl.toString());

                        //FirebaseDatabase.getInstance().getReference().child(tab_notific).child(ts).setValue(notific);

                    }
                });



            } catch (Exception e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("Ошибка файла: ", e.getMessage());
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(getApplication(), "Файл отсутствует!", Toast.LENGTH_LONG).show();
        }



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }





    @Override
    public void onDestroy() {
      // mHandler.removeCallbacksAndMessages(null);
    }



}
