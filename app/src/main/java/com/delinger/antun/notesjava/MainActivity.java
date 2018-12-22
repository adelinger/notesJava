package com.delinger.antun.notesjava;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    ListView partnersListView;
    FloatingActionButton addButton;
    Toolbar myToolbar;
    Toolbar myToolbar2;
    SharedPreferences userLocalDatabase;
    userLocalStorage userLocalStorage;
    partner partner;
    user user;
    Intent intent;
    ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStorage = new userLocalStorage(this);
        myToolbar        = findViewById(R.id.my_toolbar);
        myToolbar2       = findViewById(R.id.my_toolbar2);
        partnersListView = findViewById(R.id.listOfPartners);
        addButton        = findViewById(R.id.fab);

        setSupportActionBar(myToolbar);

        instantiateObject();
        getUserData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPartnerFragment addNewPartnerFragment = new addNewPartnerFragment();
                addNewPartnerFragment.show(getFragmentManager().beginTransaction(), "addNewPartnerFragment");
            }
        });
    }

    private void setPartnersList() {

        customListPartnersAdapter adapter = new customListPartnersAdapter(MainActivity.this, partner.firstnameList, partner.lastnameList, partner.idList);
        partnersListView.setAdapter(adapter);
    }

    private void getUserData() {
        SharedPreferences prefs = this.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        user.setFirstname(prefs.getString("firstname",""));
        user.setLastname(prefs.getString ("lastname", ""));
        user.setUsername(prefs.getString ("username",""));
        user.setPassword(prefs.getString ("password", ""));

        myToolbar2.setTitle("Korisnik: "+ user.firstname + " "+user.lastname);
    }

    private void instantiateObject() {
        intent  = getIntent();
        partner = new partner();
        partner = (com.delinger.antun.notesjava.partner) intent.getSerializableExtra("partner");
        try {
            if (partner.idList.size() == 0){ getPartnerData(); getUserData();}
            else setPartnersList();
        }
        catch (Exception e) {
            Log.e("shit", e.getMessage());
        }

        user = new user();
    }

    private void startProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Prijava...");
        progressDialog.setMessage("Pričekajte...");
        progressDialog.show();
    }

    public void getPartnerData() {
        startProgressDialog();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonresponse = new JSONArray(response);

                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        partner.firstnameList.add(i,Jsonobject.getString("firstname"));
                        partner.lastnameList. add(i,Jsonobject.getString("lastname").trim());
                        partner.phoneList.    add(i,Jsonobject.getString("phone"));
                        partner.emailList.    add(i,Jsonobject.getString("email"));
                        partner.idList.       add(i,Jsonobject.getInt("id"));
                    }
                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
                progressDialog.dismiss();
                setPartnersList();
            }

        };

        partnerDataClass partnerDataClass = new partnerDataClass(listener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(partnerDataClass);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();

        if (id == R.id.logOut) {
            Intent intent = new Intent(MainActivity.this, login_activity.class);
            userLocalStorage.setUserLoggedIn(false);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}

// TODO dodati floating action bar add new partner
