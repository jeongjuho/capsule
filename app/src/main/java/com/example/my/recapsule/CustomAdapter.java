package com.example.my.recapsule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private int mLayout;
    private List<InfoClass2> mItemList;
    private List<InfoClass> saveList;
    private LayoutInflater mInflater;



    public CustomAdapter(Context mContext, int mLayout, List<InfoClass2> mItemList) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mItemList = mItemList;
        this.mInflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        ;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void deleteItem(int position){
        mItemList.remove(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        CustomAdapter.ViewHolder holder;








        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mylistview_item, parent, false);
            holder = new CustomAdapter.ViewHolder();

            holder.profile = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.date=(TextView)convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (CustomAdapter.ViewHolder) convertView.getTag();
        }

        //holder.txtView.setText(mItemList.get(position).getName());
        //holder.imgIcon.setImageBitmap((position & 1 ) == 1 ? mIcon1 : mIcon2);

        //Glide.with(context).load(mItemList.get(position).getIcon()).into(holder.profile);

        Glide.with(context).load("http://ggcapsule.dothome.co.kr/mycapsule1.png").into(holder.profile);
        if (Integer.parseInt(mItemList.get(position).getDeadlind())<=0){
            holder.date.setText("오픈가능");
        }
        else {
            holder.date.setText("D-"+mItemList.get(position).getDeadlind());
        }

        return convertView;
    }

    private class ViewHolder {
        TextView date;
        ImageView profile;
    }
}









