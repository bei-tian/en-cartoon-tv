package com.encartoon.encartoon;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailGirdAdapter extends BaseAdapter {
    private  Context mContext;
    private int total;
    public DetailGirdAdapter(Context context, int total) {
        super();
        this.mContext = context;
        this.total = total;
    }

    @Override
    public int getCount() {
        return total;
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
        TextView textView = new TextView(mContext);
        textView.setText("第" + (position + 1) + "集");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,20,0,20);
        textView.setTextColor(Color.parseColor("#eeeeee"));
        if (position == 0) {
            textView.setTextColor(Color.parseColor("#000000"));
        }
        return  textView;
    }
}
