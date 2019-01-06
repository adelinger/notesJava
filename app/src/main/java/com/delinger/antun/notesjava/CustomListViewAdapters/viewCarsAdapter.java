package com.delinger.antun.notesjava.CustomListViewAdapters;

import android.content.Context;
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

    public viewCarsAdapter(Context context, car car, payment payment){
        super(context, R.layout.view_cars_adapter_layout);

        this.context = context;
        this.car = car;
        this.payment = payment;
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

            Double debt    = 0.00;
            Double claim   = 0.00;
            Double balance = 0.00;
            for(int i=0; i<payment.debitList.size(); i++){
                if(payment.carIdList.get(i).equals(car.idList.get(position)) ){
                    debt = debt + payment.debitList.get(i);
                    claim = claim + payment.claimList.get(i);
                }
            }
            balance = claim - debt;
            if(balance>=0)carstate.setText("Plaćeno");
            else carstate.setText("Nije plaćeno");
        }

        return view;

    }
}
