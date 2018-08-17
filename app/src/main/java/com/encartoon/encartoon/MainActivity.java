package com.encartoon.encartoon;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //全部动画
        ImageView all = findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        //观看记录
        ImageView history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });


        //焦点移动
        TableLayout rootView = findViewById(R.id.rootView);
        rootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus != null) {
                    //newFocus.animate().scaleX(1.05f).scaleY(1.05f).translationZ(1.1f).setDuration(200).start();
                    //newFocus.setPadding(5,5,5,5);
                    newFocus.setBackgroundResource(R.drawable.shape_gray_square_bg);
                }
                if (oldFocus != null) {
                    //oldFocus.animate().scaleX(1.0f).scaleY(1.0f).translationZ(1.0f).setDuration(200).start();
                    //oldFocus.setPadding(0,0,0,0);
                    oldFocus.setBackground(null);
                }
            }
        });

        //首页数据
        Base app = (Base)getApplication();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(app.getBaseUrl(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ViewGroup.LayoutParams img_params = findViewById(R.id.item_img).getLayoutParams();
                        ViewGroup.LayoutParams linear_params = findViewById(R.id.item_linear).getLayoutParams();

                        TableLayout newCartoon = findViewById(R.id.newCartoon);
                        TableRow tableRow = null;
                        try {
                            JSONArray data = response.getJSONArray("data");
                            //首页推荐
                            JSONArray tuijian = data.getJSONArray(0);
                            JSONObject bigImgItem = tuijian.getJSONObject(0);
                            ImageView index_big = findViewById(R.id.index_big);
                            Picasso.with(MainActivity.this).load("http://api.en-cartoon.com/" + bigImgItem.getString("image")).into(index_big);
                            final int bigItemId = Integer.parseInt(bigImgItem.getString("remarks"));
                            index_big.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                    intent.putExtra("id", bigItemId);
                                    startActivity(intent);
                                }
                            });

                            TableLayout index_list = findViewById(R.id.index_list);
                            int index = 0;
                            for(int j=0; j<2; j++) {
                                TableRow tableRow1 = (TableRow) index_list.getChildAt(j);
                                for(int k=0; k<3; k++) {
                                    index ++;
                                    ImageView imageView = (ImageView) tableRow1.getChildAt(k);
                                    String image = tuijian.getJSONObject(index).getString("image");
                                    String remarks = tuijian.getJSONObject(index).getString("remarks");
                                    Picasso.with(MainActivity.this).load("http://api.en-cartoon.com/" + image).into(imageView);
                                    final int finalId = Integer.parseInt(remarks);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                            intent.putExtra("id", finalId);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }



                            //最新更新
                            JSONArray updated = data.getJSONArray(1);
                            int count = updated.length();
                            for(int i = 0;i<count; i++){
                                if((i%5) == 0) {
                                    tableRow = new TableRow(MainActivity.this);
                                }
                                JSONObject item = updated.getJSONObject(i);
                                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setLayoutParams(linear_params);

                                //封面图
                                ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setFocusable(true);
                                imageView.setFocusableInTouchMode(true);
                                imageView.setPadding(6,6,6,6);
                                linearLayout.addView(imageView);
                                Picasso.with(MainActivity.this).load("http://api.en-cartoon.com/" + item.getString("cover")).into(imageView);
                                imageView.setLayoutParams(img_params);
                                final int id = item.getInt("id");
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                });

                                //标题
                                TextView textView = new TextView(MainActivity.this);
                                textView.setText(item.getString("name"));
                                linearLayout.addView(textView);

                                tableRow.addView(linearLayout);

                                if((i%5) == 0) {
                                    newCartoon.addView(tableRow);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(MainActivity.this).setMessage("网络错误").create().show();
                }
            });
        requestQueue.add(jsonObjectRequest);
    }

}
