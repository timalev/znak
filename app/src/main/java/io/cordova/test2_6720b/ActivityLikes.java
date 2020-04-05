package io.cordova.test2_6720b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityLikes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        // FirebaseAuth.getInstance().getCurrentUser().getUid()

        FirebaseDatabase.getInstance().getReference().child("zn_likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                Collection<Object> values = td.values();

                List list = new ArrayList(values);

                Log.d("alllikes2", dataSnapshot.getKey() + ", " + String.valueOf(list));
                Log.d("alllikes3", String.valueOf(td));

                for (int l = 0; l < list.size(); l++) {

                    // Log.d("List2: ", list.get(l).getClass().getName() + ", размер: " + list.toString());

                    HashMap<String, Object> itemsList = (HashMap<String, Object>) list.get(l);

                    for (Map.Entry<String, Object> entry : itemsList.entrySet()) {

                        String key = entry.getKey();
                        Object value = entry.getValue();

                        Log.d("alllikes4", key + " / " +String.valueOf(value));

                    }
                }

                /*

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    final String Key = child.getKey();

                    if (!Key.equals("total_likes"))
                    {

                        final String CommText = String.valueOf(child.getValue());



                        Log.d("alllikes", Key + ", " + CommText);


                        










                    }


                }
*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            ;
        });
    }
}
