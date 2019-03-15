package io.cordova.test2_6720b;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MessesAdapter extends RecyclerView.Adapter< MessesAdapter.ViewHolder> {

    private ArrayList<String> names;

    private ArrayList<HashMap> aryList;


    public  MessesAdapter(ArrayList<HashMap> aryList) {
        this.aryList = aryList;
    }

    /*
    public  UsersAdapter(ArrayList<String> names) {
        this.names = names;
    }
*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messes_item, parent, false);




        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(aryList.get(position).get("name").toString());





        //holder.photo.setImageResource(R.drawable.photo);

// подгрузка аватарок

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_users).child(aryList.get(position).get("key").toString()).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (!((Activity) holder.photo.getContext()).isDestroyed()) {

                    Glide
                            .with(holder.photo.getContext())
                            .load(dataSnapshot.child("profile_photo").getValue().toString())
                            .asBitmap()
                            .error(R.drawable.noavatar)
                            .centerCrop()
                            .into(holder.photo);
                }


                // Log.i("StatTab22", String.valueOf(dataSnapshot.child("avatar").getValue()));


                Log.i("StatTab22", String.valueOf(holder.photo));

                if (dataSnapshot.hasChild("curr_activity")) {

                    // Log.i("StatTab23", String.valueOf(isNumeric(dataSnapshot.child("curr_activity").getValue().toString())));

                    if (isNumeric(dataSnapshot.child("curr_activity").getValue().toString()))
                    {
                        holder.currdate.setText("Бал(а) " + getDatatime(dataSnapshot.child("curr_activity").getValue().toString()));
                    }else {
                        holder.currdate.setText("Онлайн");
                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});

// подгрузка аватарок конец



// подгрузка кол-ва новых сообщений


        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        if (child.hasChild(aryList.get(position).get("key").toString())) {
                     //   Log.i("StatTab10", String.valueOf(child.getKey() + " / " + child.getValue() + " / " + child.child(aryList.get(position).get("key").toString()).getValue()));

                            if (!child.child(aryList.get(position).get("key").toString()).child("mess_count").getValue().toString().equals("0")) {

                        //    Log.i("StatTab18", aryList.get(position).get("curruser").toString() + "/" + aryList.get(position).get("key").toString());
                                holder.name.setText(aryList.get(position).get("name").toString() + " (" + child.child(aryList.get(position).get("key").toString()).child("mess_count").getValue().toString() + ")");

                               // holder.mess_count.setText(child.child(aryList.get(position).get("key").toString()).child("mess_count").getValue().toString());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});


        // подгрузка кол-ва новых сообщений конец


        //Log.i("StatTab18", aryList.get(position).get("key").toString() + "/" + aryList.get(position).get("key").toString());

        FirebaseDatabase.getInstance().getReference().child(new Config2().tab_messagesStat).child(aryList.get(position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //Log.i("StatTab22", dataSnapshot.getValue() + "/" + aryList.get(position).get("key").toString());


                    if (dataSnapshot.hasChild("mess_count") && dataSnapshot.hasChild("readed")) {

                        if (!dataSnapshot.child("readed").getValue().toString().equals("f")) {
                            if (Integer.valueOf(dataSnapshot.child("mess_count").getValue().toString()) > 0) {

                                holder.readed.setImageResource(R.drawable.gal2);
                                //holder.readed.setText("н");
                            } else {
                                holder.readed.setImageResource(R.drawable.gal1);
                                //holder.readed.setText("п");
                            }
                        } else {
                            //holder.readed.setText("f");
                            holder.readed.setImageResource(R.drawable.gal3);
                        }
                    }

/*
                if (dataSnapshot.hasChild("currtime")) {
                    holder.currdate.setText(getDatatime(dataSnapshot.child("currtime").getValue().toString()));
                }

                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});


    }

    @Override
    public int getItemCount() {

        return aryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView photo;

        TextView currdate;
        ImageView readed;



        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            photo = (ImageView)itemView.findViewById(R.id.photo);

            currdate = (TextView) itemView.findViewById(R.id.currdate);
            readed = (ImageView) itemView.findViewById(R.id.readed);
        }
    }
    public void filterList(ArrayList<HashMap> mylist2) {
        this.aryList = mylist2;
        notifyDataSetChanged();
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public String getDatatime (String unixtime)
    {
        String restime;
        String today;
        String yestoday;

        String data;

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        java.util.Date currenTimeZone=new java.util.Date((long)Long.parseLong(unixtime)*1000);
        java.util.Date currenTimeZone2=new java.util.Date((long)tsLong*1000);


        yestoday = sdf.format(yesterday());

        today = sdf.format(currenTimeZone2);

        if (today.equals(sdf.format(currenTimeZone)))
        {
            data = "сегодня в";
        }
        else if (yestoday.equals(sdf.format(currenTimeZone)))
        {
            data = "вчера в";
        }
        else
        {
            data = sdf.format(currenTimeZone);
        }

        restime = data + " " + sdf2.format(currenTimeZone);

        return restime;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }





}