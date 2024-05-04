package com.example.newsapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<NewsHeadlines> headlines;

    private SelectListener listener;
    SQLiteDatabase db;
    public CustomAdapter(Context context, List<NewsHeadlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_list_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.text_title.setText(headlines.get(position).getTitle());
        holder.text_source.setText(headlines.get(position).getSource().getName());
        holder.content.setText(headlines.get(position).getDescription());
        holder.time.setText(headlines.get(position).getPublishedAt());

        if (isFavorite(headlines.get(position).getTitle())) {
            holder.heart.setImageResource(R.drawable.save);
        } else {
            holder.heart.setImageResource(R.drawable.heart);
        }
        if(headlines.get(position).getUrlToImage()!=null)
        {
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.img_headline);
        }
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {

                try{
                    db = context.openOrCreateDatabase("favourites",MODE_PRIVATE,null);
                    if(!isFavorite(headlines.get(position).getTitle())) {


                        db.execSQL("INSERT into fav values('" + headlines.get(position).getTitle() + "','"+headlines.get(position).getSource().getName()+"','"+headlines.get(position).getPublishedAt()+"','"+headlines.get(position).getUrl()+"','"+headlines.get(position).getUrlToImage()+"','"+headlines.get(position).getContent()+"','"+headlines.get(position).getAuthor()+"')");


                        holder.heart.setImageResource(R.drawable.save);
                        Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("DELETE from fav where title = ?",new String[]{headlines.get(position).getTitle()});
                        holder.heart.setImageResource(R.drawable.heart);
                        Toast.makeText(context,"Removed from favourites",Toast.LENGTH_SHORT).show();

                    }
                    db.close();
                }catch (SQLException e){
                    throw new SQLException(e.getMessage());
                }
            }

        });

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnNewsClicked(headlines.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
    private boolean isFavorite(String news) {
        SQLiteDatabase db = context.openOrCreateDatabase("favourites", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS fav(title varchar(100) primary key,source varchar(100),publishedAt timestamp,url varchar(500),urlToImage varchar(500),content varchar(50),author varchar(20))");
        Cursor cursor = db.rawQuery("SELECT * FROM fav WHERE title = ?", new String[]{news});
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isFavorite;
    }
}
