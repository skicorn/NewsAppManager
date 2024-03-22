package com.example.newsappmanager.adapter;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsappmanager.List_News_Activity;
import com.example.newsappmanager.R;
import com.example.newsappmanager.Show_News_Info_Activity;
import com.example.newsappmanager.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewContext_Adapter extends RecyclerView.Adapter<NewContext_Adapter.ViewHolder> {
    private ArrayList<News> arr_News;
    private ArrayList<News> getArr_Newsold;

    public NewContext_Adapter(ArrayList<News> arr_News) {
        this.arr_News = arr_News;
        this.getArr_Newsold = new ArrayList<>(arr_News);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = arr_News.get(position);
        holder.title.setText(news.getTitle());
        holder.view.setText(news.getView() + " view");
        holder.time.setText(news.getTime());
        Picasso.get().load(news.getImage()).into(holder.image);;
        //add click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(v.getContext(), Show_News_Info_Activity.class);
                i1.putExtra("newsinfo", news);
                v.getContext().startActivity(i1);
                Log.d(TAG, "onClick: " + v.getContext().getClass().getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr_News.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, view, time;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.item_news_image);
            title = itemView.findViewById(R.id.item_main_title);
            view = itemView.findViewById(R.id.item_main_poster);
            time = itemView.findViewById(R.id.item_main_time);
        }
    }
}