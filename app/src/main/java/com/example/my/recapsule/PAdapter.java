package com.example.my.recapsule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my.recapsule.sweet.Sweet_delete;
import com.example.my.recapsule.sweet.Sweet_insert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by my on 2018-09-15.
 */

public class PAdapter extends BaseAdapter {

    private AlertDialog dialog;
    String check;
    private Context mContext;
    private int mLayout;
    private List<InfoClass> mItemList;
    private List<InfoClass> saveList;
    private LayoutInflater mInflater;
    Subscribe subscribe = new Subscribe();

    public PAdapter(Context context, int layout, ArrayList<InfoClass> itemList, ArrayList<InfoClass> saveList) {
        this.mContext = context;
        this.mLayout = layout;
        this.mItemList = itemList;
        this.mInflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.saveList = saveList;
    }

    public PAdapter() {

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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            holder = new ViewHolder();
            holder.swcount = convertView.findViewById(R.id.swcount);
            holder.lvbt = convertView.findViewById(R.id.lvbt);
            holder.flbt = convertView.findViewById(R.id.flbt);
            holder.icon = (ImageView) convertView.findViewById(R.id.Liv);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            //TextView id=(TextView)convertView.findViewById(R.id.id);
            holder.nick = (TextView) convertView.findViewById(R.id.nick);
            holder.profile = (ImageView) convertView.findViewById(R.id.profile);
            //holder.addr=(TextView)convertView.findViewById(R.id.addr);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.profile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profile.setClipToOutline(true);

        holder.flbt.setFocusable(false);
        //holder.txtView.setText(mItemList.get(position).getName());
        //holder.imgIcon.setImageBitmap((position & 1 ) == 1 ? mIcon1 : mIcon2);

        holder.flbt.setImageResource(saveList.get(position).getFlimg());

        if(saveList.get(position).getFlimg()==R.drawable.follow) {
            aa(holder,context,position);

        }
        else {
            bb(holder,context,position);
        }

        if (mItemList.get(position).getIcon().equals("dummy")){
            holder.icon.setVisibility(View.GONE);
        }else {
            holder.icon.setVisibility(View.VISIBLE);
            Glide.with(context).load(mItemList.get(position).getIcon()).into(holder.icon);
        }



        holder.lvbt.setFocusable(false);
        holder.lvbt.setImageResource(saveList.get(position).getSwimg());

        if(saveList.get(position).getSwimg() == R.drawable.redheart){
            holder.lvbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Response.Listener<String> responseLisner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success= jsonObject.getBoolean("success");

                                if(success){
                                    saveList.get(position).setSwimg(R.drawable.heart);
                                    Toast.makeText(context,"좋아요취소",Toast.LENGTH_LONG).show();
                                    saveList.get(position).setSwcount(-1);
                                    RandomCapsule.adapter.notifyDataSetChanged();

                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(context,e+"예외",Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    Sweet_delete idcheck = new Sweet_delete(saveList.get(position).getNum(),Member_login.idSession,responseLisner);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(idcheck);
                }
            });
        }
        else {
            holder.lvbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Response.Listener<String> responseLisner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success= jsonObject.getBoolean("success");

                                if(success){
                                    saveList.get(position).setSwimg(R.drawable.redheart);
                                    Toast.makeText(context,"좋아요",Toast.LENGTH_LONG).show();
                                    saveList.get(position).setSwcount(1);
                                    RandomCapsule.adapter.notifyDataSetChanged();
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(context,e+"예외",Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    Sweet_insert idcheck = new Sweet_insert(saveList.get(position).getNum(),Member_login.idSession,responseLisner);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(idcheck);
                }
            });
        }


        String a = String.valueOf(saveList.get(position).getSwcount());
        holder.swcount.setText(a);
        holder.title.setText(mItemList.get(position).getTitle());
        holder.text.setText(mItemList.get(position).getText());
        //id.setText(infoClass.getId());

        Glide.with(context).load(mItemList.get(position).getProfile()).into(holder.profile);

        //Glide.with(context).load("http://ggcapsule.dothome.co.kr/person.png").into(holder.profile);

        //Glide.with(context).load(mItemList.get(position).getProfile()).into(holder.profile);
        if (mItemList.get(position).getStrPrivate().equals("true")) {
            holder.nick.setText("익명");
        } else {
            holder.nick.setText(mItemList.get(position).getNick());
        }

        //holder.addr.setText(mItemList.get(position).getAddr());

        return convertView;
    }


    public void reset(int position ,ViewHolder holder){
        for(int i = 0;i<RandomCapsule.saveList.size();i++){
        if(RandomCapsule.saveList.get(i).getId().equals(saveList.get(position).getId())){
            RandomCapsule.saveList.get(i).setFlimg(R.drawable.bleckfollow);
        }
        }
    }

    public void reset1(int position ,ViewHolder holder){
        for(int i = 0;i<RandomCapsule.saveList.size();i++){
            if(RandomCapsule.saveList.get(i).getId().equals(saveList.get(position).getId())){
                RandomCapsule.saveList.get(i).setFlimg(R.drawable.follow);
            }
        }
    }

    public void bb(final ViewHolder holder, final Context context, final int position){
        holder.flbt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                Response.Listener<String> responseLisner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success= jsonObject.getBoolean("success");

                            if(success){
                                reset1(position,holder);
                                RandomCapsule.adapter.notifyDataSetChanged();

                                subscribe.restart2(saveList.get(position).getId());
                                Log.d("subTest",saveList.get(position).getId());
                                //Subscribe.followAdapter.notifyDataSetChanged();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                dialog = builder.setMessage("팔로우를  취소하였습니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();

                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context,e+"예외",Toast.LENGTH_LONG).show();
                        }
                    }
                };
                if("http://ggcapsule.dothome.co.kr/profile/person.png".equals(mItemList.get(position).getProfile())){
                    check = "person.png";
                }
                else{
                    check = saveList.get(position).getId()+"profile.jpg";
                }


                Follow_delete idcheck = new Follow_delete(Member_login.idSession,saveList.get(position).getId(),responseLisner);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(idcheck);
            }
        });
    }



    public void aa(final ViewHolder holder, final Context context, final int position){
        holder.flbt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                Response.Listener<String> responseLisner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success= jsonObject.getBoolean("success");

                            if(success){
                                reset(position,holder);
                                RandomCapsule.adapter.notifyDataSetChanged();

                                subscribe.restart1(saveList.get(position).getId(),saveList.get(position).getProfile());
                                Subscribe.followAdapter.notifyDataSetChanged();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                dialog = builder.setMessage("팔로우 하였습니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                dialog = builder.setMessage("팔로우 할수 없습니다..")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context,e+"예외",Toast.LENGTH_LONG).show();
                        }
                    }
                };
                if("http://ggcapsule.dothome.co.kr/profile/person.png".equals(mItemList.get(position).getProfile())){
                    check = "person.png";
                }
                else{
                    check = saveList.get(position).getId()+"profile.jpg";
                }

                Follow_inset idcheck = new Follow_inset(Member_login.idSession,saveList.get(position).getId(),check,responseLisner);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(idcheck);
            }
        });
    }





    private class ViewHolder {
        ImageView lvbt;
        ImageView flbt;
        TextView swcount;
        TextView text;
        TextView title;
        TextView nick;
        ImageView icon;
        ImageView profile;
    }


}
