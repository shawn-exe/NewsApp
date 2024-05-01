package com.example.newsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView text_title, text_source,content,time;
    ImageView img_headline;
    CardView cardview;
    ImageView heart;
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        text_title = itemView.findViewById(R.id.text_title);
        content= itemView.findViewById(R.id.content);
        time=itemView.findViewById(R.id.time);
        text_source = itemView.findViewById(R.id.text_source);
        heart = itemView.findViewById(R.id.imageView2);
        img_headline=itemView.findViewById(R.id.img_headline);

        cardview=itemView.findViewById(R.id.main_container);
    }
}
