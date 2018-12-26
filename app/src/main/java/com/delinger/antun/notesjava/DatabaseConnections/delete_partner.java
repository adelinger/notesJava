package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class delete_partner extends StringRequest {
    private static final String URL = "https://autotoni.hr/notes/delete_partner.php";
    private Map<String, String> params;

    public delete_partner(Integer id, Response.Listener<String> listener){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, URL, listener, null);
        params= new HashMap<>();
        params.put("id", id.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
