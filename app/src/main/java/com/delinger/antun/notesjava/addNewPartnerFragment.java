package com.delinger.antun.notesjava;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class addNewPartnerFragment extends DialogFragment {
    View view;

    private TextView partnerNameTV;
    private TextView partnerLastnameTV;
    private TextView partnerEmailTV;
    private TextView partnerPhoneTV;

    private Button addPartnerButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_partner, null, false);

        partnerNameTV     = view.findViewById(R.id.addPartnerPN);
        partnerLastnameTV = view.findViewById(R.id.addPartnerLN);
        partnerEmailTV    = view.findViewById(R.id.addPartnerEM);
        partnerPhoneTV    = view.findViewById(R.id.addPartnerFN);
        addPartnerButton  = view.findViewById(R.id.addPartnerButton);

        addPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
