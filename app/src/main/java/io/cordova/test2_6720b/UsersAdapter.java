package io.cordova.test2_6720b;

import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class UsersAdapter extends RecyclerView.Adapter< UsersAdapter.ViewHolder> {

    private ArrayList<String> names;

    private ArrayList<HashMap> aryList;


    public  UsersAdapter(ArrayList<HashMap> aryList) {
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
                .inflate(R.layout.users_item, parent, false);




        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(aryList.get(position).get("name").toString());

    }

    @Override
    public int getItemCount() {
        return aryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;



        ViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
    public void filterList(ArrayList<HashMap> mylist2) {
        this.aryList = mylist2;
        notifyDataSetChanged();
    }



}



    /*

    private List<UsersModel> profile;

    public UsersAdapter(List<UsersModel> profile) {
        this.profile = profile;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, int position) {

        UsersModel user = profile.get(position);

        holder.name.setText(user.name);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(),0,0,"Удалить");
            }
        });

    }

    @Override
    public int getItemCount() {
        return profile.size();
    }


    class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public UsersViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public void filterList(ArrayList<String> names) {
        this.profile = names;
        notifyDataSetChanged();
    }
*/


