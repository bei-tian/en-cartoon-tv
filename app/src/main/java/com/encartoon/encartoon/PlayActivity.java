package com.encartoon.encartoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cn.jzvd.JZVideoPlayerStandard;

public class PlayActivity extends Activity {

    private MyJZVideoPlayerStandard jzVideoPlayerStandard;

    private String name;

    public String getName() {
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        int index = intent.getIntExtra("index",0);
        name = intent.getStringExtra("name");
        String cover = intent.getStringExtra("cover");
        int total = intent.getIntExtra("total",0);


        //写入播放记录
        SQLiteDatabase db = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, cover VARCHAR, movie_id SMALLINT, episode SMALLINT, total SMALLINT)");
        Cursor c = db.rawQuery("SELECT * FROM history WHERE movie_id = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            db.execSQL("delete from history where movie_id = " + id);
        }
        db.execSQL("INSERT INTO history VALUES (NULL, ?, ?, ?, ?, ?)", new Object[]{name, cover, id, index+1,total});
        db.close();


        //请求播放地址
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Base app = (Base)getApplication();
        StringRequest stringRequest = new StringRequest(app.getBaseUrl()+"play/"+id+"/"+ (index+1) +"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        jzVideoPlayerStandard = (MyJZVideoPlayerStandard) findViewById(R.id.videoPlayer);
                        jzVideoPlayerStandard.setUp(s,
                                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                                name);
                        jzVideoPlayerStandard.startVideo();

                        //jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(PlayActivity.this).setMessage("网络错误").create().show();
                    }
                });
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        jzVideoPlayerStandard.startButton.performClick();
        super.onBackPressed();
    }
}
