package com.encartoon.encartoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class DetailActivity extends Activity {

    private Integer id;
    private int lastGirdItemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        final int episode = intent.getIntExtra("episode",1);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Base app = (Base)getApplication();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(app.getBaseUrl() + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //基本信息
                            JSONObject data = response.getJSONObject("data");
                            ImageView cover = findViewById(R.id.cover);
                            Picasso.with(DetailActivity.this).load("http://api.en-cartoon.com/" + data.getString("cover")).into(cover);
                            TextView title = findViewById(R.id.title);
                            title.setText(data.getString("name"));
                            TextView description = findViewById(R.id.description);
                            description.setText("简介：" + data.getString("description"));

                            //剧集列表
                            final int total = data.getInt("total");
                            GridView gridView = findViewById(R.id.episode);
                            com.encartoon.encartoon.DetailGirdAdapter detailGirdAdapter = new DetailGirdAdapter(DetailActivity.this, total);
                            gridView.setAdapter(detailGirdAdapter);
                            ScrollView scrollView = findViewById(R.id.detailBox);
                            scrollView.smoothScrollTo(0,0);

                            final String name = data.getString("name");
                            final String coverStr = data.getString("cover");
                            gridView.setSelection(episode-1);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(DetailActivity.this, PlayActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("index", i);
                                    intent.putExtra("name", name);
                                    intent.putExtra("cover", coverStr);
                                    intent.putExtra("total", total);
                                    startActivity(intent);
                                }
                            });
                            gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                                    TextView textView = (TextView) view;
                                    textView.setTextColor(Color.parseColor("#000000"));
                                    TextView lastTextView = (TextView) parent.getChildAt(lastGirdItemPos);
                                    lastTextView.setTextColor(Color.parseColor("#eeeeee"));
                                    lastGirdItemPos = position;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(DetailActivity.this).setMessage("网络错误").create().show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
