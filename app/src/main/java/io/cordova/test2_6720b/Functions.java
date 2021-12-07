package io.cordova.test2_6720b;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class Functions {



    public void About(Context context)
    {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo("io.cordova.test2_6720b", 0);
            String version = pInfo.versionName;

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(new Languages().AboutTitle());
            alertDialog.setMessage(new Languages().AboutBody() + version);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void getDropboxIMGSize(Uri uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

    }


}
