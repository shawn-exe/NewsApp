package com.example.newsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


public class Favourite extends AppCompatActivity implements SelectListener2,View.OnClickListener{
//    TextView t;
    SQLiteDatabase db;
    Context context;
    ProgressDialog dialog;
    ArrayList<ArrayList<String>> list;
    RecyclerView recyclerView;

    SelectListener2 listener;
FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        int i = 0;
        db = this.openOrCreateDatabase("favourites",MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * from fav",null);
        list = new ArrayList<>();
        if(c!=null){
            if(c.moveToNext()){
                do{

                    @SuppressLint("Range") String title = c.getString(c.getColumnIndex("title"));
                    @SuppressLint("Range") String url = c.getString(c.getColumnIndex("url"));
                    @SuppressLint("Range") String urlToImage = c.getString(c.getColumnIndex("urlToImage"));
                    @SuppressLint("Range") String publishedAt = c.getString(c.getColumnIndex("publishedAt"));
                    @SuppressLint("Range") String content = c.getString(c.getColumnIndex("content"));
                    @SuppressLint("Range") String source = c.getString(c.getColumnIndex("source"));
                    @SuppressLint("Range") String author = c.getString(c.getColumnIndex("author"));

                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(source);
                    dataList.add(author);
                    dataList.add(title);
                    dataList.add(url);
                    dataList.add(urlToImage);
                    dataList.add(publishedAt);
                    dataList.add(content);
                    System.out.println(dataList);
                    list.add(dataList);

                }while(c.moveToNext());
            }
        }
        showNews(list);
        c.close();
        db.close();

    }

    private void showNews(ArrayList<ArrayList<String>> list) {
        recyclerView = findViewById(R.id.recycler_favourite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new FavouriteAdapter(this, list,this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClicked(ArrayList<String> headlines) {
        String nurl = headlines.get(3);
        Intent intent = new Intent(getApplicationContext(), webView.class);
        intent.putExtra("url", nurl);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // Not needed for your implementation since you are using TabLayout
    }

}