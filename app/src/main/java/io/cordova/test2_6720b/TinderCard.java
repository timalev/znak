package io.cordova.test2_6720b;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.sql.DataSource;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.progressBar)
    private ProgressBar pb;

    @View(R.id.nopho)
    private ImageView nophoto;

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.lukosov)
    private TextView lukosov;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Profile3 mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public TinderCard(Context context, Profile3 profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;

    }


    public interface FileLoadingListener {

        void onBegin();

        void onSuccess();

        void onFailure(Throwable cause);

        void onEnd();

    }

    @Resolve
    private void onResolved(){
        //Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        //https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg


       // Toast.makeText(mContext, String.valueOf(mProfile.getKey2()), Toast.LENGTH_LONG).show();


        FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();

                lukosov.setText(String.valueOf(count));

                FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(count);

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mProfile.getKey()).child("likes").setValue(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



/*
        if (mProfile.getKey().equals(mProfile.getKey3())) {


            FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("like").setValue(1);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("like").setValue(0);
                    }

                    long count = dataSnapshot.getChildrenCount();

                    FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
*/
        String str = mProfile.getImageUrl();

        final String url_file_name = Environment.getExternalStorageDirectory().toString() + "/Android/data/io.cordova.test2_6720b/cache/" + URLUtil.guessFileName(str, null, null);

        final File file = new File(url_file_name);




        if(file.exists())
        {
            Glide.with(mContext)
                    .load(url_file_name)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb.setVisibility(android.view.View.GONE);
                            nophoto.setVisibility(android.view.View.VISIBLE);
                            //.setImageDrawable(R.drawable.noavatar);
                            Log.d("glide_ex:",e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            pb.setVisibility(android.view.View.GONE);
                            return false;
                        }


                    })
                    .into(profileImageView);

        }else {


            new FileLoadingTask(
                    str,
                    new File(url_file_name),
                    new FileLoadingListener() {
                        @Override
                        public void onBegin() {

                        }

                        @Override
                        public void onSuccess() {

                            Glide.with(mContext)
                                    .load(url_file_name)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            pb.setVisibility(android.view.View.GONE);
                                            nophoto.setVisibility(android.view.View.VISIBLE);
                                            //.setImageDrawable(R.drawable.noavatar);
                                            Log.d("glide_ex:",e.getMessage());
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            pb.setVisibility(android.view.View.GONE);
                                            return false;
                                        }


                                    })
                                    .into(profileImageView);

                            // Log.d("Файл:","Загрузка завершена, путь файла - " + url_file_name);

                            //videoView.setVideoURI(Uri.parse(url_file_name));

                        }

                        @Override
                        public void onFailure(Throwable cause) {

                        }

                        @Override
                        public void onEnd() {

                        }
                    }).execute();

        }
        //Toast.makeText(mContext, "HUY V GOVNE2 (" + mProfile.getKey() + "  |  " + mProfile.getKey3()+")", Toast.LENGTH_LONG).show();
//Log.d("sho za huy",mProfile.getKey());

        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getCountry());
        lukosov.setText(mProfile.getLikes());
    }

    @SwipeOut
    private void onSwipedOut(){

        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }



    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @Click(R.id.profileImageView)
    private void onClick()
    {

     //   Toast.makeText(mContext, String.valueOf(mProfile.getKey2() + " / " + FirebaseAuth.getInstance().getCurrentUser().getUid()), Toast.LENGTH_LONG).show();

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot2) {

                final String s_gender;

                if (snapshot2.child("profile_gender").getValue().equals("m"))
                {
                    s_gender = "поставил";
                }else
                {
                    s_gender = "поставила";
                }

               // Toast.makeText(mContext, String.valueOf(snapshot2.child("profile_gender").getValue() + " / " + snapshot2.child("profile_name").getValue()), Toast.LENGTH_LONG).show();



                FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    // run some code

                    FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    sendlike("param1="+ snapshot2.child("profile_name").getValue() + "&param2=0&param3=" + mProfile.getDevice_token() + "&param4=" + s_gender + "&param5=" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    
                    //FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("like").setValue(0);
                }else
                {
                    FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);
                    //FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("like").setValue(1);

                    sendlike("param1="+ snapshot2.child("profile_name").getValue() + "&param2=1&param3=" + mProfile.getDevice_token() + "&param4=" + s_gender + "&param5=" + FirebaseAuth.getInstance().getCurrentUser().getUid());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError2) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();

                lukosov.setText(String.valueOf(count));

                FirebaseDatabase.getInstance().getReference().child("zn_likes").child("total_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(count);

                FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(mProfile.getKey()).child("likes").setValue(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //FirebaseDatabase.getInstance().getReference().child("zn_likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);

        //FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coords").setValue(coords);

        // Toast.makeText(mContext, mProfile.getKey(), Toast.LENGTH_LONG).show();
    }

    @SwipeIn
    private void onSwipeIn(){

        mSwipeView.doSwipe(false);

        //FirebaseDatabase.getInstance().getReference().child("zn_feeling").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mProfile.getKey()).setValue(1);


/*
        Intent nextScreen = new Intent(mContext, VideoActivityMess.class);

        nextScreen.putExtra("curruser", mProfile.getKey());
        nextScreen.putExtra("currname", mProfile.getName());

        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(nextScreen);

        //finish();

        Log.d("EVENT", "onSwipedIn" + mProfile.getKey());
*/


    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState" + mProfile.getKey() );
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState" + mProfile.getKey());
    }

    public void sendlike(final String params) {

        //Toast.makeText(getApplication(), extra + "/" + extra2, Toast.LENGTH_SHORT).show();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String urlParameters  = params;

                    //String urlParameters  = "param1=a&param2=b&param3=c";

                    byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                    int    postDataLength = postData.length;
                    String request        = "https://rieltorov.net/sendlikes.php";
                    //String request = "https://allwebtech.ru/sendlikes.php";

                    URL    url            = new URL( request );

                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setDoOutput( true );
                    conn.setInstanceFollowRedirects( false );
                    conn.setRequestMethod( "POST" );
                    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty( "charset", "utf-8");
                    conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                    conn.setUseCaches( false );
                    try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                        wr.write( postData );
                    }

                    Log.i("STATUS88", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG88" ,String.valueOf(conn.getResponseMessage()));


                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread2.start();
    }


}
