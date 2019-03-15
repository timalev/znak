package io.cordova.test2_6720b;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 777;
    private String filesdir = "Android/data/com.levendeev.map.geostories4/files";


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Загрузка видео: ", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE)
        {
            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Take picture OK", Toast.LENGTH_SHORT).show();

                //UploadPicture();

            } else {
                Toast.makeText(this, "Take picture Error", Toast.LENGTH_SHORT).show();
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


        setContentView(R.layout.activity_photo);

        Button Button1 = (Button) findViewById(R.id.But3);

        Button1.setOnClickListener(this);

       // Toast.makeText(this, "Кнопка Photo нажата.", Toast.LENGTH_LONG).show();





    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.But3:

                TakePicture();


                break;
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name

        File image = new File("sdcard/" + filesdir,"photo.jpg");

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public void TakePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("sdcard/" + filesdir,"photo.jpg")));

       // startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = Uri.fromFile(photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }


    }
}
