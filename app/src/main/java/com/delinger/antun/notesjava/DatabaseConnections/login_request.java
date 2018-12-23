package com.delinger.antun.notesjava.DatabaseConnections;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class login_request extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://autotoni.hr/notes/login.php";
    private Map<String, String> params;

    public login_request(String username, String password, Response.Listener<String> listener, Response.ErrorListener er ){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, er);
        params= new HashMap<>();
        params.put("username", username+"");
        params.put("password", password);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
