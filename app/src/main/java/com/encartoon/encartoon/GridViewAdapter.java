package com.encartoon.encartoon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private int layoutResourceId;
    private JSONObject data;

    public GridViewAdapter(Context context, int resource, JSONObject data) {
        super();
        this.mContext = context;
        this.data = data;
        this.layoutResourceId = resource;
    }
    @Override
    public int getCount() {
        return 7;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            JSONObject item = data.getJSONArray("data").getJSONObject(position);
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            TextView textView = convertView.findViewById(R.id.txt_item);
            textView.setText(item.getString("name"));
            ImageView imageView = convertView.findViewById(R.id.img_item);
            Picasso.with(mContext).load("http://www.en-cartoon.com/" + item.getString("cover")).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


}
