package com.delinger.antun.notesjava.HelperClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class connection {

    public static boolean isNetworkAvailable(final Context context) {

        boolean isNetAvailable = false;
        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
                final NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if(activeNetwork != null) isNetAvailable = true;
            }
        }
        return isNetAvailable;
    }
    public static void throwNoNetworkMessage(Context context) {
        Toast.makeText(context, "Nema pristupa internetu. Molimo povežite se na internet pa pokušajte ponovno", Toast.LENGTH_LONG).show();
    }
}
