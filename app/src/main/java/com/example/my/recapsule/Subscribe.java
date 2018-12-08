package com.example.my.recapsule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by my on 2018-09-10.
 */

public class Subscribe extends Fragment {

    public static ArrayList<FollowInfoClass> fitem;
    public static ArrayList<FollowInfoClass> additem;
    private RecyclerView mHorizontalView;
    public static  HorizontalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static ArrayList<HorizontalData> data;
    final Follow follow = new Follow();
    public Context fContext;
    public static FollowAdapter followAdapter;

    private int MAX_ITEM_COUNT = 50;

    final MainFollowList mainFollowList = new MainFollowList();
    public static Context mContext;
    ListView flist;
    String adid,adtitle,adtext,admain,adpro;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow_view, container, false);
        mContext = getActivity();

        mHorizontalView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL


        mHorizontalView.setLayoutManager(mLayoutManager);

        flist =(ListView)view.findViewById(R.id.followlist);

        fitem= new ArrayList<FollowInfoClass>();
        additem= new ArrayList<FollowInfoClass>();
        followAdapter = new FollowAdapter(getContext(),fitem);
        flist.setAdapter(followAdapter);

        mAdapter = new HorizontalAdapter();
        data = new ArrayList<>();

        // set Data
        mAdapter.setData(data);
        // set Adapter
        mHorizontalView.setAdapter(mAdapter);


        follow.execute();
        mainFollowList.execute();


        /*flist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FollowInfoClass item = (FollowInfoClass) flist.getItemAtPosition(position);
                Intent it = new Intent(getContext(), RandomDetail.class);
                it.putExtra("title", item.getTitle());
                it.putExtra("text", item.getText());
                //it.putExtra("nick", item.getNick());
                it.putExtra("profile", item.getFroimg());
                it.putExtra("image", item.getMainimg());
                //it.putExtra("strPrivate", item.getStrPrivate());
                startActivity(it);
            }
        });*/
        return view;
    }

    public void restart1(String id,String img){
        data.add(new HorizontalData(img,id));

    for(int i =0;i<additem.size();i++){
        if(additem.get(i).getId().equals(id)){
            adid = additem.get(i).getId();
            adtitle  = additem.get(i).getTitle();
            adtext = additem.get(i).getText();
            admain = additem.get(i).getMainimg();
            adpro = additem.get(i).getFroimg();
            fitem.add(new FollowInfoClass(adid,adtitle,adtext,admain,adpro));
        }
    }
    followAdapter.notifyDataSetChanged();
    mAdapter.notifyDataSetChanged();
    }
    public void restart2(String id){
        Log.d("size1",Integer.toString(data.size()));
        for(int i =0;i<data.size();i++){
       if(data.get(i).getText().equals(id)){
           data.remove(i);
           Log.d("size2",Integer.toString(data.size()));
       }}

        for(int i= fitem.size()-1; i>=0; i--) {
            Log.d("size3",Integer.toString(fitem.size()));
            if(fitem.get(i).getId().equals(id)) {
                fitem.remove(i);

                Log.d("size4",Integer.toString(fitem.size()));

            }
        }

        mAdapter.notifyDataSetChanged();
        followAdapter.notifyDataSetChanged();
    }

    class HorizontalData {

        private String img;
        private String text;

        public HorizontalData(String img, String text){
            this.img = img;
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public String getImg() {
            return this.img;
        }
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView description;

        public HorizontalViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.zzzaaa);
            description = (TextView) itemView.findViewById(R.id.youflid);
            icon.setBackground(new ShapeDrawable(new OvalShape()));
            icon.setClipToOutline(true);

        }
    }

    class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {

        private ArrayList<HorizontalData> HorizontalDatas;

        public void setData(ArrayList<HorizontalData> list){
            HorizontalDatas = list;
        }

        @Override
        public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.followimg_item, parent, false);

            HorizontalViewHolder holder = new HorizontalViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(HorizontalViewHolder holder, int position) {
            HorizontalData data1 = HorizontalDatas.get(position);

            holder.description.setText(data1.getText());

            Glide.with(mContext).load(data1.getImg()).into(holder.icon);
        }
        @Override
        public int getItemCount() {
            return HorizontalDatas.size();
        }
    }


    class MainFollowList extends AsyncTask<Void, Void, String> {
        String target;


        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/follow_list2.php";
        }

        @Override
        protected String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp=bufferedReader.readLine())!=null){
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count =0;
                String title,text,id,cfe,pimage;
                while (count < jsonArray.length()){
                    JSONObject object =  jsonArray.getJSONObject(count);
                    id = object.getString("id");
                    title = object.getString("title");
                    text = object.getString("text");
                    cfe = "http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image");
                    pimage = "http://ggcapsule.dothome.co.kr/profile/" + object.getString("pimage");
                    additem.add(new FollowInfoClass(id,title,text,cfe,pimage));

                    for(int i =0;i<data.size();i++) {
                        if (id.equals(data.get(i).getText())){
                            title = object.getString("title");
                            text = object.getString("text");
                            id = object.getString("id");
                            cfe = "http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image");
                            pimage = "http://ggcapsule.dothome.co.kr/profile/" + object.getString("pimage");
                            fitem.add(new FollowInfoClass(id, title, text,cfe,pimage));
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            followAdapter.notifyDataSetChanged();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    count++;
                }
                Log.d("errorCord",stringBuilder.toString());
                return stringBuilder.toString().trim();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

        }
    }

    class Follow extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/follow_list1.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String postParameters = "userId=" + Member_login.idSession;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("JSONTEST",stringBuilder.toString());
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;


                String fid,pimage;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    fid = object.getString("fid");
                    pimage = "http://ggcapsule.dothome.co.kr/profile/" + object.getString("pimage");

                    data.add(new HorizontalData(pimage, fid));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            followAdapter.notifyDataSetChanged();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    count++;
                }


                Log.d("errorCord", stringBuilder.toString());
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

        }
    }
}