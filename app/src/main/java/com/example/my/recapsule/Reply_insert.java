package com.example.my.recapsule;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018-07-03.
 */

public class Reply_insert extends StringRequest{
    final static private String URL = "http://ggcapsule.dothome.co.kr/reply_insert.php";
    private Map<String,String> parameters;

    public Reply_insert(String num,String nick, String text, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("num",num);
        parameters.put("nick",nick);
        parameters.put("text",text);

    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
