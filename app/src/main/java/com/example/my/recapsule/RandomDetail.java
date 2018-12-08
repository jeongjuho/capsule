package com.example.my.recapsule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.LocationListener;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RandomDetail extends AppCompatActivity {

    TextView textview, titleview;
    ListView replylist;
    Button rpbt;
    EditText chat;
    String nick;
    int i;
    ReplyAdapter adapter;
    String num,image;
    ImageView cImage;
    TextView delBtn,synBtn;
    int position;

    ArrayList<InfoClass2> list;
    private Activity parentActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        textview = (TextView) findViewById(R.id.text);
        titleview = (TextView)findViewById(R.id.Title);
        replylist = (ListView)findViewById(R.id.Replylist);
        rpbt = (Button)findViewById(R.id.rpbt);
        chat = (EditText)findViewById(R.id.chat);
        cImage = (ImageView)findViewById(R.id.cimage);

        ImageView profileImage =(ImageView)findViewById(R.id.profile);
        TextView nickView=(TextView)findViewById(R.id.nick);
        Intent intent = getIntent();
        String title=intent.getStringExtra("title");
        nick = intent.getStringExtra("nick");
        String text = intent.getStringExtra("text");
        num = intent.getStringExtra("num");
        image=intent.getStringExtra("image");
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);
        position=intent.getIntExtra("pos",0);
        String profile=intent.getStringExtra("profile");
        String addr =intent.getStringExtra("gps");
        String strPrivate=intent.getStringExtra("strPrivate");
        titleview.setText(title);

        if (strPrivate.equals("true")){
            nickView.setText("익명");

        }else {
            nickView.setText(nick);

        }
        if (image.equals("dummy")){
            cImage.setVisibility(View.GONE);
        }else {
            Glide.with(RandomDetail.this).load(image).into(cImage);
        }

        Glide.with(RandomDetail.this).load(profile).into(profileImage);


        textview.setText(text);

        adapter =  new ReplyAdapter();
        replylist.setAdapter(adapter);
        final BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();

        rpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chat.getText().toString();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success= jsonObject.getBoolean("success");
                            adapter.additem(new ReplyInfoClass(Member_login.nick, chat.getText().toString()));
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                Reply_insert memberInsert = new Reply_insert(num,Member_login.nick,text,listener);
                RequestQueue queue = Volley.newRequestQueue(RandomDetail.this);
                queue.add(memberInsert);
            }
        });

    }

    class  ReplyAdapter extends BaseAdapter{
        ArrayList<ReplyInfoClass> ritem = new ArrayList<ReplyInfoClass>();
        @Override
        public int getCount() {
            return ritem.size();
        }

        public void additem(ReplyInfoClass item){
            ritem.add(item);
        }

        @Override
        public Object getItem(int position) {
            return ritem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReplyCustomAdapter view = new ReplyCustomAdapter(getApplicationContext());
            ReplyInfoClass item = ritem.get(position);
            view.setid(item.getId());
            view.setText(item.getText());
            return view;
        }
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;
        @Override
        protected void onPreExecute(){
            target="http://ggcapsule.dothome.co.kr/reply_list.php";
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
                String tilte,text,number;
                while (count < jsonArray.length()){
                    JSONObject object =  jsonArray.getJSONObject(count);
                    number =object.getString("number");
                    if(number.equals(num)) {
                        nick = object.getString("nick");
                        text = object.getString("text");
                        adapter.additem(new ReplyInfoClass(nick, text));
                    }
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
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(String result){

        }
    }

}