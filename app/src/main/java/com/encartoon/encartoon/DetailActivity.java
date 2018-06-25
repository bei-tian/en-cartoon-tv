package com.encartoon.encartoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DetailActivity extends Activity {

    private Integer index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://www.en-cartoon.com/api/" + (index + 1),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int total = 0;
                        try {
                            total = response.getJSONObject("data").getInt("total");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        GridView gridView = findViewById(R.id.episode);
                        com.encartoon.encartoon.DetailGirdAdapter detailGirdAdapter = new DetailGirdAdapter(DetailActivity.this, total);
                        gridView.setAdapter(detailGirdAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(DetailActivity.this, PlayActivity.class);
                                intent.putExtra("id", index + 1);
                                intent.putExtra("index", i);
                                startActivity(intent);
                            }
                        });
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
