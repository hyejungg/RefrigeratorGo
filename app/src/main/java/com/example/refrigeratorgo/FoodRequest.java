package com.example.refrigeratorgo;

import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FoodRequest extends StringRequest {
    final static private String URL = "http://zoooz0616.dothome.co.kr/FoodInsert.php";
    private Map<String, String> map;


    public FoodRequest(String food_index, String food_name, String rfg_name, String memo, String food_date, String category, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("foodIndex", food_index);
        map.put("foodName", food_name);
        map.put("rfg_name", rfg_name);
        map.put("memo", memo);
        map.put("foodDate", food_date);
        map.put("category", category);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
