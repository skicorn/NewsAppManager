package com.example.newsappmanager;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.newsappmanager.adapter.CategoryList_Adapter;
import com.example.newsappmanager.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class List_Category_Activity extends AppCompatActivity {
    //Firebase
    FirebaseFirestore db;
    //Android
    RecyclerView recyclerView;
    EditText categoryname;
    //object
    ArrayList<Category> categories;
    Button btn_add;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        //firebase
        db = FirebaseFirestore.getInstance();
        //get db
        categories = new ArrayList<>();
        recyclerView = findViewById(R.id.admin_list_category);
        btn_add = findViewById(R.id.admin_addcategory);
        getCatefromFirestore(categories);
        //clear screen
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(v.getWindowToken(),0);
                    v.clearFocus();
                }
                return false;
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(List_Category_Activity.this, Add_New_Category_Activity.class);
                startActivity(i);
            }
        });
    }
    private void getCatefromFirestore(ArrayList<Category> categories1){
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Category cate = new Category();
                                cate.setID(data.get("cateID").toString());
                                cate.setCateName(data.get("cateName").toString());
                                categories1.add(cate);
                            }
                            CategoryList_Adapter adapter = new CategoryList_Adapter(categories);
                            recyclerView.setLayoutManager(new LinearLayoutManager(List_Category_Activity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(List_Category_Activity.this, LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(adapter);
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}