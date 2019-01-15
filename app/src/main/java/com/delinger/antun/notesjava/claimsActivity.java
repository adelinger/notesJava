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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.delinger.antun.notesjava.CustomListViewAdapters.viewClaimsAdapter;
import com.delinger.antun.notesjava.Fragments.calendarFragment;
import com.delinger.antun.notesjava.Fragments.updatePaymentFragment;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

import java.text.DecimalFormat;

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
    private payment payment;
    private payment newPayment;
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

      paymentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              performOnItemLongClickAction(i);
              return true;
          }
      });
    }

    private void performOnItemLongClickAction(final Integer i) {
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

            }
        });
        builder.create();
        builder.setCancelable(true);
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

        updatePayment = new updatePaymentFragment();
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
    public void datePicked(String datum) {
        Bundle args = new Bundle();
        args.putString("newDate", datum);
        updatePayment.Args(args);

    }

    @Override
    public void updateComplete(com.delinger.antun.notesjava.Objects.payment payment) {
        this.payment    = payment;
        this.newPayment = payment;
        resetListView(payment);
    }

    private void resetListView(payment payment) {
        for (int i = 0; i<payment.idList.size(); i++){
            Log.e("hm",payment.getPartnerID().toString());
            if(payment.claimList.get(i).equals(0.0) || payment.claimList.get(i).equals(0.00) || !(payment.partnerIdList.get(i).equals(payment.partnerIdList.get(i)))) {
                payment.partnerIdList.remove(i);
                payment.claimList.remove(i);
                payment.idList.remove(i);
                payment.carIdList.remove(i);
                payment.dateList.remove(i);
                payment.debitList.remove(i);
                payment.userIdList.remove(i);
            }
        }

        paymentsListView.setAdapter(null);
        viewClaimsAdapter adapter = new viewClaimsAdapter(claimsActivity.this, payment.userIdList, payment.carIdList, car, payment.dateList, payment.claimList, payment.idList);
        paymentsListView.setAdapter(adapter);
    }
}
