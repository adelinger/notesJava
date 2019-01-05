package com.delinger.antun.notesjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.delinger.antun.notesjava.CustomListViewAdapters.pregledUplataAdapter;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

public class claimsActivity extends AppCompatActivity {
    private TextView partnerTV;
    private TextView voziloTV;
    private TextView datumTV;
    private TextView iznosTV;
    private TextView idTV;
    private Button   addNewClaim;
    private ListView paymentsListView;

    private payment payment;
    private partner partner;
    private user user;
    private car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);

        paymentsListView = findViewById(R.id.uplateListView);
        addNewClaim      = findViewById(R.id.addNewClaimNewButton);

        instantiateObjects();
        fillListView();

    }

    private void fillListView() {
        pregledUplataAdapter adapter = new pregledUplataAdapter(claimsActivity.this, payment.userIdList, payment.carIdList, car, payment.dateList, payment.claimList, payment.idList);
        paymentsListView.setAdapter(adapter);
    }

    private void instantiateObjects() {
        payment = new payment();
        partner = new partner();
        user    = new user();
        payment = (com.delinger.antun.notesjava.Objects.payment) getIntent().getSerializableExtra("payment");
        partner = (com.delinger.antun.notesjava.Objects.partner) getIntent().getSerializableExtra("partner");
        user    = (com.delinger.antun.notesjava.Objects.user)    getIntent().getSerializableExtra("user");
        car     = (com.delinger.antun.notesjava.Objects.car)     getIntent().getSerializableExtra("car");
    }

    //TODO finish listview adapter (car names) tofix
}
