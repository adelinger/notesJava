package com.delinger.antun.notesjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.delinger.antun.notesjava.CustomListViewAdapters.viewClaimsAdapter;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

import java.text.DecimalFormat;

public class claimsActivity extends AppCompatActivity {
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
        toolbar          = findViewById(R.id.toolbar);
        sumTV            = findViewById(R.id.claimActivitySum);

        instantiateObjects();
        removeDebtsFromPayments();
        fillListView();
        getSum();
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
            Log.e("shit", e.getMessage());
        }
    }

    private void fillListView() {
        viewClaimsAdapter adapter = new viewClaimsAdapter(claimsActivity.this, payment.userIdList, payment.carIdList, car, payment.dateList, payment.claimList, payment.idList);
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

        toolbar.setTitle("Pregled uplata partnera "+partner.getFirstname() + " " + partner.getLastName());
        sum = 0.00;
    }

    //TODO finish listview adapter (car names) tofix
}
