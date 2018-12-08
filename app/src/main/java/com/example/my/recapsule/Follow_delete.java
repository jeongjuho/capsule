package com.example.my.recapsule;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Follow_delete extends StringRequest {
    final static private String URL = "http://ggcapsule.dothome.co.kr/follow_delete.php";
    private Map<String,String> parameters;

    public Follow_delete(String myid, String flid,Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        parameters=new HashMap<>();
        parameters.put("myid",myid);
        parameters.put("flid",flid);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
