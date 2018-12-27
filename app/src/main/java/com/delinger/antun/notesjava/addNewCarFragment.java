package com.delinger.antun.notesjava;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.DatabaseConnections.add_car;
import com.delinger.antun.notesjava.DatabaseConnections.insertPartner;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addNewCarFragment extends DialogFragment {
    private View view;

    private EditText carNameET;
    private EditText requiredWorkET;
    private Button   addCarButton;

    private Integer save_result;
    private String  currentDate;
    private List<String>emptyList;

    private partner partner;
    private car car;
    private ProgressDialogWait progressDialog;

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
                progressDialog.start();
                addCar();
            }
        });

        return view;
    }

    private void addCar() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for(int i=0;i<jsonresponse.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        Jasonobject = jsonresponse.getJSONObject(i);
                        save_result =  Jasonobject.getInt("rezultat");

                        if(save_result == 0) throwNotSuccessfulMessage("Neuspješno dodavanje vozila.");
                        if(save_result == 1) closeDialog("Vozilo uspješno spremljeno");
                    }

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        add_car addCar = new add_car(car.getName(), car.getWorkRequired(),car.getReceiptDate(), car.getDispatchDate(), partner.getId(), listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(addCar);
    }

    private void throwNotSuccessfulMessage(String noSuccessMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(noSuccessMessage);
        builder.setPositiveButton("Pokušaj ponovno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                return;
            }

        });
        builder.setNegativeButton("Izlaz", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void closeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                builder.dismiss();
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void setCarData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        car.setName(carNameET.getText().toString());
        car.setWorkRequired(requiredWorkET.getText().toString());
        car.setReceiptDate(currentDate);
        car.setDispatchDate("");
    }

    private void instantiateObjects() {
        car            = new car();
        progressDialog = new ProgressDialogWait(getDialog().getContext());
        partner        = new partner();
        partner.setId(getArguments().getInt("partnerID", 0));

        emptyList = new ArrayList<>();
    }

    private void checkIfTextInserted() {
        if(car.getName().isEmpty() || car.getWorkRequired().isEmpty()){
            Toast.makeText(getDialog().getContext(), "Morate ispuniti sva polja da biste mogli spremiti vozilo", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
