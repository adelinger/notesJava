package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class get_cars_for_user extends StringRequest {
    private static final String partnerDataURL = "https://autotoni.hr/notes/get_cars.php";
    private Map<String, String> params;

    public get_cars_for_user(Response.Listener<String> listener, Integer id){
        super(Request.Method.POST, partnerDataURL, listener, null);
        params= new HashMap<>();
        params.put("id", id.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
