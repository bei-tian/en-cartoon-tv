package com.encartoon.encartoon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class PlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        int index = intent.getIntExtra("index",0);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest("http://www.en-cartoon.com/api/play/"+id+"/"+ (index+1) +"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Uri uri = Uri.parse(s);
//                        VideoView videoView = findViewById(R.id.videoView);
//                        videoView.setMediaController(new MediaController(PlayActivity.this));
//                        videoView.setVideoURI(uri);
//                        videoView.start();
//                        videoView.requestFocus();

                        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoPlayer);
                        jzVideoPlayerStandard.setUp(s, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");
                        jzVideoPlayerStandard.startButton.callOnClick();

                        //jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        JZVideoPlayer.goOnPlayOnPause();
        super.onBackPressed();
    }

}
