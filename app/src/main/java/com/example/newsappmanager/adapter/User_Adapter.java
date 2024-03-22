package com.example.newsappmanager.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsappmanager.R;
import com.example.newsappmanager.Show_User_Info_Activity;
import com.example.newsappmanager.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder>{
    private ArrayList<User> userArrayList;

    public User_Adapter(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }
    @NonNull
    @Override
    public User_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Adapter.ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.Email.setText(user.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Show_User_Info_Activity.class);
                i.putExtra("userdata", user);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.admin_userManager_email);
        }
    }
}
