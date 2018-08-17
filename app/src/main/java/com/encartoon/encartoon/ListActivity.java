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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ListActivity extends Activity {
    private View lastItemView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Base app = (Base)getApplication();
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(app.getBaseUrl() + "all/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GridView gridView = findViewById(R.id.cartoon);

                        GridViewAdapter gridViewAdapter = new GridViewAdapter(ListActivity.this, R.layout.grid_item, response);
                        gridView.setAdapter(gridViewAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                                intent.putExtra("index", i);
                                startActivity(intent);
                            }
                        });
                        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                                LinearLayout item = (LinearLayout) view;
                                item.setBackgroundColor(Color.parseColor("#ffffff"));
                                TextView textView = (TextView) item.getChildAt(1);
                                textView.setTextColor(Color.parseColor("#000000"));

                                if(lastItemView ==null) {
                                    lastItemView = parent.getChildAt(0);
                                }
                                LinearLayout linearLayout = (LinearLayout) lastItemView;
                                linearLayout.setBackgroundColor(Color.TRANSPARENT);
                                TextView lastTextView = (TextView) linearLayout.getChildAt(1);
                                lastTextView.setTextColor(Color.parseColor("#ffffff"));

                                lastItemView = view;
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(ListActivity.this).setMessage("网络错误").create().show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
