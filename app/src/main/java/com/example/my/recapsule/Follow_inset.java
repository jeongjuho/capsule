package com.example.my.recapsule;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Follow_inset extends StringRequest {
    final static private String URL = "http://ggcapsule.dothome.co.kr/follow_insert.php";
    private Map<String,String> parameters;

    public Follow_inset(String myid, String flid,String profile, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        parameters=new HashMap<>();
        parameters.put("myid",myid);
        parameters.put("flid",flid);
        parameters.put("profile",profile);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
