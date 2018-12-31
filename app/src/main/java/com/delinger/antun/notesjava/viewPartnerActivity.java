package com.delinger.antun.notesjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class viewPartnerActivity extends AppCompatActivity {

    private TextView debitSumTV;
    private TextView claimSumTV;
    private TextView balanceTV;
    private Button   addCarButton;
    private ListView carsListView;
    private Button   addNewClaimButton;
    private Toolbar  myToolbar;

    private List emptyList;
    private Intent intent;
    private Integer debitSum;
    private Integer claimSum;
    private Integer balance;

    private partner partner;
    private car car;
    private payment payment;

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
        calculatePayments();
        setListView();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewCarFragment();
            }
        });
    }

    private void calculatePayments() {

        for (int i=0; i<payment.debitList.size(); i++) {
            debitSum = debitSum + payment.debitList.get(i);
        }
        for (int i=0; i<payment.claimList.size(); i++){
            claimSum = claimSum + payment.claimList.get(i);
        }
        balance = claimSum - debitSum;

        debitSumTV.setText(" "+debitSum.toString()+ "kn");
        claimSumTV.setText(" "+claimSum.toString()+ "kn");
        balanceTV .setText(" "+balance.toString ()+ "kn");

    }

    private void getCarsData() {
        car = (com.delinger.antun.notesjava.Objects.car) getIntent().getSerializableExtra("car");

        try {
            for (int i=0; i <car.partnerIDList.size(); i++) {

                Integer carPartnerID = car.partnerIDList.get(i);
                Integer partnerID = partner.getId();

                if(!(carPartnerID.equals(partnerID))){

                    car.noteList.        remove(i);
                    car.partnerIDList.   remove(i);
                    car.dispatchDateList.remove(i);
                    car.receiptDateList. remove(i);
                    car.nameList.        remove(i);
                    car.workRequiredList.remove(i);
                    car.idList.          remove(i);
                    i=-1;
                }
            }

        } catch (Exception e) {
            Log.e("shit", e.getMessage());
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
                    i=-1;
                }
            }

        } catch (Exception e) {
            Log.e("shit", e.getMessage());
        }
    }

    private void setListView() {

        if(car.nameList.size() == 0){
            String[] emptyList =  {"Niste dodali niti jedno vozilo za ovog partnera"};
            carsListView.setAdapter(new ArrayAdapter<String>(viewPartnerActivity.this, android.R.layout.simple_list_item_1, emptyList));
        }
        else{
            carsListView.setAdapter(new ArrayAdapter<String>(viewPartnerActivity.this, android.R.layout.simple_list_item_1, car.nameList));
        }
    }

    private void goToAddNewCarFragment() {
        addNewCarFragment addNewCar = new addNewCarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("partnerID", partner.id);
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
        debitSum = 0;
        claimSum = 0;
        balance  = 0;

        partner  = new partner();
        intent   = new Intent();

        emptyList = new ArrayList<String>();

        car = new car();
        car.noteList         = new ArrayList<>();
        car.idList           = new ArrayList<>();
        car.partnerIDList    = new ArrayList<>();
        car.dispatchDateList = new ArrayList<>();
        car.receiptDateList  = new ArrayList<>();
        car.nameList         = new ArrayList<>();
        car.workRequiredList = new ArrayList<>();

        payment = new payment();
        payment.idList        = new ArrayList<>();
        payment.carIdList     = new ArrayList<>();
        payment.claimList     = new ArrayList<>();
        payment.dateList      = new ArrayList<>();
        payment.debitList     = new ArrayList<>();
        payment.partnerIdList = new ArrayList<>();
    }

}
