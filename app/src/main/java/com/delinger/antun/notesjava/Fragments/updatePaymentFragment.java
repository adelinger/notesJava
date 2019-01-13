package com.delinger.antun.notesjava.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

import com.delinger.antun.notesjava.R;
import com.delinger.antun.notesjava.claimsActivity;

import java.text.DecimalFormat;

public class updatePaymentFragment extends DialogFragment {
    private View     view;
    private EditText dateET;
    private EditText claimET;
    private Spinner  carSpinner;
    private Button   saveButton;

    private payment payment;
    private car car;
    private calendarFragment calendar;

    private Integer id;
    private Integer position;
    private Integer carPosition;
    private String datum;

    @Override
    public void onResume() {
        Log.e("resume", "resume");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e("start", "start");
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_payment_fragment_layout, null, false);

        dateET         = view.findViewById(R.id.dateET);
        claimET        = view.findViewById(R.id.claimET);
        carSpinner     = view.findViewById(R.id.voziloSpinner);
        saveButton     = view.findViewById(R.id.paymentsButton);

        instantiateObjects();
        loadSpinner();

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

               Bundle bundle = new Bundle();
               bundle.putString("previousDate", dateET.getText().toString());
               calendar.setArguments(bundle);
               calendar.show(getFragmentManager().beginTransaction(), "updatePaymentFragment");
            }
        });

        return view;
    }

    private void loadSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDialog().getContext(), android.R.layout.simple_list_item_1, car.nameList);
        carSpinner.setAdapter(adapter);
        carSpinner.setSelection(carPosition);
    }

    private void instantiateObjects() {
        payment payment = new payment();
        car = (com.delinger.antun.notesjava.Objects.car) getArguments().getSerializable("car");

        payment.setClaim(getArguments   ().getDouble("claim"));
        payment.setId(getArguments      ().getInt("id"));
        payment.setDate(getArguments    ().getString("date"));
        position    = getArguments      ().getInt("position");
        carPosition = getArguments      ().getInt("carPosition");

        calendar = new calendarFragment();

        DecimalFormat df = new DecimalFormat("#0.00");

        claimET.setText(df.format(payment.getClaim()));
        dateET.setText(payment.getDate());
    }

    public void Args(Bundle args) {
        String newDate = args.getString("newDate");
        dateET.setText(newDate);
    }


}
