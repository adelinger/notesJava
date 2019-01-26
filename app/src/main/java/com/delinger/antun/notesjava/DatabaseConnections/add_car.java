package com.delinger.antun.notesjava.DatabaseConnections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class add_car extends StringRequest {
    private static final String URL = "https://autotoni.hr/notes/add_car.php";
    private Map<String, String> params;

    public add_car(String carName, String requiredWork, String receivedDate, String dispatchDate, Integer partnerID, Double carCost, Response.Listener<String> listener){
        //super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        super(Request.Method.POST, URL, listener, null);
        params= new HashMap<>();
        params.put("carname", carName+"");
        params.put("requiredWork", requiredWork);
        params.put("receivedDate", receivedDate+"");
        params.put("dispatchDate", dispatchDate);
        params.put("partnerID", partnerID.toString());
        params.put("carCost", carCost.toString());
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
