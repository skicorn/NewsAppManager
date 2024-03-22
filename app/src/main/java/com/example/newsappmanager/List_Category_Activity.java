package com.example.newsappmanager;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    //object
    ArrayList<Category> categories;
    Category category;
    Dialog bar;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        //firebase
        db = FirebaseFirestore.getInstance();
        //get db
        categories = new ArrayList<>();
        recyclerView = findViewById(R.id.admin_list_category);
        bar = new Dialog(List_Category_Activity.this, R.style.dialog);
        bar.setContentView(R.layout.processbar);
        bar.setCanceledOnTouchOutside(false);
        getCatefromFirestore(categories);
        //toolbar
        toolbar = findViewById(R.id.listcategory_topmenu);
        setSupportActionBar(toolbar);
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
    }
    private void showProgress(){
        bar.show();
    }
    private void endProgress(){
        bar.dismiss();
    }
    private void getCatefromFirestore(ArrayList<Category> categories1){
        showProgress();
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
                            endProgress();
                            CategoryList_Adapter adapter = new CategoryList_Adapter(categories);
                            recyclerView.setLayoutManager(new LinearLayoutManager(List_Category_Activity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(List_Category_Activity.this, LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(adapter);
                        }else {
                            endProgress();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        if (item.getItemId()==R.id.menu_add){
            Intent i = new Intent(List_Category_Activity.this, Add_New_Category_Activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}