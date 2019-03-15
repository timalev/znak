package io.cordova.test2_6720b;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;

public class Functions {



    public void About(Context context)
    {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo("io.cordova.test2_6720b", 0);
            String version = pInfo.versionName;

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("О приложении");
            alertDialog.setMessage("Текущая версия приложения: " + version);
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
}
