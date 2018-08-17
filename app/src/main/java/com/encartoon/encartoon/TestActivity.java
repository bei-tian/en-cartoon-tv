package com.encartoon.encartoon;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //打开或创建test.db数据库
        SQLiteDatabase db = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, cover VARCHAR, movie_id SMALLINT, episode SMALLINT)");
        Cursor c = db.rawQuery("SELECT * FROM history order by id desc limit 20", new String[]{});

        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("id"));
            String name = c.getString(c.getColumnIndex("name"));
            Log.i("db", "_id=>" + _id + ", name=>" + name );
        }
        c.close();


        //关闭当前数据库
        db.close();
    }

}
