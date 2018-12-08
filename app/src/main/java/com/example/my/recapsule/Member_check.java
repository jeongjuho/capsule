package com.example.my.recapsule;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018-07-03.
 */

public class Member_check extends StringRequest{
    final static private String URL = "http://ggcapsule.dothome.co.kr/idcheck.php";
    private Map<String,String> parameters;

    public Member_check(String userID, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("userID",userID);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
