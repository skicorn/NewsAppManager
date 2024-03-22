package com.example.newsappmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsappmanager.List_Category_Activity;
import com.example.newsappmanager.R;
import com.example.newsappmanager.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CategoryList_Adapter extends RecyclerView.Adapter<CategoryList_Adapter.ViewHolder> {
    FirebaseFirestore db;
    ArrayList<Category> categories;
    String newName;
    AlertDialog.Builder alertDialog;

    public CategoryList_Adapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryList_Adapter.ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        Category category = categories.get(position);
        holder.catename.setText(category.getCateName());
        holder.catename.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    holder.btn_save.setVisibility(View.VISIBLE);
                    holder.btn_save.setEnabled(true);
                }
                else {
                    holder.btn_save.setVisibility(View.INVISIBLE);
                    holder.btn_save.setEnabled(false);
                }
            }
        });
        if (holder.catename.hasFocus()){
            holder.btn_save.setVisibility(View.VISIBLE);
            holder.btn_save.setEnabled(true);
        } else {
            holder.btn_save.setVisibility(View.INVISIBLE);
            holder.btn_save.setEnabled(false);
        }
        holder.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName = holder.catename.getText().toString();
                category.setCateName(newName);
                updateCate(category, v);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDelete(v, category);
            }
        });
    }
    private void showDelete(View view, Category category){
        alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Are you sure to delete?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletefromFirestore(category, view);
                dialog.dismiss();
                refresh( (Activity) view.getContext());
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void refresh(Activity activity){
        activity.recreate();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    private void updateCate(Category category, View v){
        db.collection("category").document(category.getID())
                .update("cateName", category.getCateName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "UPDATE COMPLETE", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "UPDATE FAILED", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void deletefromFirestore(Category category, View v){
        db.collection("category").document(category.getID())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(v.getContext(), "DELETE SUCCESS", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "DELETE FAILED", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton btn_save,btn_delete;
        EditText catename;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catename = itemView.findViewById(R.id.admin_category_name);
            btn_save = itemView.findViewById(R.id.admin_category_save);
            btn_delete = itemView.findViewById(R.id.admin_category_delete);
        }
    }
}
