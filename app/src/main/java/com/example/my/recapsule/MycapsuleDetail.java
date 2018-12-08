package com.example.my.recapsule;

import android.app.Activity;
import android.content.Intent;
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

public class MycapsuleDetail extends AppCompatActivity {

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
        setContentView(R.layout.activity_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        textview = (TextView) findViewById(R.id.text);
        titleview = (TextView)findViewById(R.id.Title);
        replylist = (ListView)findViewById(R.id.Replylist);
        rpbt = (Button)findViewById(R.id.rpbt);
        chat = (EditText)findViewById(R.id.chat);
        cImage = (ImageView)findViewById(R.id.cimage);
        delBtn=(TextView)findViewById(R.id.delBtn);
        synBtn=(TextView)findViewById(R.id.synBtn);
        Intent intent = getIntent();
        nick = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        num = intent.getStringExtra("num");
        image=intent.getStringExtra("image");
        position=intent.getIntExtra("pos",0);
        Glide.with(MycapsuleDetail.this).load(image).into(cImage);
        textview.setText(text);
        titleview.setText(nick);
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
                RequestQueue queue = Volley.newRequestQueue(MycapsuleDetail.this);
                queue.add(memberInsert);
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NUMTEST",num);
                new deleteCapsule().execute();
                deleteItem(position);
                finish();

            }
        });
    }
    public void deleteItem(int position){
        MyCapsule.customAdapter.deleteItem(position);
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
    class deleteCapsule extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/delete.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                /*String postParameters = "num=" + num;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();*/
              HttpPostData();





                return null;
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
    public void HttpPostData() {
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://ggcapsule.dothome.co.kr/delete.php");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            //http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            buffer.append("strNum").append("=").append(Integer.parseInt(num));                 // php 변수에 값 대입

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            final String myResult = builder.toString();                       // 전송결과를 전역 변수에 저장



        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData
}