package com.example.newsappmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.newsappmanager.model.User;

public class Show_User_Info_Activity extends AppCompatActivity {
    TextView user_UID;
    EditText userEmail, userPassword;
    Button updateUser, deleteUser;
    ImageButton back;
    //Data
    User user;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_info);
        //find view
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        updateUser = findViewById(R.id.user_update);
        deleteUser = findViewById(R.id.user_delete);
        back = findViewById(R.id.user_back);
        intent = getIntent();
        if (intent.hasExtra("userdata")){
            user = (User) intent.getSerializableExtra("userdata");
            userEmail.setText(user.getEmail());
            userPassword.setText(user.getPassword());
        }
    }
}