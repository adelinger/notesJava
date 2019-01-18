package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.CustomListViewAdapters.viewClaimsAdapter;
import com.delinger.antun.notesjava.DatabaseConnections.editDataByQuery;
import com.delinger.antun.notesjava.DatabaseConnections.selectDataByQuery;
import com.delinger.antun.notesjava.Fragments.calendarFragment;
import com.delinger.antun.notesjava.Fragments.updatePaymentFragment;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class claimsActivity extends AppCompatActivity implements calendarFragment.Datum, updatePaymentFragment.OnUpdateComplete{
    private TextView partnerTV;
    private TextView voziloTV;
    private TextView datumTV;
    private TextView iznosTV;
    private TextView idTV;
    private TextView sumTV;
    private Button   addNewClaim;
    private ListView paymentsListView;

    private Toolbar toolbar;
    private Double  sum;

    private updatePaymentFragment updatePayment;
    private ProgressDialogWait progressDialogWait;
    private payment payment;
    private payment newPayment;
    private payment getPayment;
    private partner partner;
    private user user;
    private car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);

        paymentsListView = findViewById(R.id.uplateListView);
        addNewClaim      = findViewById(R.id.addNewClaimNewButton);
        toolbar          = findViewById(R.id.toolbar);
        sumTV            = findViewById(R.id.claimActivitySum);

        instantiateObjects();
        resetListView();
        getSum();

        addNewClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("car", car);
                bundle.putSerializable("user", user);
                bundle.putSerializable("partner", partner);
                bundle.putSerializable("payment", payment);
                bundle.putInt("partnerID", partner.id);
                updatePayment.setArguments(bundle);

                updatePayment.show(getFragmentManager().beginTransaction(), "insert");
            }
        });

      paymentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              performOnItemLongClickAction(i);
              return true;
          }
      });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("payment", payment);
        setResult(2, intent);
        finish();
        super.onBackPressed();
    }

    private void performOnItemLongClickAction(final Integer i) {
        final Integer position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(claimsActivity.this);
        builder.setPositiveButton("Izmijeni unos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("car", car);
                bundle.putSerializable("user", user);
                bundle.putSerializable("partner", partner);
                bundle.putSerializable("payment", payment);
                bundle.putInt         ("carPosition", getCarPosition(payment.carIdList.get(i)));
                bundle.putInt         ("id", payment.idList.get(i));
                bundle.putDouble      ("claim",payment.claimList.get(i));
                bundle.putInt         ("partnerID", payment.partnerIdList.get(i));
                bundle.putString      ("date", getDatum(payment.dateList.get(i)));
                updatePayment.setArguments(bundle);

                updatePayment.show(getFragmentManager().beginTransaction(), "update");
            }

        });
        builder.setNeutralButton("Obriši unos", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AlertDialog.Builder builder = new AlertDialog.Builder(claimsActivity.this);
                builder.setMessage("Jeste li sigurni da želite obrisati ovu uplatu?");
                builder.setPositiveButton("Obriši", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface builder, int which) {
                        deleteSelectedItem(position);
                    }
                });
                builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create();
                builder.setCancelable(false);
                builder.show();
            }
        });
        builder.create();
        builder.setCancelable(true);
        builder.show();
    }

    private void deleteSelectedItem(Integer position){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int save_result = 0;
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for(int i=0;i<jsonresponse.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        Jasonobject = jsonresponse.getJSONObject(i);
                        save_result =  Jasonobject.getInt("rezultat");

                        if(save_result == 0) throwNotSuccessfulMessage("Neuspješno. Pokušajte ponovno");
                        if(save_result == 1) getPaymentData();
                    }

                } catch (JSONException e) {
                    getPaymentData();
                }
                progressDialogWait.dismis();
            }
        };
        progressDialogWait.start();
        String query = "DELETE FROM payments WHERE id = "+newPayment.idList.get(position)+" ";
        editDataByQuery editData = new editDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(claimsActivity.this);
        queue.add(editData);
    }
    private void getPaymentData(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialogWait.dismis();

                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);
                        getPayment.idList.add(i,        Jsonobject.getInt("id"));
                        getPayment.debitList.add(i,     Jsonobject.getDouble("debit"));
                        getPayment.claimList.add(i,     Jsonobject.getDouble("claim"));
                        getPayment.dateList.add(i,      Jsonobject.getString("date"));
                        getPayment.partnerIdList.add(i, Jsonobject.getInt("partnerID"));
                        getPayment.carIdList.add(i,     Jsonobject.getInt("carID"));
                        getPayment.userIdList.add(i,    Jsonobject.getInt("userID"));

                    }
                } catch (JSONException e) {
                    Log.e("shit1", e.getMessage());
                }
                closeDialog("Uspješno obrisano!");
            }
        };

        String query = "SELECT * FROM payments WHERE  partnerID = "+partner.id.toString()+" ";
        selectDataByQuery get_transactions = new selectDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(claimsActivity.this);
        queue.add(get_transactions);
    }

    private void closeDialog(String message) {
        progressDialogWait.dismis();
        AlertDialog.Builder builder = new AlertDialog.Builder(claimsActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                payment = getPayment;
                instantiatePaymentObject();
                resetListView();
                sum = 0.0;
                getSum();
                builder.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }
    private void throwNotSuccessfulMessage(String notSuccessfullMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(claimsActivity.this);
        builder.setMessage(notSuccessfullMessage);
        builder.setPositiveButton("Pokušaj ponovno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                return;
            }

        });
        builder.setNegativeButton("Izlaz", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private Integer getCarPosition(Integer carID) {
        Integer position = 0;
        for (int i = 0; i<car.idList.size(); i++){
            if(carID.equals(car.idList.get(i)))position = i;
        }
        return position;
    }

    private void getSum() {
        for (int i=0; i<payment.claimList.size(); i++) {
            sum = sum + payment.claimList.get(i);
        }

        DecimalFormat df = new DecimalFormat("#0.00");
        sumTV.setText(df.format(sum)+ " €");
    }

    private void removeDebtsFromPayments() {
        try {
            for (int i=0; i<payment.idList.size(); i++){
                if (payment.claimList.get(i).equals(0.00) ){
                    payment.idList       .remove(i);
                    payment.carIdList    .remove(i);
                    payment.partnerIdList.remove(i);
                    payment.dateList     .remove(i);
                    payment.claimList    .remove(i);
                    payment.debitList    .remove(i);
                    payment.userIdList   .remove(i);
                    i=-1;
                }
            }

        } catch (Exception e) {
            Log.e("shit2", e.getMessage());
        }
    }


    private void instantiatePaymentObject() {
        newPayment = new payment();
        newPayment.idList        = new ArrayList<>();
        newPayment.carIdList     = new ArrayList<>();
        newPayment.claimList     = new ArrayList<>();
        newPayment.dateList      = new ArrayList<>();
        newPayment.debitList     = new ArrayList<>();
        newPayment.partnerIdList = new ArrayList<>();
        newPayment.userIdList    = new ArrayList<>();

        getPayment = new payment();
        getPayment.idList        = new ArrayList<>();
        getPayment.carIdList     = new ArrayList<>();
        getPayment.claimList     = new ArrayList<>();
        getPayment.dateList      = new ArrayList<>();
        getPayment.debitList     = new ArrayList<>();
        getPayment.partnerIdList = new ArrayList<>();
        getPayment.userIdList    = new ArrayList<>();
    }

    private void instantiateObjects() {
        payment = new payment();
        partner = new partner();
        user    = new user();
        payment = (com.delinger.antun.notesjava.Objects.payment) getIntent().getSerializableExtra("payment");
        partner = (com.delinger.antun.notesjava.Objects.partner) getIntent().getSerializableExtra("partner");
        user    = (com.delinger.antun.notesjava.Objects.user)    getIntent().getSerializableExtra("user");
        car     = (com.delinger.antun.notesjava.Objects.car)     getIntent().getSerializableExtra("car");

        toolbar.setTitle("Pregled uplata partnera "+partner.getFirstname() + " " + partner.getLastName());
        sum     = 0.00;
        partner.setId(getIntent().getIntExtra("partnerID",0));

        updatePayment      = new updatePaymentFragment();
        progressDialogWait = new ProgressDialogWait(claimsActivity.this);
        instantiatePaymentObject();
    }

    private String getDatum(String datum) {
        String returnDate = "";
        try {
            datum = datum.replaceAll("-", " ");

            String year   = datum.substring(0, datum.indexOf(" ")).trim();
            String month = datum.substring(4,7).trim();
            String day  = datum.substring(8,11).trim();

            returnDate = day + "-" + month + "-" + year;

        }
        catch(Exception e) {
            Log.e("exception", e.getMessage());
        }
        return returnDate;
    }

    @Override
    public void datePicked(String datum, String dateFromCalendar) {
        Bundle args = new Bundle();
        args.putString("newDate", datum);
        args.putString("dateFromCalendar", dateFromCalendar);
        updatePayment.Args(args);
    }

    @Override
    public void updateComplete(com.delinger.antun.notesjava.Objects.payment payment) {
        this.payment    = payment;
        resetListView();
    }

    private void resetListView() {
        try {
            for(int i=0; i<payment.idList.size(); i++){
                if(payment.debitList.get(i).equals(0.00)){
                    newPayment.idList       .add(payment.idList       .get(i));
                    newPayment.carIdList    .add(payment.carIdList    .get(i));
                    newPayment.partnerIdList.add(payment.partnerIdList.get(i));
                    newPayment.dateList     .add(payment.dateList     .get(i));
                    newPayment.claimList    .add(payment.claimList    .get(i));
                    newPayment.debitList    .add(payment.debitList    .get(i));
                    newPayment.userIdList   .add(payment.userIdList   .get(i));
                }
            }
        } catch(Exception e){
            Log.e("resetlistview", e.getMessage());
        }

        if(newPayment.claimList.size() == 0){
            String[] emptyList =  {"Ovaj partner nema niti jednu uplatu"};
            paymentsListView.setAdapter(new ArrayAdapter<String>(claimsActivity.this, android.R.layout.simple_list_item_1, emptyList));
        }else{
            paymentsListView.setAdapter(null);
            viewClaimsAdapter newAdapter = new viewClaimsAdapter(claimsActivity.this, newPayment.userIdList, newPayment.carIdList, car, newPayment.dateList, newPayment.claimList, newPayment.idList);
            paymentsListView.setAdapter(newAdapter);
        }


    }

}
