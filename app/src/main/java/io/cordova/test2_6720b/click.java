package io.cordova.test2_6720b;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by timofey on 27.09.17.
 */

public class click implements View.OnClickListener
{

    private static Context context;

    String curruser;

    public click(Context c, String curruser) {
        this.curruser = curruser;
        context = c;
    }

    @Override
    public void onClick(View v)
    {
        Intent nextScreen = new Intent(context, VideoActivity.class);

        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        nextScreen.putExtra("curruser", curruser);

        context.startActivity(nextScreen);



        //Toast.makeText(context, curruser, Toast.LENGTH_LONG).show();
    }

};