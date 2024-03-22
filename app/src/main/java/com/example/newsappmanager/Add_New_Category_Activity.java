package com.example.newsappmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsappmanager.adapter.CategoryList_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_New_Category_Activity extends AppCompatActivity {
    FirebaseFirestore db;
    Button btn_submit;
    EditText cate_name;
    Dialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        db = FirebaseFirestore.getInstance();
        btn_submit = findViewById(R.id.admin_category_create);
        cate_name = findViewById(R.id.admin_category_getname);
        bar = new Dialog(Add_New_Category_Activity.this, R.style.dialog);
        bar.setContentView(R.layout.processbar);
        bar.setCanceledOnTouchOutside(false);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cate_name.getText()!=null) {
                        uploadtoFirebase();
                    }
                }
            });

    }
    private void showProgress(){
        bar.show();
    }
    private void endProgress(){
        bar.dismiss();
    }
    private void uploadtoFirebase(){
        showProgress();
        Map<String, Object> category = new HashMap<>();
        category.put("cateName", cate_name.getText().toString());
        db.collection("category")
                .add(category)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        db.collection("category").document(id)
                                .update("cateID", id)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        endProgress();
                                        Toast.makeText(Add_New_Category_Activity.this, "CREATE CATEGORY SUCCESS", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(Add_New_Category_Activity.this, List_Category_Activity.class);
                                        finish();
                                        startActivity(i);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Add_New_Category_Activity.this, "CREATE CATEGORY FAILED", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }
}