package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class selectDataByQuery extends StringRequest {
    private static final String partnerDataURL = "https://autotoni.hr/notes/selectDataByQuery.php";
    private Map<String, String> params;

    public selectDataByQuery(String query, Response.Listener<String> listener){
        super(Request.Method.POST, partnerDataURL, listener, null);
        params= new HashMap<>();
        params.put("query", query);

    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
