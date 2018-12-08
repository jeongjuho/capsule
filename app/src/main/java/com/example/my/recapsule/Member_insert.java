package com.example.my.recapsule;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018-07-03.
 */

public class Member_insert extends StringRequest{
    final static private String URL = "http://ggcapsule.dothome.co.kr/member_input.php";
    private Map<String,String> parameters;

    public Member_insert(String userID, String userPass, String userName, String userNick, String userPnum1,String userPnum2,String userPnum3, String userEmail1,String userEmail2, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPass",userPass);
        parameters.put("userName",userName);
        parameters.put("userNick",userNick);
        parameters.put("userPnum1",userPnum1);
        parameters.put("userPnum2",userPnum2);
        parameters.put("userPnum3",userPnum3);
        parameters.put("userEmail1",userEmail1);
        parameters.put("userEmail2",userEmail2);


    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
