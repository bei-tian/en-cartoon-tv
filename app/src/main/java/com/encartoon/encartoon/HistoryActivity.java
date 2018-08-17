package com.encartoon.encartoon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //焦点移动
        TableLayout rootView = findViewById(R.id.rootView);
        rootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus != null) {
                    newFocus.setBackgroundResource(R.drawable.shape_gray_square_bg);
                }
                if (oldFocus != null) {
                    oldFocus.setBackground(null);
                }
            }
        });



        SQLiteDatabase db = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, cover VARCHAR, movie_id SMALLINT, episode SMALLINT)");
        Cursor c = db.rawQuery("SELECT * FROM history order by id desc limit 20", new String[]{});

        ViewGroup.LayoutParams img_params = findViewById(R.id.item_img).getLayoutParams();
        ViewGroup.LayoutParams linear_params = findViewById(R.id.item_linear).getLayoutParams();
        TableLayout playHistory = findViewById(R.id.playHistory);
        TableRow tableRow = null;
        int i = 0;
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            final int movie_id = c.getInt(c.getColumnIndex("movie_id"));
            final int episode = c.getInt(c.getColumnIndex("episode"));
            String name = c.getString(c.getColumnIndex("name"));
            String cover = c.getString(c.getColumnIndex("cover"));

            if((i%5) == 0) {
                tableRow = new TableRow(HistoryActivity.this);
            }
            LinearLayout linearLayout = new LinearLayout(HistoryActivity.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(linear_params);

            //封面图
            ImageView imageView = new ImageView(HistoryActivity.this);
            imageView.setFocusable(true);
            imageView.setFocusableInTouchMode(true);
            imageView.setPadding(6,6,6,6);
            linearLayout.addView(imageView);
            Picasso.with(HistoryActivity.this).load("http://api.en-cartoon.com/" + cover).into(imageView);
            imageView.setLayoutParams(img_params);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                    intent.putExtra("id", movie_id);
                    intent.putExtra("episode", episode);
                    startActivity(intent);
                }
            });

            //标题
            TextView textView = new TextView(HistoryActivity.this);
            textView.setText(name+"（第 "+ episode +" 集）");
            linearLayout.addView(textView);

            tableRow.addView(linearLayout);

            if((i%5) == 0) {
                playHistory.addView(tableRow);
            }

            i++;
        }

        c.close();
        db.close();
    }
}
