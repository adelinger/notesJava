package com.delinger.antun.notesjava.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.sip.SipSession;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.DatabaseConnections.editDataByQuery;
import com.delinger.antun.notesjava.DatabaseConnections.get_transactions;
import com.delinger.antun.notesjava.DatabaseConnections.selectDataByQuery;
import com.delinger.antun.notesjava.DatabaseConnections.update_payment;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.MainActivity;
import com.delinger.antun.notesjava.Objects.partner;
import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;

import com.delinger.antun.notesjava.R;
import com.delinger.antun.notesjava.claimsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class updatePaymentFragment extends DialogFragment {
    private View     view;
    private EditText dateET;
    private EditText claimET;
    private Spinner  carSpinner;
    private Button   saveButton;

    private payment payment;
    private payment newPayment;
    private car car;
    private user user;
    private partner partner;
    private calendarFragment calendar;
    ProgressDialogWait progressDialog;

    private Integer id;
    private Integer position;
    private Integer carPosition;
    private String dateFromCalendar;
    private String tag;
    private OnUpdateComplete listener;

    public interface OnUpdateComplete {
        public void updateComplete(payment payment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnUpdateComplete) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_payment_fragment_layout, null, false);

        dateET         = view.findViewById(R.id.dateET);
        claimET        = view.findViewById(R.id.claimET);
        carSpinner     = view.findViewById(R.id.voziloSpinner);
        saveButton     = view.findViewById(R.id.paymentsButton);

        instantiateObjects();
        loadSpinner();
        tag = getTag();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(claimET.getText().toString().trim().length()<1 || dateET.getText().toString().trim().length()<1) { Toast.makeText(getDialog().getContext(), "Morate upisati iznos i datum", Toast.LENGTH_LONG).show(); return;}
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismis();
                        int save_result = 0;

                        try {
                            JSONArray jsonresponse = new JSONArray(response);
                            for(int i=0;i<jsonresponse.length();i++)
                            {
                                JSONObject Jasonobject = null;
                                Jasonobject = jsonresponse.getJSONObject(i);
                                save_result =  Jasonobject.getInt("rezultat");

                                if(save_result == 0) throwNotSuccessfulMessage("Neuspješno. Pokušajte ponovno");
                                if(save_result == 1) getPaymentData();
                            }

                        } catch (JSONException e) {
                            Log.e("insert/update", e.getMessage());
                        }
                    }
                };

                progressDialog.start();
                payment.setClaim(Double.parseDouble(claimET.getText().toString()));
                if(tag == "update") {
                    update_payment update_payment = new update_payment(payment.getClaim(), dateET.getText().toString(), payment.getPartnerID(), getCarIdByPosition(), user.getId(), payment.getId(), listener);
                    RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
                    queue.add(update_payment);
                }
                else  {
                    String query = "INSERT INTO payments (claim, date, partnerID, carID, userID) " +
                                   "VALUES ("+payment.getClaim()+",CURRENT_TIMESTAMP,"+payment.getPartnerID()+", "+getCarIdByPosition()+", "+user.getId()+")";
                    editDataByQuery insert_payment = new editDataByQuery(query, listener);
                    RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
                    queue.add(insert_payment);
                }

            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                claimET.clearFocus();
               Bundle bundle = new Bundle();
               bundle.putString("previousDate", dateET.getText().toString());
               calendar.setArguments(bundle);
               calendar.show(getFragmentManager().beginTransaction(), "updatePaymentFragment");
            }
        });

        return view;
    }

    private void getPaymentData(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);
                        newPayment.idList.add(i,        Jsonobject.getInt("id"));
                        newPayment.debitList.add(i,     Jsonobject.getDouble("debit"));
                        newPayment.claimList.add(i,     Jsonobject.getDouble("claim"));
                        newPayment.dateList.add(i,      Jsonobject.getString("date"));
                        newPayment.partnerIdList.add(i, Jsonobject.getInt("partnerID"));
                        newPayment.carIdList.add(i,     Jsonobject.getInt("carID"));
                        newPayment.userIdList.add(i,    Jsonobject.getInt("userID"));

                    }
                } catch (JSONException e) {
                    Log.e("getPaymentData", e.getMessage());
                }

                closeDialog("Spremanje uspješno.");
            }
        };

        String query = "SELECT * FROM payments WHERE  partnerID = "+payment.getPartnerID()+" ";
        selectDataByQuery get_transactions = new selectDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(get_transactions);
    }

    private Integer getCarIdByPosition() {
        String carName = carSpinner.getSelectedItem().toString();
        int carID = 0;

        for (int i=0; i<car.idList.size(); i++){
            if(carName.equals(car.nameList.get(i))) carID = car.idList.get(i);
        }
        return carID;
    }

    private void closeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                listener.updateComplete(newPayment);
                builder.dismiss();
                getDialog().dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void throwNotSuccessfulMessage(String notSuccessfullMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(notSuccessfullMessage);
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

    private void loadSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDialog().getContext(), android.R.layout.simple_list_item_1, car.nameList);
        carSpinner.setAdapter(adapter);
        carSpinner.setSelection(carPosition);
    }


    private void instantiateObjects() {
        payment = (com.delinger.antun.notesjava.Objects.payment) getArguments().getSerializable("payment");
        car     = (com.delinger.antun.notesjava.Objects.car)     getArguments().getSerializable    ("car");
        user    = (com.delinger.antun.notesjava.Objects.user)    getArguments().getSerializable   ("user");
        partner = (com.delinger.antun.notesjava.Objects.partner) getArguments().getSerializable("partner");

        payment.setClaim(getArguments    ().getDouble("claim"));
        payment.setId(getArguments       ().getInt("id"));
        payment.setDate(getArguments     ().getString("date"));
        payment.setPartnerID(getArguments().getInt("partnerID"));

        position    = getArguments       ().getInt("position");
        carPosition = getArguments       ().getInt("carPosition");

        calendar   = new calendarFragment();
        newPayment = new payment();

        newPayment.idList        = new ArrayList<>();
        newPayment.carIdList     = new ArrayList<>();
        newPayment.claimList     = new ArrayList<>();
        newPayment.dateList      = new ArrayList<>();
        newPayment.debitList     = new ArrayList<>();
        newPayment.partnerIdList = new ArrayList<>();
        newPayment.userIdList    = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#0.00");

        if(payment.getClaim() > 0){
            claimET.setText(df.format(payment.getClaim()));
        } else claimET.setHint("Iznos (€)");

        dateET.setText(payment.getDate());

        progressDialog = new ProgressDialogWait(getDialog().getContext());
    }

    public void Args(Bundle args) {
        String newDate   = args.getString("newDate");
        dateFromCalendar = args.getString("dateFromCalendar");
        dateET.setText(newDate);
    }


}
