package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.CustomListViewAdapters.viewCarsAdapter;
import com.delinger.antun.notesjava.DatabaseConnections.editDataByQuery;
import com.delinger.antun.notesjava.Fragments.addNewCarFragment;
import com.delinger.antun.notesjava.Fragments.viewCarFragment;
import com.delinger.antun.notesjava.HelperClasses.DialogMessage;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class viewPartnerActivity extends AppCompatActivity implements addNewCarFragment.onCarAdded, viewCarFragment.OnUpdateCar{

    private TextView debitSumTV;
    private TextView claimSumTV;
    private TextView balanceTV;
    private Button   addCarButton;
    private ListView carsListView;
    private Button   addNewClaimButton;
    private Toolbar  myToolbar;

    private List emptyList;
    private Intent intent;
    private Double debitSum;
    private Double claimSum;
    private Double balance;
    private Double addedCarPrice;

    private partner partner;
    private car car;
    private car newCar;
    private payment payment;
    private user user;

    ProgressDialogWait progressDialogWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_partner);

        debitSumTV          = findViewById(R.id.debitSum);
        claimSumTV          = findViewById(R.id.claimSum);
        balanceTV           = findViewById(R.id.balance);
        addCarButton        = findViewById(R.id.addCarsButton);
        carsListView        = findViewById(R.id.carsListView);
        addNewClaimButton   = findViewById(R.id.addNewClaim);
        myToolbar           = findViewById(R.id.toolbar);

        instantiateObjects();
        getPartnerData();
        getCarsData();
        getPaymentsData();
        calculatePayments(0.00);
        setListView();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewCarFragment();
            }
        });
        addNewClaimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewPartnerActivity.this, claimsActivity.class);
                intent.putExtra("payment", payment);
                intent.putExtra("partner", partner);
                intent.putExtra("user", user);
                intent.putExtra("car",  car);
                intent.putExtra("partnerID", partner.id);
                startActivityForResult(intent,2);
            }
        });
        carsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                performItemClickAction(i);
            }
        });
        carsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                performOnItemLongClickAction(i);
                return true;
            }
        });
    }


    private void performItemClickAction(int position) {
        viewCarFragment viewCarFragment = new viewCarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("carName",         newCar.nameList.get(position));
        bundle.putString("carReceiptDate",  newCar.receiptDateList.get(position));
        bundle.putString("carDispatchDate", newCar.dispatchDateList.get(position));
        bundle.putInt   ("carID",           newCar.idList.get(position));
        bundle.putDouble("carCost",         newCar.costList.get(position));
        bundle.putString("carNote",         newCar.noteList.get(position));
        bundle.putInt   ("finished",        newCar.finishedList.get(position));
        bundle.putInt   ("partnerID",       partner.getId());
        bundle.putDouble("claimSum",        getClaimSumForThisCar(newCar.idList.get(position)));
        viewCarFragment.setArguments(bundle);
        viewCarFragment.show(getFragmentManager().beginTransaction(), "viewCar");
    }

    private void performOnItemLongClickAction(final Integer i) {
        final Integer position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(viewPartnerActivity.this);

        builder.setNeutralButton("Obriši unos", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AlertDialog.Builder builder = new AlertDialog.Builder(viewPartnerActivity.this);
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

    private void deleteSelectedItem(final Integer position) {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    int save_result = 0;
                    int carID = car.idList.get(position);
                    try {
                        JSONArray jsonresponse = new JSONArray(response);
                        for(int i=0;i<jsonresponse.length();i++)
                        {
                            JSONObject Jasonobject = null;
                            Jasonobject = jsonresponse.getJSONObject(i);
                            save_result =  Jasonobject.getInt("rezultat");

                            if(save_result == 0) DialogMessage.throwNotSuccessfulMessage("Neuspješno brisanje.", viewPartnerActivity.this);
                            if(save_result == 1) deletePayments(carID);
                        }

                    } catch (JSONException e) {
                       Log.e("deleteSelected", e.getMessage());
                    }
                }
            };
            progressDialogWait.start();
            String query = "DELETE FROM cars WHERE id = "+car.idList.get(position)+" ";
            editDataByQuery editData = new editDataByQuery(query, listener);
            RequestQueue queue = Volley.newRequestQueue(viewPartnerActivity.this);
            queue.add(editData);
    }

    private void deletePayments(final Integer carID) {
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

                        if(save_result == 0) DialogMessage.throwNotSuccessfulMessage("Neuspješno brisanje.", viewPartnerActivity.this);
                        if(save_result == 1) deleteFromObject(carID);
                    }

                } catch (JSONException e) {
                    Log.e("deleteSelected", e.getMessage());
                }
                progressDialogWait.dismis();
            }
        };
        progressDialogWait.start();
        String query = "DELETE FROM payments WHERE carID = "+carID+" ";
        editDataByQuery editData = new editDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(viewPartnerActivity.this);
        queue.add(editData);
    }

    private void deleteFromObject(Integer carID) {
        for (int i = 0; i <newCar.idList.size() ; i++) {
            if(newCar.idList.get(i).equals(carID)) {
                newCar.idList.remove(i);
                newCar.nameList.remove(i);
                newCar.finishedList.remove(i);
                newCar.costList.remove(i);
                newCar.partnerIDList.remove(i);
                newCar.costList.remove(i);
                newCar.receiptDateList.remove(i);
                newCar.dispatchDateList.remove(i);
                newCar.workRequiredList.remove(i);
                newCar.noteList.remove(i);
            }
        }
        resetListView();
        progressDialogWait.dismis();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == 2) {
                payment = (com.delinger.antun.notesjava.Objects.payment) data.getSerializableExtra("payment");
            }
        }
        recalculatePayments(0.00);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }

    private void recalculatePayments(Double price) {
        claimSum = 0.00;
        debitSum = 0.00;
        balance  = 0.00;
        calculatePayments(price);
    }

    private void calculatePayments(Double addedCarPrice) {
        for (int i=0; i<payment.debitList.size(); i++) {
            debitSum = debitSum + payment.debitList.get(i);
        }
        debitSum = debitSum + addedCarPrice;
        for (int i=0; i<payment.claimList.size(); i++){
            claimSum = claimSum + payment.claimList.get(i);
        }

        balance = claimSum - debitSum;

        DecimalFormat df = new DecimalFormat("#0.00");
        claimSum = Double.valueOf(df.format(claimSum));

        debitSumTV.setText(" "+df.format(debitSum)+  " €");
        claimSumTV.setText(" "+df.format(claimSum) + " €");
        balanceTV .setText(" "+df.format(balance ) + " €");
    }

    private void getCarsData() {
        car = (com.delinger.antun.notesjava.Objects.car) getIntent().getSerializableExtra("car");

        try {
            for (int i=0; i <car.partnerIDList.size(); i++) {

                Integer carPartnerID = car.partnerIDList.get(i);
                Integer partnerID = partner.getId();

                if(carPartnerID.equals(partnerID)){
                    newCar.noteList.        add(car.noteList.get(i));
                    newCar.partnerIDList.   add(car.partnerIDList.get(i));
                    newCar.dispatchDateList.add(car.dispatchDateList.get(i));
                    newCar.receiptDateList. add(car.receiptDateList.get(i));
                    newCar.nameList.        add(car.nameList.get(i));
                    newCar.workRequiredList.add(car.workRequiredList.get(i));
                    newCar.idList.          add(car.idList.get(i));
                    newCar.costList.        add(car.costList.get(i));
                    newCar.finishedList.    add(car.finishedList.get(i));
                }
            }

        } catch (Exception e) {
            Log.e("getcars", e.getMessage());
        }

    }

    private void getPaymentsData() {
        payment = (com.delinger.antun.notesjava.Objects.payment) getIntent().getSerializableExtra("payment");

        try {
            for (int i=0; i<payment.idList.size(); i++){
                if( !(payment.partnerIdList.get(i).equals(partner.id)) ){

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
            Log.e("getpaymentsdata", e.getMessage());
        }
    }

    private Double getClaimSumForThisCar (Integer carID) {
        Double claimSum = 0.00;
        for (int i = 0; i < payment.claimList.size(); i++) {
            if(payment.partnerIdList.get(i).equals(partner.getId()) && payment.carIdList.get(i).equals(carID)){
                claimSum = claimSum + payment.claimList.get(i);
            }
        }
        return claimSum;
    }

    private void setListView() {
        if(newCar.nameList.size() == 0){
            String[] emptyList =  {"Niste dodali niti jedno vozilo za ovog partnera"};
            carsListView.setAdapter(new ArrayAdapter<String>(viewPartnerActivity.this, android.R.layout.simple_list_item_1, emptyList));
        }
        else{
            viewCarsAdapter adapter = new viewCarsAdapter(viewPartnerActivity.this, newCar, payment);
            carsListView.setAdapter(adapter);
        }
    }

    private void goToAddNewCarFragment() {
        addNewCarFragment addNewCar = new addNewCarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("partnerID", partner.id);
        bundle.putInt("userID", user.id);
        addNewCar.setArguments(bundle);
        addNewCar.show(getFragmentManager().beginTransaction(), "addNewCar");
    }

    private void getPartnerData() {
        partner.setFirstname(getIntent().getStringExtra("firstname"));
        partner.setLastName(getIntent().getStringExtra("lastname"));
        partner.setEmail(getIntent().getStringExtra("email"));
        partner.setPhone(getIntent().getStringExtra("phone"));
        partner.setId(getIntent().getIntExtra("id",0));

        myToolbar.setTitle("Partner: "+partner.getFirstname() + " " + partner.getLastName());
    }

    private void instantiateObjects() {
        debitSum      = 0.00;
        claimSum      = 0.00;
        balance       = 0.00;
        addedCarPrice = 0.00;

        partner  = new partner();
        intent   = new Intent();

        emptyList          = new ArrayList<String>();
        progressDialogWait = new ProgressDialogWait(viewPartnerActivity.this);

        car = new car();

        newCar = new car();
        newCar.noteList         = new ArrayList<>();
        newCar.idList           = new ArrayList<>();
        newCar.partnerIDList    = new ArrayList<>();
        newCar.dispatchDateList = new ArrayList<>();
        newCar.receiptDateList  = new ArrayList<>();
        newCar.nameList         = new ArrayList<>();
        newCar.workRequiredList = new ArrayList<>();
        newCar.costList         = new ArrayList<>();
        newCar.finishedList     = new ArrayList<>();

        payment = new payment();
        payment.idList        = new ArrayList<>();
        payment.carIdList     = new ArrayList<>();
        payment.claimList     = new ArrayList<>();
        payment.dateList      = new ArrayList<>();
        payment.debitList     = new ArrayList<>();
        payment.partnerIdList = new ArrayList<>();
        payment.userIdList    = new ArrayList<>();

        user = new user();
        user = (com.delinger.antun.notesjava.Objects.user) getIntent().getSerializableExtra("user");
    }


    @Override
    public void onComplete(com.delinger.antun.notesjava.Objects.car car, Double price) {
        newCar = new car();
        newCar = car;

        addedCarPrice = addedCarPrice + price;
        recalculatePayments(addedCarPrice);
        resetListView();
    }

    private void resetListView() {
        carsListView.setAdapter(null);
        if(newCar.nameList.size() == 0){
            String[] emptyList =  {"Dogodila se greška."};
            carsListView.setAdapter(new ArrayAdapter<String>(viewPartnerActivity.this, android.R.layout.simple_list_item_1, emptyList));
        }
        else{
            viewCarsAdapter adapter = new viewCarsAdapter(viewPartnerActivity.this, newCar, payment);
            carsListView.setAdapter(adapter);
        }
    }

    @Override
    public void isCompleted(com.delinger.antun.notesjava.Objects.car newCar, Double addedCarPrice, payment payment) {
        this.newCar  = newCar;
        this.payment = payment;

        recalculatePayments(0.00);
        resetListView();
    }
}
