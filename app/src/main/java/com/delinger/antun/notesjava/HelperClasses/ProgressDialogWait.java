package com.delinger.antun.notesjava.HelperClasses;

import android.app.ProgressDialog;
import android.content.Context;

import com.delinger.antun.notesjava.login_activity;

public class ProgressDialogWait {
    private Context context;
    ProgressDialog progressDialog;

    public ProgressDialogWait(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    public void start() {
        progressDialog.setTitle("Učitavanje...");
        progressDialog.setMessage("Pričekajte...");
        progressDialog.show();
    }
    public void dismis() {
        progressDialog.dismiss();
    }
}
