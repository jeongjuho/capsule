package com.example.my.recapsule;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by my on 2018-07-03.
 */

public class Member_login extends AppCompatActivity {
    private AlertDialog alertDialog;
    Button loginbt;
    Button memberbt;
    EditText idedit;
    EditText psedit;
    CheckBox checkBox;
    Boolean loginChecked=false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String id,pass;
    boolean success;
    public static int num;
    public static String nick;
    public static String idSession=null;
    public static boolean idState=false;

    @Override
    protected void onStop() {
        super.onStop();
        if(alertDialog!=null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_login);


      /*  if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLUE);
        }
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.WHITE);
            }
        }*/

        loginbt = (Button)findViewById(R.id.login_bt);
        memberbt = (Button)findViewById(R.id.membt);
        idedit = (EditText)findViewById(R.id.ided);
        psedit = (EditText)findViewById(R.id.psed);
        checkBox = (CheckBox)findViewById(R.id.check);

        pref = getSharedPreferences("setting",0);
        editor = pref.edit();

        if (pref.getBoolean("autoLogin", false)) {
            idedit.setText(pref.getString("id", ""));
            psedit.setText(pref.getString("pw", ""));
            checkBox.setChecked(true);
            idSession=pref.getString("id", "");
            new getUserInfo().execute();
            Toast.makeText(getApplicationContext(),"자동로그인",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),FragmentAcivity.class);
            intent.putExtra("id",idedit.getText().toString());
            startActivity(intent);
            finish();
        }


        memberbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Member_form.class);
                startActivity(intent);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    loginChecked=true;
                }
                else{
                    loginChecked=false;
                }
            }
        });


        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idedit.getText().toString();
                pass= psedit.getText().toString();

                Response.Listener<String> respons = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            success = jsonResponse.getBoolean("success");
                            if(success){
                                idSession=idedit.getText().toString();
                                idState=true;

                                if(loginChecked){
                                    editor.putString("id", id);
                                    editor.putString("pw", pass);
                                    editor.putBoolean("autoLogin", true);

                                    editor.commit();
                                    Toast.makeText(getApplicationContext(),"저장완료",Toast.LENGTH_LONG).show();
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(Member_login.this);
                                alertDialog = builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new getUserInfo().execute();
                                                Intent intent = new Intent(getApplicationContext(),FragmentAcivity.class);
                                                intent.putExtra("id",idedit.getText().toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .create();
                                alertDialog.show();


                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Member_login.this);
                                alertDialog = builder.setMessage("계정을 다시 확인하세요.")
                                        .setNegativeButton("다시시도",null)
                                        .create();
                                alertDialog.show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(id,pass,respons);
                RequestQueue queue = Volley.newRequestQueue(Member_login.this);
                queue.add(loginRequest);
            }
        });

    }
    class getUserInfo extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/userInfo.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String postParameters = "userId=" + idSession;
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


                    JSONObject object = jsonArray.getJSONObject(count);

                    num = Integer.parseInt(object.getString("num"))+1;
                    nick= object.getString("nick");
                    Log.d("numTest",Integer.toString(num));
                    Log.d("nickTest",nick);
                    /*URL myFileUrl = new URL("http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image"));
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    Bitmap bmImg = BitmapFactory.decodeStream(is);*/



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
