package com.example.my.recapsule;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{
    final static private String URL = "http://ggcapsule.dothome.co.kr/Login.php";
    private Map<String,String> parameters;

    public LoginRequest(String userID, String userPass, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPass",userPass);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
