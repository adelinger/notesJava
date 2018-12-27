package com.delinger.antun.notesjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class viewPartnerActivity extends AppCompatActivity {

    private TextView debitSum;
    private TextView claimSum;
    private TextView balance;
    private Button   addCarButton;
    private ListView carsListView;
    private Button   addNewClaimButton;

    private List emptyList;
    private Intent intent;

    private partner partner;
    private car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_partner);

        debitSum          = findViewById(R.id.debitSum);
        claimSum          = findViewById(R.id.claimSum);
        balance           = findViewById(R.id.balance);
        addCarButton      = findViewById(R.id.addCarsButton);
        carsListView      = findViewById(R.id.carsListView);
        addNewClaimButton = findViewById(R.id.addNewClaim);

        instantiateObjects();
        getPartnerData();
        getCarsData();
        setListView();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewCarFragment();
            }
        });
    }

    private void getCarsData() {
        car = (com.delinger.antun.notesjava.Objects.car) getIntent().getSerializableExtra("car");
        try {
            for (int i=0; i < car.idList.size(); i++) {
                if(car.partnerIDList.get(i) != partner.getId()) {

                    car.noteList.        remove(i);
                    car.partnerIDList.   remove(i);
                    car.dispatchDateList.remove(i);
                    car.receiptDateList. remove(i);
                    car.nameList.        remove(i);
                    car.workRequiredList.remove(i);
                    car.idList.          remove(i);
                }
            }
            for (int i=0; i < car.idList.size(); i++) {
                Log.e("cars", car.nameList.get(i));
            }

        } catch (Exception e) {
            Log.e("shit", e.getMessage());
        }

//TODO završiti ovaj shit s car objectom tofix
    }

    private void setListView() {

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
    }

    private void instantiateObjects() {
        partner = new partner();
        intent  = new Intent();

        emptyList = new ArrayList<String>();

        car = new car();
        car.noteList         = new ArrayList<>();
        car.idList           = new ArrayList<>();
        car.partnerIDList    = new ArrayList<>();
        car.dispatchDateList = new ArrayList<>();
        car.receiptDateList  = new ArrayList<>();
        car.nameList         = new ArrayList<>();
        car.workRequiredList = new ArrayList<>();
    }

}
