package com.delinger.antun.notesjava;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.delinger.antun.notesjava.DatabaseConnections.delete_partner;
import com.delinger.antun.notesjava.DatabaseConnections.insertPartner;
import com.delinger.antun.notesjava.DatabaseConnections.update_partner;
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
    partnerOptions partnerOptions;

    private user user;
    private partner partner;

    private Integer save_result;
    private String tag;
    private Boolean insert;
    private Boolean update;
    private Boolean delete;

    public interface partnerOptions {
        void complete(Boolean complete);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.partnerOptions = (addNewPartnerFragment.partnerOptions) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

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
        setButtonTitle();


        addPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPartnerData();

                if(!textIsEntered()) {
                    Toast.makeText(getDialog().getContext(), "Morate upisati ime i prezime partnera kako biste ga mogli spremiti.", Toast.LENGTH_LONG).show(); return;
                }
                if(!connection.isNetworkAvailable(getDialog().getContext())) throwNotSuccessfulMessage("Nema internet veze.");

                progressDialog.start();

                if(insert) insertPartner();
                if(update) updatePartner();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        if(delete) {
            getDialog().hide();
            showWarningMessageAndDeleteIfAccepted();
        }
        super.onResume();
    }

    private void showWarningMessageAndDeleteIfAccepted() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage("Jeste li sigurni da želite obrisati partnera: "+ partner.firstname + " " + partner.lastName);
        builder.setPositiveButton("Obriši", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                deletePartner();
            }

        });
        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void deletePartner() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                checkIfSuccessful(response, "Uspješno obrisano.", "Brisanje neuspješno.");
            }
        };

        delete_partner delete_partner = new delete_partner(partner.id, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(delete_partner);
    }

    private void checkIfSuccessful(String response, String succesMessage, String noSuccesMessage) {
        try {
            JSONArray jsonresponse = new JSONArray(response);
            for(int i=0;i<jsonresponse.length();i++)
            {
                JSONObject Jasonobject = null;
                Jasonobject = jsonresponse.getJSONObject(i);
                save_result =  Jasonobject.getInt("rezultat");

                if(save_result == 0)throwNotSuccessfulMessage(noSuccesMessage);
                if(save_result == 1) closeDialog(succesMessage);
            }

        } catch (JSONException e) {
            Log.e("shit", e.getMessage());
        }
    }

    private void updatePartner() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
               checkIfSuccessful(response, "Uspješna izmjena.", "Neuspješna izmjena.");
            }
        };


        update_partner updatePartner = new update_partner(partner.getFirstname(), partner.getLastName(), partner.getEmail(), partner.getPhone(), partner.getId(), listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(updatePartner);
    }

    private void getPartnerDataFromBundle() {
        partner.firstname = getArguments().getString("firstname");
        partner.lastName  = getArguments().getString("lastname");
        partner.email     = getArguments().getString("email");
        partner.phone     = getArguments().getString("phone");
        partner.id        = getArguments().getInt("id");

        partnerNameTV    .setText(partner.firstname);
        partnerLastnameTV.setText(partner.lastName);
        partnerEmailTV   .setText(partner.email);
        partnerPhoneTV   .setText(partner.phone);
    }

    private void setButtonTitle() {
        if(tag == "addNewPartnerFragment") {
            addPartnerButton.setText("Dodaj partnera");
            insert = true;
            update = false;
            delete = false;
        }
       if(tag == "update") {
            addPartnerButton.setText("Izmijeni partnera");
            update = true;
            insert = false;
            delete = false;
            getPartnerDataFromBundle();
        }
        if(tag == "delete") {
            delete = true;
            update = false;
            insert = false;
            getPartnerDataFromBundle();
        }
    }

    private Boolean textIsEntered() {
        if (partner.firstname.isEmpty()) {
            return false;
        }
        if(partner.lastName.isEmpty()){
           return false;
    }
        else return true;
    }

    private void insertPartner() {
        progressDialog.start();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
               checkIfSuccessful(response, "Partner uspješno dodan.", "Neuspješno dodavanje.");
            }
        };
        insertPartner insertPartner = new insertPartner(partner.getFirstname(), partner.getLastName(), partner.getEmail(), partner.getPhone(), listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(insertPartner);
    }

    private void closeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                partnerOptions.complete(true);
                builder.dismiss();
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void throwNotSuccessfulMessage(String noSuccessMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(noSuccessMessage);
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

        tag = this.getTag();
    }


}
