package com.example.refrigeratorgo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://zoooz0616.dothome.co.kr/LoginRegister.php";
    private Map<String, String> map;

    public RegisterRequest(String user_name, String user_id, String user_email, String rfg_name, String user_pw, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", user_id);
        map.put("userPwd", user_pw);
        map.put("userName", user_name);
        map.put("userEmail", user_email);
        map.put("rfgName", rfg_name);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
