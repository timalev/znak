package io.cordova.test2_6720b;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.io.File;

import javax.sql.DataSource;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.progressBar)
    private ProgressBar pb;

    @View(R.id.nopho)
    private ImageView nophoto;

    @View(R.id.profileImageView)
    private ImageView profileImageView;

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



















/*
        Glide
                .with(mContext)
                .load(mProfile.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noavatar)
                .into(profileImageView);


                */

        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getCountry());
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

    @SwipeIn
    private void onSwipeIn(){


        Intent nextScreen = new Intent(mContext, VideoActivityMess.class);

        nextScreen.putExtra("curruser", mProfile.getKey());
        nextScreen.putExtra("currname", mProfile.getName());

        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(nextScreen);

        //finish();

        Log.d("EVENT", "onSwipedIn" + mProfile.getKey());



    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
