package com.sanju784.android_gson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Contact> contacts = new ArrayList<>();
    private Context context;
    private String image_path ="http://10.0.2.2/contactApp/upload/";

    public RecyclerAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.Name.setText(contacts.get(position).getName());
        holder.Email.setText(contacts.get(position).getName());
        String path = image_path + contacts.get(position).getName() + ".jpg";
        Glide.with(context).load(path).into(holder.ProfilePic);

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView Name, Email;
        ImageView ProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.card_name);
            Email = (TextView) itemView.findViewById(R.id.card_email);
            ProfilePic = (ImageView) itemView.findViewById(R.id.card_img);
        }
    }
}
