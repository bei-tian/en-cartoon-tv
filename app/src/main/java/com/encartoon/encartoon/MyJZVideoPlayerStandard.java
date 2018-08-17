package com.encartoon.encartoon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.jzvd.JZVideoPlayerStandard;

public class MyJZVideoPlayerStandard extends JZVideoPlayerStandard {
    private Context mContext;
    public MyJZVideoPlayerStandard(Context context) {
        super(context);
        mContext=context;
    }

    public MyJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        mContext=context;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public int getLayoutId() {
        return cn.jzvd.R.layout.jz_layout_standard;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        SQLiteDatabase db = mContext.openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM history order by id desc limit 1", new String[]{});
        if (c.moveToFirst()) {
            int movie_id = c.getInt(c.getColumnIndex("movie_id"));
            int episode = c.getInt(c.getColumnIndex("episode"));
            String name = c.getString(c.getColumnIndex("name"));
            String cover = c.getString(c.getColumnIndex("cover"));
            int total = c.getInt(c.getColumnIndex("total"));
            if(episode < total) {
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("id", movie_id);
                intent.putExtra("index", episode);
                intent.putExtra("name", name);
                intent.putExtra("cover", cover);
                intent.putExtra("total", total);
                mContext.startActivity(intent);
            }
        }

    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }

}
