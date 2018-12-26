package com.delinger.antun.notesjava;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;

public class addNewCarFragment extends DialogFragment {
    private View view;

    private EditText carNameET;
    private EditText requiredWorkET;
    private Button   addCarButton;

    private partner partner;
    private car car;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_car_fragment_layout, null, false);

        carNameET      = view.findViewById(R.id.carName);
        requiredWorkET = view.findViewById(R.id.requiredWork);
        addCarButton   = view.findViewById(R.id.addCarTODBButton);

        instantiateObjects();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCarData();
                checkIfTextInserted();
            }
        });

        return view;
    }

    private void setCarData() {
        car = new car();
        car.setName(carNameET.getText().toString());
    }

    private void instantiateObjects() {
        partner    = new partner();
        partner.id = getArguments().getInt("partnerID", 0);
    }

    private void checkIfTextInserted() {

    }
}
