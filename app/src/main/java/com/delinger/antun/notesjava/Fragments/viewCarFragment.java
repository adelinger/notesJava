package com.delinger.antun.notesjava.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.delinger.antun.notesjava.DatabaseConnections.editDataByQuery;
import com.delinger.antun.notesjava.DatabaseConnections.selectDataByQuery;
import com.delinger.antun.notesjava.HelperClasses.Datum;
import com.delinger.antun.notesjava.HelperClasses.DialogMessage;
import com.delinger.antun.notesjava.HelperClasses.Margins;
import com.delinger.antun.notesjava.HelperClasses.ProgressDialogWait;
import com.delinger.antun.notesjava.Objects.car;

import com.delinger.antun.notesjava.Objects.payment;
import com.delinger.antun.notesjava.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;

public class viewCarFragment extends DialogFragment {
    private View view;
    private TextView carNameTxtr;
    private TextView receiptDateTxt;
    private TextView dispatchDateTxt;
    private TextView typeOfWorTxt;
    private TextView carPriceTxt;
    private TextView statusTxt;
    private Button   exitButton;
    private Button   markAsDispatchedB;

    private car car;
    private car newCar;
    private payment payment;
    private ProgressDialogWait progressDialog;
    private Integer carStatus;
    private Double claimSum;
    private Double claimToUpdate;
    private OnUpdateCar OnUpdateCarListener;

    public interface OnUpdateCar {
        public void isCompleted (car newCar, Double addedClaim, payment payment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.OnUpdateCarListener = (viewCarFragment.OnUpdateCar) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_car_fragment_layout, null, false);

        carNameTxtr       = view.findViewById(R.id.carNameVCF);
        receiptDateTxt    = view.findViewById(R.id.dateReceiptVCF);
        dispatchDateTxt   = view.findViewById(R.id.dispatchDateVCF);
        typeOfWorTxt      = view.findViewById(R.id.typeOfWorkVCF);
        carPriceTxt       = view.findViewById(R.id.priceofcarVCF);
        exitButton        = view.findViewById(R.id.exitVCF);
        markAsDispatchedB = view.findViewById(R.id.markAsDispatchedVCF);
        statusTxt         = view.findViewById(R.id.statusVCF);


        getCarData();

