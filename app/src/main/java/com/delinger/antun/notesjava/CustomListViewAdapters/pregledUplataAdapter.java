package com.delinger.antun.notesjava.CustomListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.delinger.antun.notesjava.Objects.user;
import com.delinger.antun.notesjava.Objects.car;
import com.delinger.antun.notesjava.R;

import java.util.List;

public class pregledUplataAdapter extends ArrayAdapter<String> {
    private final Context context;
    private car car;
    private List<Integer> korisnikID;
    private List<Integer>  vozilo;
    private List<String>  datum;
    private List<Integer> iznos;
    private List<Integer> id;

    public pregledUplataAdapter(Context context, List<Integer> korisnikID, List<Integer> vozilo, car car, List<String> datum, List<Integer> iznos, List<Integer>id){
        super(context, R.layout.pregled_uplata_layout);

        this.context    = context;
        this.korisnikID = korisnikID;
        this.vozilo     = vozilo;
        this.car        = car;
        this.datum      = datum;
        this.iznos      = iznos;
        this.id         = id;
    }
    @Override
    public int getCount() {
        return id.size();
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

            if(korisnikID.get(position)==0)korisnikText = "Admin";
            if(korisnikID.get(position)==1)korisnikText ="Antun";
            if(korisnikID.get(position)==2)korisnikText ="Mato";
            else korisnikText = "Novi korisnik";

            TextView partnerTV = view.findViewById(R.id.partnerlistplace);
            TextView voziloTV  = view.findViewById(R.id.vozilolistplace);
            TextView datumTV   = view.findViewById(R.id.datumlistplace);
            TextView iznosTV   = view.findViewById(R.id.iznoslistplace);
            TextView idTV      = view.findViewById(R.id.idlistplace);

            for(int i=0; i<car.idList.size(); i++){
                if(car.idList.get(i) == vozilo.get(position)) carText = car.nameList.get(i);
            }

            partnerTV.setText(korisnikText);
            voziloTV.setText (carText);
            datumTV.setText  (datum.get(position));
            iznosTV.setText  (iznos.get(position).toString());
            idTV.setText     (id.get(position).toString());
        }


        return view;

    }
}
