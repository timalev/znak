package io.cordova.test2_6720b;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends android.app.Application   {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
       // DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("test2-6720b");
        //scoresRef.keepSynced(false);

        FirebaseApp.initializeApp(this);
    }
}
