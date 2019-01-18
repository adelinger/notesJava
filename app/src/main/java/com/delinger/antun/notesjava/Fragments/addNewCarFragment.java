package com.delinger.antun.notesjava.Fragments;

import android.app.Activity;
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
import com.delinger.antun.notesjava.DatabaseConnections.get_cars_for_user;
import com.delinger.antun.notesjava.DatabaseConnections.insertCarDebt;
import com.delinger.antun.notesjava.DatabaseConnections.insertPartner;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.R;

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
    private EditText priceET;
    private Button   addCarButton;

    private Integer save_result;
    private Double price;
    private String  currentDate;
    private Integer userID;
    private Integer carID;
    private List<String>emptyList;

    private partner partner;
    private car car;
    private car newCar;
    private ProgressDialogWait progressDialog;
    private onCarAdded listener;

    public interface onCarAdded {
        public void onComplete(car car, Double price);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (addNewCarFragment.onCarAdded) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_car_fragment_layout, null, false);

        carNameET      = view.findViewById(R.id.carName);
        requiredWorkET = view.findViewById(R.id.requiredWork);
        addCarButton   = view.findViewById(R.id.addCarTODBButton);
        priceET        = view.findViewById(R.id.priceET);

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
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for(int i=0;i<jsonresponse.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        Jasonobject = jsonresponse.getJSONObject(i);
                        save_result =  Jasonobject.getInt("rezultat");

                        if(save_result == 0) throwNotSuccessfulMessage("Neuspješno dodavanje vozila.");
                        if(save_result == 1) getNewCar();
                    }

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        add_car addCar = new add_car(car.getName(), car.getWorkRequired(),car.getReceiptDate(), car.getDispatchDate(), partner.getId(),listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(addCar);
    }

    private void insertDebt() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

        price = Double.parseDouble(priceET.getText().toString());
        insertCarDebt insert = new insertCarDebt(carID, price, partner.getId(), userID, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(insert);
    }

    private void getNewCar() {
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
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        newCar.nameList.        add(i, Jsonobject.getString("name"));
                        newCar.workRequiredList.add(i, Jsonobject.getString("requiredWork"));
                        newCar.receiptDateList. add(i, Jsonobject.getString("receiptdate"));
                        newCar.dispatchDateList.add(i, Jsonobject.getString("dispatchdate"));
                        newCar.idList.          add(i, Jsonobject.getInt("id"));
                        newCar.partnerIDList.   add(i, Jsonobject.getInt("partnerID"));
                        newCar.noteList.        add(i, Jsonobject.getString("note"));
                        newCar.costList.        add(i, Jasonobject.getDouble("cost"));

                        carID = Jsonobject.getInt("id");
                    }

                    if(newCar.idList.size() != 0) insertDebt();
                    else throwNotSuccessfulMessage("Dogodila se greška.");

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        get_cars_for_user getCar = new get_cars_for_user(listener, partner.getId());
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(getCar);
    }

    private void removeOtherUsersFromCar() {
        try {
            for (int i=0; i <newCar.partnerIDList.size(); i++) {

                Integer carPartnerID = newCar.partnerIDList.get(i);
                Integer partnerID    = partner.getId();

                if(!(carPartnerID.equals(partnerID)) ){
                    newCar.noteList.        remove(i);
                    newCar.partnerIDList.   remove(i);
                    newCar.dispatchDateList.remove(i);
                    newCar.receiptDateList. remove(i);
                    newCar.nameList.        remove(i);
                    newCar.workRequiredList.remove(i);
                    newCar.idList.          remove(i);
                    newCar.costList.        remove(i);
                    i = -1;
                }
            }

        } catch (Exception e) {
            Log.e("shit", e.getMessage());
        }
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
                removeOtherUsersFromCar();
                listener.onComplete(newCar, price);
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
        car                  = new car();
        newCar               = new car();
        newCar.noteList         = new ArrayList<>();
        newCar.idList           = new ArrayList<>();
        newCar.partnerIDList    = new ArrayList<>();
        newCar.dispatchDateList = new ArrayList<>();
        newCar.receiptDateList  = new ArrayList<>();
        newCar.nameList         = new ArrayList<>();
        newCar.workRequiredList = new ArrayList<>();
        newCar.costList         = new ArrayList<>();

        progressDialog = new ProgressDialogWait(getDialog().getContext());
        partner        = new partner();
        partner.setId(getArguments().getInt("partnerID", 0));
        userID = getArguments().getInt("userID",0);

        emptyList = new ArrayList<>();
    }

    private void checkIfTextInserted() {
        if(car.getName().isEmpty() || car.getWorkRequired().isEmpty()){
            Toast.makeText(getDialog().getContext(), "Morate ispuniti sva polja da biste mogli spremiti vozilo", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
