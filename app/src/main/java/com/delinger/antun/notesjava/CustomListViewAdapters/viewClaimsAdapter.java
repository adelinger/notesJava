package com.delinger.antun.notesjava.CustomListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class viewClaimsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private car car;
    private List<Integer> korisnikID;
    private List<Integer>  vozilo;
    private List<String>  datum;
    private List<Double> iznos;
    private List<Integer> id;

    public viewClaimsAdapter(Context context, List<Integer> korisnikID, List<Integer> vozilo, car car, List<String> datum, List<Double> iznos, List<Integer>id){
        super(context, R.layout.pregled_uplata_layout);

        this.context    = context;
        this.korisnikID = korisnikID;
        this.vozilo     = vozilo;
        this.car        = car;
        this.datum      = datum;
        this.iznos      = iznos;
        this.id         = id;

    }

    private String getDatum(String datum) {
        String returnDate = "";
        Log.e("datum", datum);
        try {
            datum = datum.replaceAll("-", " ");

            String year   = datum.substring(0, datum.indexOf(" ")).trim();
            String month = datum.substring(4,7).trim();
            String day  = datum.substring(8,11).trim();

            returnDate = day + "-" + month + "-" + year;

        }
        catch(Exception e) {
            Log.e("exception", e.getMessage());
        }
        return returnDate;
    }

    @Override
    public int getCount() {
        return iznos.size();
    }

    @Override
    public String getItem(int position) {
        return datum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pregled_uplata_layout, null, false);
            String korisnikText = "";
            String carText = "";
            String datumText = "";

            if(korisnikID.get(position).equals(0))korisnikText = "Admin";
            if(korisnikID.get(position).equals(1))korisnikText ="Antun";
            if(korisnikID.get(position).equals(2))korisnikText ="Mato";
            if(!korisnikID.get(position).equals(0) && !korisnikID.get(position).equals(1) && !korisnikID.get(position).equals(2)) korisnikText = "Novi korisnik";

            TextView partnerTV = view.findViewById(R.id.partnerlistplace);
            TextView voziloTV  = view.findViewById(R.id.vozilolistplace);
            TextView datumTV   = view.findViewById(R.id.datumlistplace);
            TextView iznosTV   = view.findViewById(R.id.iznoslistplace);
            TextView idTV      = view.findViewById(R.id.idlistplace);

            for(int i=0; i<car.idList.size(); i++){
                if(car.idList.get(i).equals(vozilo.get(position))) carText = car.nameList.get(i);
            }

            datumText = datum.get(position);

            DecimalFormat df = new DecimalFormat("#0.00");

            partnerTV.setText(korisnikText);
            voziloTV.setText (carText);
            datumTV.setText  (getDatum(datum.get(position)));
            iznosTV.setText  (df.format(iznos.get(position)).toString());
            idTV.setText     (id.get(position).toString());
        }

        return view;

    }
}
