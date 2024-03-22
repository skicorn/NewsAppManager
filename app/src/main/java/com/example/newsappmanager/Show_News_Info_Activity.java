package com.example.newsappmanager;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsappmanager.List_News_Activity;
import com.example.newsappmanager.adapter.Category_Adapter;
import com.example.newsappmanager.model.Category;
import com.example.newsappmanager.model.News;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Show_News_Info_Activity extends AppCompatActivity {
    //Firebase object
    FirebaseFirestore db;
    StorageReference storageReference;
    //View object
    ArrayList<Category> categories;
    Category_Adapter adapter;
    EditText title, context;
    TextView imgName;
    ImageView img;
    ImageButton btn_back;
    Spinner spinner;
    Button btn_update, btn_delete, btn_uploadIMG;
    Intent intent;
    Uri uri;
    //other object
    String filename1;
    int size;
    News news;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_info);
        //get db
        db = FirebaseFirestore.getInstance();
        alertDialog = new AlertDialog.Builder(Show_News_Info_Activity.this);
        categories = new ArrayList<Category>();
        //find view
        title = findViewById(R.id.admin_show_new_title);
        context = findViewById(R.id.admin_show_new_context);
        imgName = findViewById(R.id.admin_show_update_imgname);
        img = findViewById(R.id.admin_show_update_img);
        spinner = findViewById(R.id.admin_show_new_category);
        btn_delete = findViewById(R.id.admin_show_button_delete);
        btn_update = findViewById(R.id.admin_show_button_update);
        btn_back = findViewById(R.id.add_news_imgBack);
        btn_uploadIMG = findViewById(R.id.admin_show_update_img_button);
        getCategory();
        //get intetnt
        intent = getIntent();
        if (intent.hasExtra("newsinfo")) {
            news = (News) intent.getSerializableExtra("newsinfo");
            title.setText(news.getTitle().toString());
            context.setText(news.getContext().toString());
            Picasso.get().load(news.getImage()).into(img);
            img.setVisibility(View.VISIBLE);
            filename1 = news.getImageID();
        }
        imgName.setText(news.getImageID());
        btn_uploadIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                news.setCategory(categories.get(position).getID());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //UPDATE DOCUMENT
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNews(news);
                updateIMG(news);
                updateDocument(news.getId());
            }
        });
        //DELETE DOCUMENT
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(news.getImageID(), news.getId());
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //create a News object to update
    private void updateNews(News news){
        news.setTitle(title.getText().toString());
        news.setContext(context.getText().toString());
        Log.d(TAG, "news: " + news.getImageID()+"\n" +  news.getCategory()+"\n" + news.getImage() +"\n" + news.getContext());
    }
    //update img
    private void updateIMG(News news){
        if (uri != null && !uri.equals(Uri.parse(news.getImage()))) {
            storageReference = FirebaseStorage.getInstance().getReference("image/" + filename1);
            storageReference.putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Show_News_Info_Activity.this, "UPDATE FAILED: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    //update document
    private void updateDocument(String documentID){
        db.collection("news").document(documentID)
                .update(
                        "news_Title",news.getTitle(),
                        "news_cate", news.getCategory(),
                        "news_Context", news.getContext(),
                        "news_imgURI", filename1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Show_News_Info_Activity.this, "UPDATE COMPLETED", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Show_News_Info_Activity.this, "UPDATE FAILED", Toast.LENGTH_LONG).show();
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
                            adapter = new Category_Adapter(Show_News_Info_Activity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
                            spinner.setAdapter(adapter);
                            size =  categories.size();
                            // set spinner value for item when display
                            if(intent.hasExtra("newsinfo")){
                                News news = (News) intent.getSerializableExtra("newsinfo");
                                String newsCategory = news.getCategory();
                                for(int i = 0; i < size; i++) {
                                    if (categories.get(i).getCateName().equals(newsCategory)) {
                                        spinner.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && data!=null && data.getData()!=null){
            uri = data.getData();
            filename1 = uri.getLastPathSegment() + ".jpg";
            img.setImageURI(uri);
            imgName.setText(filename1);
        }
    }
    //DELETE A DOCUMENT
    private void deleteDocument(String documentID){
        db.collection("news").document(documentID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Show_News_Info_Activity.this, "DELETE COMPLETE", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Show_News_Info_Activity.this, List_News_Activity.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Show_News_Info_Activity.this, "DELETE FAILED",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void deleteIMG(String imgID){
        storageReference = FirebaseStorage.getInstance().getReference("image/" + imgID);
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Show_News_Info_Activity.this, "DELETE COMPLETE", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Show_News_Info_Activity.this, "DELETE FAILED", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void showDialog(String imgID, String ID){
        alertDialog.setTitle("ARE YOU SỦE TO DELETE?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteIMG(imgID);
                deleteDocument(ID);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Show_News_Info_Activity.this, "DELETE CANCEL", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}