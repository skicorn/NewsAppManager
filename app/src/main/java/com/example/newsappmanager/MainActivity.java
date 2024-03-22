    package com.example.newsappmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

    public class MainActivity extends AppCompatActivity {
    Button btn_news_manager, btn_category_manager, btn_user_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_news_manager = findViewById(R.id.admin_fragment_newsManager);
        btn_category_manager = findViewById(R.id.admin_fragment_cateManager);
        btn_user_manager = findViewById(R.id.admin_fragment_usersManager);
        btn_news_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, List_News_Activity.class);
                startActivity(i);
            }
        });
        btn_category_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, List_Category_Activity.class);
                startActivity(i);
            }
        });
        btn_user_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, List_User_Activity.class);
                startActivity(i);
            }
        });
    }
}