package com.example.newsappmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsappmanager.R;
import com.example.newsappmanager.model.Category;

import java.util.ArrayList;

public class Category_Adapter extends ArrayAdapter<Category> {
    private ArrayList<Category> categories;
    private LayoutInflater inflater;


    public Category_Adapter(@NonNull Context context, int resource, ArrayList<Category> categories) {
        super(context, resource, categories);
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }
    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    //get view for item
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_category, parent, false);
        }
        TextView item = convertView.findViewById(R.id.items_category_name);
        Category category = getItem(position);
        if(category!=null){
            item.setText(category.getCateName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_category,parent, false );
        }
        TextView drpitem = convertView.findViewById(R.id.items_category_name);
        Category category = getItem(position);
        if(category!=null){
            drpitem.setText(category.getCateName());
        }
        return convertView;
    }

}
