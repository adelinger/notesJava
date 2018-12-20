package com.delinger.antun.notesjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

    private String name;
    private String lastName;

    private userLocalStorage userLocalStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_activity);

        usernameTV    = findViewById(R.id.userNameTextView);
        passwordTV    = findViewById(R.id.passwordTextView);
        loginButton   = findViewById(R.id.loginButton);
        zapamtiMeCB   = findViewById(R.id.zapamtiMeCheckBox);

        userLocalStorage = new userLocalStorage(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 username  = usernameTV.getText().toString().trim();
                 password  = usernameTV.getText().toString().trim();
                 zapamtiMe = false;

                 if (zapamtiMeCB.isChecked()) zapamtiMe = true;

                 startProgressDialog();
                 authUser();

            }
        });
    }

    private void logIn() {
        userLocalStorage.storeUserData(username, password, zapamtiMe);
        Intent intent = new Intent(login_activity.this, MainActivity.class);
        startActivity(intent);

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
              progressDialog.dismiss();

                try {
                    userAuthenticated = true;
                    JSONObject jsonresponse = new JSONObject(response);
                    Toast.makeText(login_activity.this, jsonresponse.toString(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                    userAuthenticated = false;
                }

                if(userAuthenticated)logIn();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };

        login_request loginRequest = new login_request(username, password, responselistener, errorListener);
        //    loginRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(login_activity.this);
        queue.add(loginRequest);
    }
}
