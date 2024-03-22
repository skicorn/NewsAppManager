package com.example.newsappmanager;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsappmanager.R;
import com.example.newsappmanager.adapter.Category_Adapter;
import com.example.newsappmanager.model.Category;
import com.example.newsappmanager.model.News;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class Add_News_Activity extends AppCompatActivity {
    private static final String TAG = "Add_News_Activity";
    ArrayList<Category> categories;
    FirebaseFirestore db;
    StorageReference storageReference;
    TextView title, context, IMGname;
    ImageView showIMGpreview;
    ImageButton btn_back;
    Button chooseIMG, submit;
    Spinner spinner;
    Category_Adapter adapter;
    String obj_cate;
    String filename;
    Uri uri;
    Dialog bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        db = FirebaseFirestore.getInstance();;
        title = findViewById(R.id.add_news_title);
        context = findViewById(R.id.add_news_addContext);
        chooseIMG = findViewById(R.id.add_news_image);
        IMGname = findViewById(R.id.add_news_showIMGname);
        submit = findViewById(R.id.add_news_submit);
        showIMGpreview = findViewById(R.id.add_news_showIMG);
        btn_back = findViewById(R.id.add_news_imgBack);
        categories = new ArrayList<>();
        spinner = findViewById(R.id.add_news_category);
        bar = new Dialog(Add_News_Activity.this, R.style.dialog);
        bar.setContentView(R.layout.processbar);
        bar.setCanceledOnTouchOutside(false);
        getCategory();
        //get category to view
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obj_cate = categories.get(position).getID();
                Log.d(TAG, "onItemSelected: " + obj_cate.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Choose IMAGE from gallery
        chooseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIMG();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditText()==true) {
                    upLoadFiletoDB();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void showProgress(){
        bar.show();
    }
    private void endProgress(){
        bar.dismiss();
    }
    boolean checkEditText() {
        boolean check = true;
        if (title.getText().toString().isEmpty()) {
            title.setError("MUST ENTER YOUR NEWS TITLE");
            check = false;
        }
        if (context.getText().toString().isEmpty()) {
            context.setError("MUST ENTER YOUR NEWS CONTEXT");
            check = false;
        }
        if (filename == null) {
            Toast.makeText(Add_News_Activity.this, "PLEASE UPLOAD IMAGE", Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }
    private void selectIMG() {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 100);
    }
    private void uploadIMG(String filename){
        storageReference = FirebaseStorage.getInstance().getReference("image/" + filename);
        storageReference.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_News_Activity.this, "add failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data!=null && data.getData()!=null){
            uri = data.getData();
            filename = uri.getLastPathSegment()+ ".jpg";
            Log.d(TAG, "onActivityResult: " + filename);
            showIMGpreview.setImageURI(uri);
            showIMGpreview.setVisibility(View.VISIBLE);
            IMGname.setText(filename);
        }
    }

    private void upLoadFiletoDB(){
        showProgress();
        LocalDate current = LocalDate.now();
        Map<String, Object> news = new HashMap<>();
        news.put("news_Title", title.getText().toString());
        news.put("news_Context", context.getText().toString());
        news.put("news_cate", obj_cate);
        news.put("news_time", current.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        news.put("news_imgURI", filename);
        news.put("news_View", "0");
        db.collection("news").add(news)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentId = documentReference.getId();
                        db.collection("news").document(documentId)
                                .update("news_ID", documentId)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {
                                        uploadIMG(filename);
                                        endProgress();
                                        Toast.makeText(Add_News_Activity.this, "UPLOAD COMPLETE", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(Add_News_Activity.this, List_News_Activity.class);
                                        finish();
                                        startActivity(i);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Add_News_Activity.this, "UPDATE FAILED", Toast.LENGTH_LONG).show();
                                        endProgress();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_News_Activity.this, "add failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getCategory(){
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Category cate = new Category(document.get("cateID").toString(),document.get("cateName").toString());
                                categories.add(cate);
                            }
                            adapter = new Category_Adapter(Add_News_Activity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
                            spinner.setAdapter(adapter);
                        }
                    }
                });
    }
}