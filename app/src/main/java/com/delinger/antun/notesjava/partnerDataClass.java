package com.delinger.antun.notesjava;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class partnerDataClass extends StringRequest {
    private static final String partnerDataURL = "https://autotoni.hr/notes/partnerdata.php";
    private Map<String, String> params;

    public partnerDataClass(Response.Listener<String> listener, Response.ErrorListener er ){
        super(Request.Method.POST, partnerDataURL, listener, er);

    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }

}