package com.delinger.antun.notesjava.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

abstract public class DialogMessage {

    public static void throwNotSuccessfulMessage(String noSuccessMessage, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
               dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }
}
