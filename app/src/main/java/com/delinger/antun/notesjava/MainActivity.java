package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.DatabaseConnections.get_cars;
import com.delinger.antun.notesjava.DatabaseConnections.get_transactions;
import com.delinger.antun.notesjava.DatabaseConnections.partnerDataClass;
import com.delinger.antun.notesjava.HelperClasses.connection;
import com.delinger.antun.notesjava.Fragments.addNewPartnerFragment;
import com.delinger.antun.notesjava.HelperClasses.customListPartnersAdapter;
import com.delinger.antun.notesjava.HelperClasses.userLocalStorage;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements addNewPartnerFragment.partnerOptions {

    ListView partnersListView;
    FloatingActionButton addButton;
    Toolbar myToolbar;
    Toolbar myToolbar2;
    SharedPreferences userLocalDatabase;
    userLocalStorage userLocalStorage;

    partner partner;
    user user;
    car car;
    payment payment;

    Intent intent;
    ProgressDialog progressDialog;
    connection connection;
    customListPartnersAdapter adapter;

    private Boolean refresh;

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
        instantiateCarObject();
        instantiatePaymentObject();
        getPartnerData();
        getUserData();
        getCarData();
        getPaymentData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPartnerFragment addNewPartnerFragment = new addNewPartnerFragment();
                addNewPartnerFragment.show(getFragmentManager().beginTransaction(), "addNewPartnerFragment");
            }
        });
        partnersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                performOnLongPressAction(i);
                return true;
            }
        });
        partnersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                performItemClickAction(i);
            }
        });
    }

    private void getCarData() {
        instantiateCarObject();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        car.nameList.        add(i,Jsonobject.getString("name"));
                        car.workRequiredList.add(i, Jsonobject.getString("requiredWork"));
                        car.receiptDateList. add(i, Jsonobject.getString("receiptdate"));
                        car.dispatchDateList.add(i, Jsonobject.getString("dispatchdate"));
                        car.idList.          add(i, Jsonobject.getInt("id"));
                        car.partnerIDList.   add(i, Jsonobject.getInt("partnerID"));
                        car.noteList.        add(i, Jsonobject.getString("note"));

                    }
                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        get_cars add_car = new get_cars(listener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(add_car);
    }

    private void getPaymentData(){
        instantiatePaymentObject();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonresponse = new JSONArray(response);

                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);
                        payment.idList.add(i,        Jsonobject.getInt("id"));
                        payment.debitList.add(i,     Jsonobject.getDouble("debit"));
                        payment.claimList.add(i,     Jsonobject.getDouble("claim"));
                        payment.dateList.add(i,      Jsonobject.getString("date"));
                        payment.partnerIdList.add(i, Jsonobject.getInt("partnerID"));
                        payment.carIdList.add(i,     Jsonobject.getInt("carID"));
                        payment.userIdList.add(i,    Jsonobject.getInt("userID"));
                    }
                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        get_transactions get_transactions = new get_transactions(listener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(get_transactions);
    }

    private void performItemClickAction(Integer i) {

        Intent intent = new Intent(MainActivity.this, viewPartnerActivity.class);

        intent.putExtra("user",      user);
        intent.putExtra("payment",   payment);
        intent.putExtra("partner",   partner);
        intent.putExtra("car",       car);
        intent.putExtra("firstname", partner.firstnameList.get(i));
        intent.putExtra("lastname",  partner.lastnameList.get(i));
        intent.putExtra("email",     partner.emailList.get(i));
        intent.putExtra("phone",     partner.phoneList.get(i));
        intent.putExtra("id",        partner.idList.get(i));

        startActivity(intent);
    }


    private void performOnLongPressAction(final Integer position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Izmijeni partnera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
              goToFragment(position, "update");
            }

        });
        builder.setNeutralButton("Obriši partnera", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToFragment(position, "delete");
            }
        });
        builder.create();
        builder.setCancelable(true);
        builder.show();
    }

    private void goToFragment(Integer position, String tag) {
        Bundle bundle = new Bundle();

        bundle.putString("firstname",partner.firstnameList.get(position));
        bundle.putString("lastname", partner.lastnameList .get(position));
        bundle.putString("email",    partner.emailList    .get(position));
        bundle.putString("phone",    partner.phoneList    .get(position));
        bundle.putInt   ("id",       partner.idList       .get(position));

        addNewPartnerFragment addNewPartnerFragment = new addNewPartnerFragment();
        addNewPartnerFragment.setArguments(bundle);
        addNewPartnerFragment.show(getFragmentManager().beginTransaction(), tag);
    }

    private void setPartnersList() {
        adapter = new customListPartnersAdapter(MainActivity.this, partner.firstnameList, partner.lastnameList, partner.idList);
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
        partner = (com.delinger.antun.notesjava.Objects.partner) intent.getSerializableExtra("partner");
        try {
            if (partner.idList.size() == 0){ getPartnerData(); getUserData();}
            else setPartnersList();
        }
        catch (Exception e) {
            Log.e("shit", e.getMessage());
        }

        user       = new user();
        connection = new connection();
        refresh    = false;

        progressDialog = new ProgressDialog(MainActivity.this);
    }

    private void instantiateCarObject(){
        car        = new car();

        car.noteList         = new ArrayList<>();
        car.idList           = new ArrayList<>();
        car.partnerIDList    = new ArrayList<>();
        car.dispatchDateList = new ArrayList<>();
        car.receiptDateList  = new ArrayList<>();
        car.nameList         = new ArrayList<>();
        car.workRequiredList = new ArrayList<>();
    }
   private void instantiatePaymentObject() {
       payment = new payment();
       payment.idList        = new ArrayList<>();
       payment.carIdList     = new ArrayList<>();
       payment.claimList     = new ArrayList<>();
       payment.dateList      = new ArrayList<>();
       payment.debitList     = new ArrayList<>();
       payment.partnerIdList = new ArrayList<>();
       payment.userIdList    = new ArrayList<>();

   }

    private void startProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Učitavanje...");
        progressDialog.setMessage("Pričekajte...");
        progressDialog.show();
    }

    public void getPartnerData() {
        if(!connection.isNetworkAvailable(MainActivity.this)) {
            logOut();
        }
        try {
            if (partner.idList.size() == 0 || refresh){
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
                        setPartnersList();
                        if(refresh){
                            getCarData();
                            getPaymentData();
                        }
                    }

                };

                partnerDataClass partnerDataClass = new partnerDataClass(listener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(partnerDataClass);
                }

            else setPartnersList();
        }
        catch (Exception e) {
            Log.e("shit", e.getMessage());
        }

    }

    private void logOut() {
        Intent intent = new Intent(MainActivity.this, login_activity.class);
        userLocalStorage.setUserLoggedIn(false);
        startActivity(intent);
        this.finish();
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
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void complete(Boolean complete) {
        if(complete) {
            partner.firstnameList.clear();
            partner.lastnameList. clear();
            partner.emailList.    clear();
            partner.phoneList.    clear();
            partner.idList.       clear();

            refresh = true;
            getPartnerData();
        }
    }
}
