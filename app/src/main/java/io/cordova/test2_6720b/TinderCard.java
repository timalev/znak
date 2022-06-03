package io.cordova.test2_6720b;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;

//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.progressBar)
    ProgressBar pb;

    @View(R.id.nopho)
    ImageView nophoto;

    @View(R.id.s_like)
    ImageView s_like;


    @View(R.id.towrite)
    TextView towrite;

    @View(R.id.profileImageView)
    ImageView profileImageView;

    @View(R.id.lukosov)
    TextView lukosov;

    @View(R.id.nameAgeTxt)
    TextView nameAgeTxt;


    @View(R.id.locationNameTxt)
    TextView locationNameTxt;

    private Profile3 mProfile;
    private Context mContext;
    private Point mCardViewHolderSize;
    private Callback mCallback;
    //SwipePlaceHolderView mSwipeView;

    @SwipeView
    android.view.View mSwipeView;

    public TinderCard(Context context, Profile3 profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;


        //  mSwipeView = swipeView;

    }


    public interface FileLoadingListener {

        void onBegin();

        void onSuccess();

        void onFailure(Throwable cause);

        void onEnd();

    }

    @Resolve
    public void onResolved(){
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


        String str = mProfile.getImageUrl();
        final String url_file_name;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //File file = new File (this.getExternalFilesDir(null) + "/Znak/files");
            url_file_name = mContext.getExternalCacheDir() + "/"  + URLUtil.guessFileName(str, null, null);
        }else {

            url_file_name = Environment.getExternalStorageDirectory().toString() + "/Znak/cache/" + URLUtil.guessFileName(str, null, null);
            // final String url_file_name = Environment.getExternalStorageDirectory().toString() + "/Pictures/1627784511724.jpg";
        }

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
                            Log.d("glide_ex:",e.getMessage() + ",," + url_file_name + e.getCauses());



                          //  deleteFiles(Environment.getExternalStorageDirectory() + "/Znak/cache");



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
                                            Log.d("glide_ex: " ,e.getMessage() + ", " + url_file_name);
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

    @SwipeOutDirectional
    public void onSwipeOutDirectional(SwipeDirection direction) {

        Log.d("DEBUG", "SwipeOutDirectional " + direction.name());
        if (direction.getDirection() == SwipeDirection.TOP.getDirection()) {
            mCallback.onSwipeUp();
        }
    }

    @SwipeCancelState
    public void onSwipeCancelState() {

        Log.d("DEBUG", "onSwipeCancelState");
        mSwipeView.setAlpha(1);
    }

    @SwipeInDirectional
    public void onSwipeInDirectional(SwipeDirection direction) {

        Toast.makeText(mContext, mProfile.getKey2() + " /22 " + mProfile.getKey3(), Toast.LENGTH_SHORT).show();
    }

    @SwipingDirection
    public void onSwipingDirection(SwipeDirection direction) {

        Log.d("DEBUG", "SwipingDirection " + direction.name());
    }

    @SwipeTouch
    public void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {

        float cardHolderDiagonalLength =
                (float) Math.sqrt(Math.pow(mCardViewHolderSize.x, 2) + (Math.pow(mCardViewHolderSize.y, 2)));
        float distance = (float) Math.sqrt(Math.pow(xCurrent - xStart, 2) + (Math.pow(yCurrent - yStart, 2)));

        float alpha = 1 - distance / cardHolderDiagonalLength;

        Log.d("DEBUG", "onSwipeTouch "
                + " xStart : " + xStart
                + " yStart : " + yStart
                + " xCurrent : " + xCurrent
                + " yCurrent : " + yCurrent
                + " distance : " + distance
                + " TotalLength : " + cardHolderDiagonalLength
                + " alpha : " + alpha
        );

        ((FrameLayout)mSwipeView).setAlpha(alpha);
    }

    interface Callback {
        void onSwipeUp();
    }
/*
    @SwipeOut
    private void onSwipedOut(){

        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }


    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }
*/
    @Click(R.id.s_like)
    public void onLike()
    {
        //if (mProfile.)
        // mSwipeView.isShown()

       // Toast.makeText(mContext, String.valueOf(mSwipeView.isShown()), Toast.LENGTH_LONG).show();


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
                            FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot3) {
                                    if (snapshot3.hasChild("profile_name")) {

                                        FirebaseDatabase.getInstance().getReference().child("zn_likes").child(mProfile.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(snapshot3.child("profile_name").getValue());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError3)
                                {
                                }
                            });

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



    }

    @Click(R.id.towrite)
    public void toWrite(){

        Intent nextScreen = new Intent(mContext, VideoActivityMess.class);

        nextScreen.putExtra("curruser", mProfile.getKey());
        nextScreen.putExtra("currname", mProfile.getName());

        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(nextScreen);
    }

    @SwipeOut
    public  void SwipeOut()
    {
       // Toast.makeText(mContext, mProfile.getKey2() + " / " + mProfile.getKey3(), Toast.LENGTH_SHORT).show();

        if (mProfile.getKey3() == mProfile.getKey2()) {

            Toast.makeText(mContext, new Languages().NoMoreUsers(), Toast.LENGTH_SHORT).show();
           //mSwipeView.
        }
    }

        @SwipeIn
    public void onSwipeIn() {

           // mSwipeView.do

          //  Toast.makeText(mContext, mProfile.getKey2() + " / " + mProfile.getKey3(), Toast.LENGTH_SHORT).show();
            if (mProfile.getKey3() == mProfile.getKey2()) {

                Toast.makeText(mContext, new Languages().NoMoreUsers(), Toast.LENGTH_SHORT).show();

                //mSwipeView.removeAllViews();
                //  mSwipeView.addView(this);

                // mSwipeView.doSwipe(true);
            }
        }


/*
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState" + mProfile.getKey() );
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState" + mProfile.getKey());
    }
*/
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

    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {

                Log.d("del_error",e.getMessage());
            }
        }
    }


}
