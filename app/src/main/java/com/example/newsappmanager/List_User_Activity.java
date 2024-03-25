package com.example.newsappmanager;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsappmanager.adapter.User_Adapter;
import com.example.newsappmanager.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List_User_Activity extends AppCompatActivity {
    //db
    DatabaseReference reference;
    //view
    RecyclerView view;
    User_Adapter adapter;
    ArrayList<User> users;
    Dialog bar;
    Toolbar toolbar;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        //find view
        users = new ArrayList<>();
        view = findViewById(R.id.user_list);
        toolbar = findViewById(R.id.user_bar);
        setSupportActionBar(toolbar);
        //create db
        reference = FirebaseDatabase.getInstance().getReference().child("User");
        bar = new Dialog(List_User_Activity.this, R.style.dialog);
        bar.setContentView(R.layout.processbar);
        bar.setCanceledOnTouchOutside(false);
        getUserList();
    }
    private void showProgress(){
        bar.show();
    }
    private void endProgress(){
        bar.dismiss();
    }
    public void getUserList(){
        showProgress();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = new User();
                    user.setEmail(dataSnapshot.child("Email").getValue(String.class));
                    user.setPassword(dataSnapshot.child("PassWord").getValue(String.class));
                    users.add(user);
                }
                endProgress();
                adapter = new User_Adapter(users);
                view.setLayoutManager(new LinearLayoutManager(List_User_Activity.this));
                view.addItemDecoration(new DividerItemDecoration(List_User_Activity.this, LinearLayoutManager.VERTICAL));
                view.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent i = new Intent(List_User_Activity.this, MainActivity.class);
            startActivity(i);
        }
        if (item.getItemId()==R.id.menu_add){
            Intent i = new Intent(List_User_Activity.this, Add_User_Activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}