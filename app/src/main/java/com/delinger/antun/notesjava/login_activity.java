package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class login_activity extends AppCompatActivity {
    private TextView usernameTV;
    private TextView passwordTV;
    private CheckBox zapamtiMeCB;
    private Button loginButton;

    private String username;
    private String password;
    private Boolean userAuthenticated;
    private Boolean zapamtiMe;
    ProgressDialog progressDialog;

    partner partner;
    user user;

    private userLocalStorage userLocalStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_activity);

        usernameTV    = findViewById(R.id.userNameTextView);
        passwordTV    = findViewById(R.id.passwordTextView);
        loginButton   = findViewById(R.id.loginButton);
        zapamtiMeCB   = findViewById(R.id.zapamtiMeCheckBox);

        instantiateObjects();

        if(userIsAlreadyLoggedIn()) {
            getPartnerData();
            goToMainAcitivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 username  = usernameTV.getText().toString().trim();
                 password  = passwordTV.getText().toString().trim();

                 startProgressDialog();
                 authUser();
            }
        });
        zapamtiMeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                zapamtiMe = b;
            }
        });
    }

    private Boolean userIsAlreadyLoggedIn() {
        SharedPreferences prefs = this.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        Boolean loggedIn = prefs.getBoolean("loggedIn", false);

        return loggedIn;
    }

    private void goToMainAcitivity() {
        Intent intent = new Intent(login_activity.this, MainActivity.class);
        intent.putExtra("partner", partner);
        intent.putExtra("user", user);
        this.finishAffinity();
        startActivity(intent);
    }

    private void logIn() {

        if(!userIsAlreadyLoggedIn()) userLocalStorage.storeUserData(username, password, user.getFirstname(), user.getLastname());
        if(zapamtiMe)userLocalStorage.setUserLoggedIn(true);
        goToMainAcitivity();

    }
    private void throwBadAuthDialog() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(login_activity.this);
        builder.setMessage("Neuspješna prijava");
        builder.setPositiveButton("Pokušaj ponovno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                builder.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void startProgressDialog(){
        progressDialog = new ProgressDialog(login_activity.this);
        progressDialog.setTitle("Prijava...");
        progressDialog.setMessage("Pričekajte...");
        progressDialog.show();
    }

    private void authUser () {
            Response.Listener<String> responselistener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    if(jsonresponse.length() > 0) userAuthenticated = true;

                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject Jasonobject = new JSONObject();
                        Jasonobject = jsonresponse.getJSONObject(i);
                        user.setFirstname(Jasonobject.getString("firstname"));
                        user.setLastname(Jasonobject.getString("lastname"));
                        user.setUsername(Jasonobject.getString("username"));
                        user.setPassword(Jasonobject.getString("password"));
                    }


                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                    userAuthenticated = false;
                }

                if(userAuthenticated) getPartnerData();
                if(!userAuthenticated)throwBadAuthDialog();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };

        login_request loginRequest = new login_request(username, password, responselistener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(login_activity.this);
        queue.add(loginRequest);
    }

    public void getPartnerData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonresponse = new JSONArray(response);

                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        partner.firstnameList.add(i,Jsonobject.getString("firstname"));
                        partner.lastnameList. add(i,Jsonobject.getString("lastname"));
                        partner.phoneList.    add(i,Jsonobject.getString("phone"));
                        partner.emailList.    add(i,Jsonobject.getString("email"));
                        partner.idList.       add(i,Jsonobject.getInt("id"));
                    }
                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }

                logIn();
            }
        };

        partnerDataClass partnerDataClass = new partnerDataClass(listener);
        RequestQueue queue = Volley.newRequestQueue(login_activity.this);
        queue.add(partnerDataClass);
    }

    private void instantiateObjects() {
        this.setTitle("Prijava u sustav");

        zapamtiMe             = false;

        progressDialog        = new ProgressDialog(login_activity.this);
        partner               = new partner();
        partner.firstnameList = new ArrayList<>();
        partner.lastnameList  = new ArrayList<>();
        partner.emailList     = new ArrayList<>();
        partner.phoneList     = new ArrayList<>();
        partner.idList        = new ArrayList<>();

        user = new user();

        userLocalStorage      = new userLocalStorage(this);
    }
}

