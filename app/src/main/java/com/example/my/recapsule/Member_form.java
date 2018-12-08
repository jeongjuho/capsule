package com.example.my.recapsule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by my on 2018-07-03.
 */

public class Member_form extends AppCompatActivity {

    private String userID;
    private String userPass;
    private String userName;
    private String userNick;
    private String userPnum1;
    private  String userPnum2;
    private String userPnum3;
    private  String userEmail1;
    private String userEmail2;
    private AlertDialog dialog;
    private boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_form);

        final EditText idEt=(EditText)findViewById(R.id.id_et);
        final EditText passEt=(EditText)findViewById(R.id.pass_et);
        final EditText nameEt=(EditText)findViewById(R.id.name_et);
        final EditText nickEt=(EditText)findViewById(R.id.nick_et);
        final EditText pnumEt1=(EditText)findViewById(R.id.pnum_et1);
        final EditText pnumEt2=(EditText)findViewById(R.id.pnum_et2);
        final EditText pnumEt3=(EditText)findViewById(R.id.pnum_et3);
        final EditText emailEt1=(EditText)findViewById(R.id.email_et1);
        final EditText emailEt2=(EditText)findViewById(R.id.email_et2);

        final Button idcheck = (Button)findViewById(R.id.id_btn);
        Button okButton=(Button)findViewById(R.id.ok_btn);
        Button cancelButton=(Button)findViewById(R.id.cancle_btn);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    //중복확인
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID= idEt.getText().toString();
                if(check)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_form.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseLisner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success= jsonObject.getBoolean("success");

                             if(success){
                                Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Member_form.this);
                                dialog = builder.setMessage("사용 가능한 아이디 입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                idEt.setEnabled(false);
                                check = true;
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Member_form.this);
                                dialog = builder.setMessage("사용 할 수 없는 아이디 입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (JSONException e){
                                e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"예외",Toast.LENGTH_LONG).show();
                        }
                    }
                };
                Member_check idcheck = new Member_check(userID,responseLisner);
                RequestQueue queue = Volley.newRequestQueue(Member_form.this);
                queue.add(idcheck);
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = idEt.getText().toString();
                userPass = passEt.getText().toString();
                userName = nameEt.getText().toString();
                userNick = nickEt.getText().toString();
                userPnum1 = pnumEt1.getText().toString();
                userPnum2 = pnumEt2.getText().toString();
                userPnum3 = pnumEt3.getText().toString();
                userEmail1 = emailEt1.getText().toString();
                userEmail2 = emailEt2.getText().toString();

                if(!check)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_form.this);
                    dialog = builder.setMessage("중복체크 확인해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(userID.equals("")|| userPass.equals("")|| userName.equals("")||userNick.equals("")
                   ||userPnum1.equals("")||userPnum2.equals("")||userPnum3.equals("")
                   ||userEmail1.equals("")||userEmail2.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_form.this);
                    dialog = builder.setMessage("빈칸 없이 입력 하십시요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success= jsonObject.getBoolean("success");
                            if (success){
                                AlertDialog.Builder builder=new AlertDialog.Builder(Member_form.this);
                                builder.setMessage("회원가입에 성공 했습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create().show();

                            }else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(Member_form.this);
                                builder.setMessage("회원가입에 실패 했습니다.")
                                        .setNegativeButton("다시 시도",null)
                                        .create().show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                Member_insert memberInsert = new Member_insert(userID,userPass,userName,userNick,userPnum1,userPnum2,userPnum3,userEmail1,userEmail2,listener);
                RequestQueue queue = Volley.newRequestQueue(Member_form.this);
                queue.add(memberInsert);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