        markAsDispatchedB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.start();
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Integer save_result = 0;
                            JSONArray jsonresponse = new JSONArray(response);
                            for(int i=0;i<jsonresponse.length();i++)
                            {
                                JSONObject Jasonobject = null;
                                Jasonobject = jsonresponse.getJSONObject(i);
                                save_result =  Jasonobject.getInt("rezultat");

                                if(save_result == 0) {DialogMessage.throwNotSuccessfulMessage("Neuspješno. Pokušajte kasnije", getDialog().getContext()); return;}
                                if(save_result == 1) setCarIsFinished();

                            }
                        } catch (JSONException e) {
                            Log.e("shit", e.getMessage());
                        }

                    }
                };

                claimToUpdate = car.getCost() - claimSum;
                String query = "UPDATE payments SET claim = "+claimToUpdate+"  WHERE carID = "+car.getId()+" AND debit = "+car.getCost()+" ";
                editDataByQuery editDataByQuery = new editDataByQuery(query, listener);
                RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
                queue.add(editDataByQuery);

            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void setCarIsFinished() {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Integer save_result = 0;
                    JSONArray jsonresponse = new JSONArray(response);
                    for(int i=0;i<jsonresponse.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        Jasonobject = jsonresponse.getJSONObject(i);
                        save_result =  Jasonobject.getInt("rezultat");

                        if(save_result == 0) {DialogMessage.throwNotSuccessfulMessage("Neuspješno. Pokušajte kasnije", getDialog().getContext()); return;}
                        if(save_result == 1)  getNewCarData();

                    }
                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        String query ="UPDATE cars SET finished = 1, dispatchdate = CURRENT_TIMESTAMP WHERE id = "+car.getId()+" ";
        editDataByQuery editDataByQuery = new editDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(editDataByQuery);
    }

    private void getNewCarData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newCar = new car();
                newCar.noteList         = new ArrayList<>();
                newCar.idList           = new ArrayList<>();
                newCar.partnerIDList    = new ArrayList<>();
                newCar.dispatchDateList = new ArrayList<>();
                newCar.receiptDateList  = new ArrayList<>();
                newCar.nameList         = new ArrayList<>();
                newCar.workRequiredList = new ArrayList<>();
                newCar.costList         = new ArrayList<>();
                newCar.finishedList     = new ArrayList<>();

                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        newCar.nameList.        add(i,Jsonobject.getString("name"));
                        newCar.workRequiredList.add(i, Jsonobject.getString("requiredWork"));
                        newCar.receiptDateList. add(i, Jsonobject.getString("receiptdate"));
                        newCar.dispatchDateList.add(i, Jsonobject.getString("dispatchdate"));
                        newCar.idList.          add(i, Jsonobject.getInt("id"));
                        newCar.partnerIDList.   add(i, Jsonobject.getInt("partnerID"));
                        newCar.noteList.        add(i, Jsonobject.getString("note"));
                        newCar.costList.        add(i, Jsonobject.getDouble("cost"));
                        newCar.finishedList.    add(i, Jsonobject.getInt("finished"));
                    }

                    if(newCar.idList.size() != 0) {
                       getPaymentData();
                    }

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        String query = "SELECT * FROM cars WHERE partnerID = "+car.getPartnerID()+" ";
        selectDataByQuery selectDataByQuery = new selectDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(selectDataByQuery);
    }

    private void getPaymentData() {
        payment = new payment();
        payment.idList        = new ArrayList<>();
        payment.carIdList     = new ArrayList<>();
        payment.claimList     = new ArrayList<>();
        payment.dateList      = new ArrayList<>();
        payment.debitList     = new ArrayList<>();
        payment.partnerIdList = new ArrayList<>();
        payment.userIdList    = new ArrayList<>();

        Response.Listener<String>listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismis();
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    for (int i = 0; i < jsonresponse.length(); i++) {
                        JSONObject  Jsonobject = jsonresponse.getJSONObject(i);

                        payment.idList.add(i,        Jsonobject.getInt("id"));
                        payment.debitList.add(i,     Jsonobject.getDouble("debit"));
                        payment.claimList.add(i,     Jsonobject.getDouble("claim"));
                        payment.dateList.add(i,      Jsonobject.getString("date"));
                        payment.partnerIdList.add(i, Jsonobject.getInt("partnerID"));
                        payment.carIdList.add(i,     Jsonobject.getInt("carID"));
                        payment.userIdList.add(i,    Jsonobject.getInt("userID"));
                    }

                    if(newCar.idList.size() != 0) {
                        closeDialog("Uspješno spremljeno.");
                    }

                } catch (JSONException e) {
                    Log.e("shit", e.getMessage());
                }
            }
        };

        String query = "SELECT * FROM payments WHERE partnerID = "+car.getPartnerID()+" ";
        selectDataByQuery selectDataByQuery = new selectDataByQuery(query, listener);
        RequestQueue queue = Volley.newRequestQueue(getDialog().getContext());
        queue.add(selectDataByQuery);
    }

    private void setDrawableRight(TextView textview, Integer id){
        Drawable img = getDialog().getContext().getResources().getDrawable(id);
        img.setBounds( 0, 0, 60, 60 );
        textview.setCompoundDrawables(null, null, img,null);
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void closeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialog().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface builder, int which) {
                OnUpdateCarListener.isCompleted(newCar, claimToUpdate, payment);
                getDialog().setCancelable(false);
                setDrawableRight(statusTxt, R.drawable.ok_iconn_green);
                statusTxt.setText("Status: Završeno");
                builder.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private void getCarData() {
        progressDialog = new ProgressDialogWait(getDialog().getContext());

        car = new car();

        car.setName(getArguments        ().getString("carName"));
        car.setId(getArguments          ().getInt("carID"));
        car.setCost(getArguments        ().getDouble("carCost"));
        car.setReceiptDate(getArguments ().getString("carReceiptDate"));
        car.setDispatchDate(getArguments().getString("carDispatchDate"));
        car.setNote(getArguments        ().getString("carNote"));
        car.setId(getArguments          ().getInt("carID"));
        car.setFinished(getArguments    ().getInt("finished"));
        car.setPartnerID(getArguments   ().getInt("partnerID"));


        String date   = Datum.presloziDatum(car.getReceiptDate().substring(0,10));
        claimSum      = getArguments().getDouble("claimSum", 0);
        claimToUpdate = 0.00;

        DecimalFormat df = new DecimalFormat("#0.00");

        carNameTxtr.setText    ("Vozilo: " +car.getName());
        receiptDateTxt.setText ("Datum zaprimanja: " + date);

        if(car.getDispatchDate().equals("") || car.getDispatchDate().equals(null) || car.getDispatchDate().equals("null")) dispatchDateTxt.setText("Datum predaje: vozilo još nije predano");
        else dispatchDateTxt.setText("Datum predaje: "    +Datum.presloziDatum(car.getDispatchDate().substring(0,10)));

        if(car.getNote().equals("") || car.getNote().equals(null) || car.getNote().equals("null")) typeOfWorTxt.setText   ("Tip posla: -nije dodano");
        else typeOfWorTxt.setText   ("Tip posla: "        +car.getNote());

        carPriceTxt.setText    ("Cijena popravka: "  +df.format(car.getCost()).toString() + " €");
        if(car.getFinished().equals(1)) {
            statusTxt.setText("Status: Završeno");
            setDrawableRight(statusTxt, R.drawable.ok_iconn_green);
        }
        else {
            statusTxt.setText("Status: popravak u tijeku");
            markAsDispatchedB.setVisibility(View.VISIBLE);
            Margins.setMargins(exitButton,0,150,0,30);
        }
    }
}
