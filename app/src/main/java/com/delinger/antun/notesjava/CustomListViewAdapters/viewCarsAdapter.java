package com.delinger.antun.notesjava.CustomListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.R;

import java.util.List;

public class viewCarsAdapter extends ArrayAdapter<String>  {
    private Context context;
    private payment payment;
    car car;

    private Double sum;
    private Double claimSum;

    public viewCarsAdapter(Context context, car car, payment payment){
        super(context, R.layout.view_cars_adapter_layout);

        this.context = context;
        this.car = car;
        this.payment = payment;

        sum      = 0.00;
        claimSum = 0.00;

        getClaimSum();
    }

    private void getClaimSum() {
        for(int i=0; i<payment.debitList.size(); i++){
            claimSum = claimSum + payment.claimList.get(i);
        }

    }

    @Override
    public int getCount() {
        return car.idList.size();
    }

    @Override
    public String getItem(int position) {
        return car.nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_cars_adapter_layout, null, false);

            TextView carname  = view.findViewById(R.id.carNameForLV);
            TextView carstate = view.findViewById(R.id.carStateForLV);
            TextView carId    = view.findViewById(R.id.carIdForLV);

            carname.setText(car.nameList.get(position));

            //set debts closed by specific claim
            Double debt     = 0.00;
            Double claim    = 0.00;
            Double balance  = 0.00;
            for(int i=0; i<payment.debitList.size(); i++){
                if(payment.carIdList.get(i).equals(car.idList.get(position)) ){
                    debt = debt + payment.debitList.get(i);
                    claim = claim + payment.claimList.get(i);
                }

            }
            balance  = claim - debt;
            sum = sum + balance;

            if(balance>=0){
                carstate.setText("Plaćeno");
                claimSum = claimSum - debt;
            }
            else{
                if (claimSum>0 && claimSum > debt) {
                    claimSum = claimSum - debt;
                    carstate.setText("Plaćeno");
                    Log.e("claimSum2",claimSum.toString());
                } else carstate.setText("Nije plaćeno");
            }
        }

        return view;

    }
}
