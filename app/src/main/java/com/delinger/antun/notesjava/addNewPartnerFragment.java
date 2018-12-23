package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.DatabaseConnections.insertPartner;
import com.delinger.antun.notesjava.HelperClasses.connection;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

public class addNewPartnerFragment extends DialogFragment {
    View view;

    private TextView partnerNameTV;
    private TextView partnerLastnameTV;
    private TextView partnerEmailTV;
    private TextView partnerPhoneTV;
    private Button addPartnerButton;

    ProgressDialogWait progressDialog;
    connection connection;

    private user user;
    private partner partner;

    private Integer save_result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_partner, null, false);

        partnerNameTV     = view.findViewById(R.id.addPartnerFN);
        partnerLastnameTV = view.findViewById(R.id.addPartnerLN);
        partnerEmailTV    = view.findViewById(R.id.addPartnerEM);
        partnerPhoneTV    = view.findViewById(R.id.addPartnerPN);
        addPartnerButton  = view.findViewById(R.id.addPartnerButton);

        instantiateObjects();

        addPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPartnerData();
                checkIfTextEntered();
                if( connection.isNetworkAvailable(getDialog().getContext())) insertPartner();
                if(!connection.isNetworkAvailable(getDialog().getContext())) throwNotSuccessfulMessage();
            }
        });

        return view;
    }

    private void checkIfTextEntered() {
        if (partnerNameTV.getText().toString() == "" || partnerLastnameTV.getText().toString() == "") {
            Toast.makeText(getDialog().getContext(), "Morate upisati ime i prezime partnera kako biste ga spremili.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void insertPartner() {
        progressDialog.start();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
                save_result = 0;
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for(int i=0;i<jsonresponse.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        Jasonobject = jsonresponse.getJSONObject(i);
                        save_result =  Jasonobject.getInt("rezultat");

                        if(save_result == 0)throwNotSuccessfulMessage();
                        if(save_result == 1) closeDialog();
                    }

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };
        insertPartner insertPartner = new insertPartner(partner.getFirstname(), partner.getLastName(), partner.getEmail(), partner.getPhone(), listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(insertPartner);
    }

    private void closeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage("Partner uspješno spremljen");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                builder.dismiss();
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void throwNotSuccessfulMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage("Partner nije spremljen. Došlo je do greške.");
        builder.setPositiveButton("Pokušaj ponovno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                return;
            }

        });
        builder.setNegativeButton("Izlaz", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void getPartnerData() {
        partner.setFirstname(partnerNameTV.getText().toString().trim());
        partner.setLastName (partnerLastnameTV.getText().toString().trim());
        partner.setEmail    (partnerEmailTV.getText().toString().trim());
        partner.setPhone    (partnerPhoneTV.getText().toString().trim());
    }

    private void instantiateObjects() {
        user    = new user();
        partner = new partner();

        progressDialog = new ProgressDialogWait(getDialog().getContext());
        connection = new connection();
    }


}
