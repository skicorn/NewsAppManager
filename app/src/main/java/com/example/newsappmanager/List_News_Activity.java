package com.example.newsappmanager;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsappmanager.adapter.NewContext_Adapter;
import com.example.newsappmanager.model.News;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class List_News_Activity extends AppCompatActivity {
    private ArrayList<News> news;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private CollectionReference refer;
    Dialog bar;
//    Button btn_add,btn_back;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        //firebase and classes
        news = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        refer = db.collection("category");
        //recycler view
        recyclerView = findViewById(R.id.admin_list_news);
        //toolbar
        toolbar = findViewById(R.id.listnew_topmenu);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quản lý tin tức");
        //dialog
        bar = new Dialog(List_News_Activity.this, R.style.dialog);
        bar.setContentView(R.layout.processbar);
        bar.setCanceledOnTouchOutside(false);
        getDataformFireStore(news);
    }
    private void showProgress(){
        bar.show();
    }
    private void endProgress(){
        bar.dismiss();
    }
    void getDataformFireStore(ArrayList<News> news1) {
        showProgress();
        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Lấy dữ liệu của tài liệu
                                Map<String, Object> data = document.getData();
                                //Tạo obj news
                                News objnews = new News();
                                String uri = document.get("news_imgURI").toString();
                                Log.d(TAG, "onComplete: the document's image: " + uri);
                                String URL = "https://firebasestorage.googleapis.com/v0/b/newsdb-e0729.appspot.com/o/image%2F" + uri +  "?alt=media";
                                objnews.setId(document.getId());
                                objnews.setImageID(uri);
                                objnews.setImage(URL);
                                objnews.setTitle(data.get("news_Title").toString());
                                objnews.setContext(data.get("news_Context").toString());
                                objnews.setUser(data.get("news_ID").toString());
                                objnews.setTime(data.get("news_time").toString());
                                objnews.setView(data.get("news_View").toString());
                                objnews.setCategory(getCate(data.get("news_cate").toString(), objnews));
                                news1.add(objnews);
                            }
                            endProgress();
                            NewContext_Adapter adapter = new NewContext_Adapter(news);
                            recyclerView.setLayoutManager(new LinearLayoutManager(List_News_Activity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(List_News_Activity.this, LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(adapter);
                        } else {
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
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        if (item.getItemId()==R.id.menu_add){
            Intent i = new Intent(List_News_Activity.this, Add_News_Activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCate(String id, News news){
        db.collection("category").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        news.setCategory(documentSnapshot.getString("cateName"));
                    }
                });
        return news.getCategory();
    }
}