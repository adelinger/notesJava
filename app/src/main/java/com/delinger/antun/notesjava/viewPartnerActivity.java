package com.delinger.antun.notesjava;

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

import java.util.List;

public class viewPartnerActivity extends AppCompatActivity {

    private TextView debitSum;
    private TextView claimSum;
    private TextView balance;
    private Button   addCarButton;
    private ListView carsListView;
    private Button   addNewClaimButton;

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

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewCarFragment();
            }
        });
    }

    private void goToAddNewCarFragment() {
        addNewCarFragment addNewCar = new addNewCarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("partnerID", partner.id);
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
        car     = new car();
    }

}
