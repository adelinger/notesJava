package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class update_payment extends StringRequest {
    private static final String URL = "https://autotoni.hr/notes/update_payment.php";
    private Map<String, String> params;

    public update_payment(Double claim, String date, Integer partnerID, Integer carID, Integer userID, Integer id, Response.Listener<String> listener){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, URL, listener, null);
        params= new HashMap<>();
        params.put("claim", claim.toString());
        params.put("date", date+"");
        params.put("partnerID", partnerID.toString());
        params.put("carID", carID.toString());
        params.put("userID", userID.toString());
        params.put("id", id.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
