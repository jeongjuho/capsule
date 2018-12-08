package com.example.my.recapsule;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018-07-03.
 */

public class Writing_insert extends StringRequest{
    final static private String URL = "http://ggcapsule.dothome.co.kr/writing_insert.php";
    private Map<String,String> parameters;

    public Writing_insert(String text2,String text, String date, String gps,String id,String path,String nick,String profile,String wdate,String strPrivate, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("title",text2);
        parameters.put("content",text);
        parameters.put("date",date);
        parameters.put("gps",gps);
        parameters.put("id",id);
        parameters.put("path",path);
        parameters.put("nick",nick);
        parameters.put("profile",profile);
        parameters.put("wdate",wdate);
        parameters.put("strPrivate",strPrivate);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
