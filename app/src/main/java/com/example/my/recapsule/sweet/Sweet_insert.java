package com.example.my.recapsule.sweet;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Sweet_insert extends StringRequest {
    final static private String URL = "http://ggcapsule.dothome.co.kr/sweet_insert.php";
    private Map<String,String> parameters;

    public Sweet_insert(String num, String swid, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        parameters=new HashMap<>();
        parameters.put("num",num);
        parameters.put("swid",swid);
    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
