package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class get_transactions extends StringRequest {
    private static final String partnerDataURL = "https://autotoni.hr/notes/get_transactions.php";
    private Map<String, String> params;

    public get_transactions(Response.Listener<String> listener ){
        super(Request.Method.POST, partnerDataURL, listener, null);
        params= new HashMap<>();

    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
