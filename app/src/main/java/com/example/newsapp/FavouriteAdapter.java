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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;

    SelectListener2 listener;
    private ArrayList<ArrayList<String>> list;
    SQLiteDatabase db;
    public FavouriteAdapter(Context context, ArrayList<ArrayList<String>> list, SelectListener2 listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.favourite_list_items,parent,false));
   }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.text_source.setText(list.get(position).get(0));
        holder.text_title.setText(list.get(position).get(2));
        holder.content.setText(list.get(position).get(6));
        holder.time.setText(list.get(position).get(5));

        holder.heart.setImageResource(R.drawable.save);
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {

                try{
                    db = context.openOrCreateDatabase("favourites",MODE_PRIVATE,null);
                    if(!isFavorite(list.get(position).get(2))) {
                        holder.heart.setImageResource(R.drawable.save);
                        Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("DELETE from fav where title = ?",new String[]{list.get(position).get(2)});
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
                listener.onClicked(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private boolean isFavorite(String news) {
        SQLiteDatabase db = context.openOrCreateDatabase("favourites", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS fav(title varchar(100) primary key,source varchar(100),time timestamp,url varchar(500))");
        Cursor cursor = db.rawQuery("SELECT * FROM fav WHERE title = ?", new String[]{news});
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isFavorite;
    }

}
