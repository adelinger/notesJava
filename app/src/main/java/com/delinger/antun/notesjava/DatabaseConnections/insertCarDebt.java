package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class insertCarDebt extends StringRequest {
    private static final String URL = "https://autotoni.hr/notes/insert_car_price.php";
    private Map<String, String> params;

    public insertCarDebt(Integer carID, Double price, Integer partnerID, Integer userID, Response.Listener<String> listener){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, URL, listener, null);
        params= new HashMap<>();
        params.put("carID", carID.toString());
        params.put("partnerID", partnerID.toString());
        params.put("userID", userID.toString());
        params.put("price", price.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
