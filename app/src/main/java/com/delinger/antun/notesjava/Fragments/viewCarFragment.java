package com.delinger.antun.notesjava.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.delinger.antun.notesjava.Objects.car;

import com.delinger.antun.notesjava.R;

import org.w3c.dom.Text;

public class viewCarFragment extends DialogFragment {
    private View view;
    private TextView carNameTxtr;
    private TextView receiptDateTxt;
    private TextView dispatchDateTxt;
    private TextView typeOfWorTxt;
    private TextView carPriceTxt;
    private Button   exitButton;
    private Button   markAsDispatchedB;

    private car car;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_car_fragment_layout, null, false);

        carNameTxtr       = view.findViewById(R.id.carNameVCF);
        receiptDateTxt    = view.findViewById(R.id.dateReceiptVCF);
        dispatchDateTxt   = view.findViewById(R.id.dateReceiptVCF);
        typeOfWorTxt      = view.findViewById(R.id.typeOfWorkVCF);
        carPriceTxt       = view.findViewById(R.id.priceofcarVCF);
        exitButton        = view.findViewById(R.id.exitVCF);
        markAsDispatchedB = view.findViewById(R.id.markAsDispatchedVCF);

        getCarData();

        return view;
    }

    private void getCarData() {
        car = new car();

        car.setName(getArguments        ().getString("carName"));
        car.setId(getArguments          ().getInt("carID"));
        car.setCost(getArguments        ().getDouble("carCost"));
        car.setReceiptDate(getArguments ().getString("carReceiptDate"));
        car.setDispatchDate(getArguments().getString("carDispatchDate"));
        car.setNote(getArguments        ().getString("carNote"));
        car.setId(getArguments          ().getInt("carID"));

        carNameTxtr.setText    (car.getName());
        receiptDateTxt.setText ("Datum zaprimanja: " + car.getReceiptDate());
        dispatchDateTxt.setText("Datum predaje: "    +car.getDispatchDate());
        typeOfWorTxt.setText   ("Tip posla: "        +car.getNote());
        carPriceTxt.setText    ("Cijena popravka: "  +car.getCost().toString());
    }
}
