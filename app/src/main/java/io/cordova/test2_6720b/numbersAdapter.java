package io.cordova.test2_6720b;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class numbersAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>
{

    Context context;
    ArrayList<Profile2> profiles;

    private static int viewHolderCount;
    private int numberItems;

    public numbersAdapter(Context c, ArrayList<Profile2> p)
    {
        context = c;
        profiles = p;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        switch (viewType) {

            case 0:

                int layoutIdForListItem;

                layoutIdForListItem = R.layout.number_list_item;

                LayoutInflater inflater = LayoutInflater.from(context);

                View view = inflater.inflate(layoutIdForListItem, parent, false);

                NumberViewHolder NumberViewHolder = new NumberViewHolder(view);

                return NumberViewHolder;

            case 1:

                int layoutIdForListItem1;

                layoutIdForListItem1 = R.layout.number_list_item_right;

                LayoutInflater inflater1 = LayoutInflater.from(context);

                View view1 = inflater1.inflate(layoutIdForListItem1, parent, false);

                NumberViewHolder1 NumberViewHolder1 = new NumberViewHolder1(view1);

                return NumberViewHolder1;


           case 3:

                int layoutIdForListItem3;

                layoutIdForListItem3 = R.layout.number_list_item_pic_right;

                //int layoutIdForListItem = R.layout.number_list_item;

                LayoutInflater inflater3 = LayoutInflater.from(context);

                View view3 = inflater3.inflate(layoutIdForListItem3, parent, false);

                NumberViewHolder3 numberViewHolder3 = new NumberViewHolder3(view3);

                return numberViewHolder3;

                default:

                int layoutIdForListItem2;

                layoutIdForListItem2 = R.layout.number_list_item_pic;

                //int layoutIdForListItem = R.layout.number_list_item;

                LayoutInflater inflater2 = LayoutInflater.from(context);

                View view2 = inflater2.inflate(layoutIdForListItem2, parent, false);

                NumberViewHolder2 numberViewHolder2 = new NumberViewHolder2(view2);

                return numberViewHolder2;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()) {
            case 0:

                NumberViewHolder numberViewHolder = (NumberViewHolder)holder;

                //numberViewHolder.name.setText(profiles.get(position).getName());
                numberViewHolder.mess.setText(profiles.get(position).getMess());

                break;

            case 1:

                NumberViewHolder1 numberViewHolder1 = (NumberViewHolder1)holder;

                //numberViewHolder.name.setText(profiles.get(position).getName());
                numberViewHolder1.mess.setText(profiles.get(position).getMess());

                break;

            case 3:
                NumberViewHolder3 numberViewHolder3 = (NumberViewHolder3)holder;

                // numberViewHolder2.name.setText(profiles.get(position).getName());

                numberViewHolder3.imageView.setRotation(90);

                Glide
                        .with(context)
                        .load(profiles.get(position).getMess())
                        .into(numberViewHolder3.imageView);
                break;

            default:
                NumberViewHolder2 numberViewHolder2 = (NumberViewHolder2)holder;

               // numberViewHolder2.name.setText(profiles.get(position).getName());

                numberViewHolder2.imageView.setRotation(90);

                Glide
                        .with(context)
                        .load(profiles.get(position).getMess())
                        .into(numberViewHolder2.imageView);



                //profiles.get(position).getType();
        }

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }





    class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView mess;

        public NumberViewHolder (View itemView) {

            super(itemView);

            //name = (TextView) itemView.findViewById(R.id.name);
            mess = (TextView) itemView.findViewById(R.id.mess);
        }

    }
    class NumberViewHolder1 extends RecyclerView.ViewHolder {

        TextView name;
        TextView mess;

        public NumberViewHolder1 (View itemView) {

            super(itemView);

            //name = (TextView) itemView.findViewById(R.id.name);
            mess = (TextView) itemView.findViewById(R.id.mess);
        }

    }
    class NumberViewHolder2 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public NumberViewHolder2(View itemView) {

            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.pic);
            //name = (TextView) itemView.findViewById(R.id.name);

        }
    }
    class NumberViewHolder3 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public NumberViewHolder3(View itemView) {

            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.pic);
            //name = (TextView) itemView.findViewById(R.id.name);

        }
    }

    @Override
    public int getItemViewType(final int position) {


        if (profiles.get(position).getType()!=null && profiles.get(position).getType().equals("pic")) {

            if (FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(profiles.get(position).getUser()))
            {
                return 3;
            }
            else {
                return 2;
            }
        }
        else {

            if (FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(profiles.get(position).getUser()))
            {
                return 1;
            }
            else {
                return 0;
            }
        }
        //*/


        //return profiles.get(position).getMess();
    }
}