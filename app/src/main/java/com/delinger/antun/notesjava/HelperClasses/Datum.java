package com.delinger.antun.notesjava.HelperClasses;

import android.util.Log;

abstract public class Datum {
    /**
     *
     * @param datum
     * @return
     */
    public static String presloziDatum(String datum) {
        String returnDate = "";
        try {
            datum = datum.replaceAll("-", " ");

            String year   = datum.substring(0, datum.indexOf(" ")).trim();
            String month = datum.substring(4,7).trim();
            String day  = datum.substring(8,10).trim();

            returnDate = day + "-" + month + "-" + year;

        }
        catch(Exception e) {
            Log.e("exception", e.getMessage());
        }
        return returnDate;
    }
}
