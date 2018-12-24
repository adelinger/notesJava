package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class update_partner extends StringRequest {
    private static final String URL = "https://autotoni.hr/notes/update_partner.php";
    private Map<String, String> params;

    public update_partner(String firstname, String lastname, String email, String phone, Integer id, Response.Listener<String> listener){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, URL, listener, null);
        params= new HashMap<>();
        params.put("firstname", firstname+"");
        params.put("lastname", lastname);
        params.put("email", email+"");
        params.put("phone", phone);
        params.put("id", id.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
