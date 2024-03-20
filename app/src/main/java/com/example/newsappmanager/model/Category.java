package com.example.newsappmanager.model;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Category {
    private String ID;
    private String CateName;

    public String getID() {
        return ID;
    }

    public String setID(String ID) {
        return this.ID = ID;
    }

    public String getCateName() {
        return CateName;
    }

    public void setCateName(String cateName) {
        CateName = cateName;
    }

    public Category(String ID, String cateName) {
        this.ID = ID;
        CateName = cateName;
    }
    public Category(){}
}
