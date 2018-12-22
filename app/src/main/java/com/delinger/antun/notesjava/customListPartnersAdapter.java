package com.delinger.antun.notesjava;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class customListPartnersAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> firstname;
    private List<String> lastname;
    private List<Integer> id;

    public customListPartnersAdapter(Context context, List<String> firstname, List<String> lastname, List<Integer>id) {
        super(context, R.layout.custom_list_partners);

        this.context   = context;
        this.firstname = firstname;
        this.lastname  = lastname;
        this.id        = id;
    }

    @Override
    public int getCount() {

        return firstname.size();
    }

    @Override
    public String getItem(int position) {
        return firstname.get(position);
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_partners, null, false);

            TextView firstnameTV = view.findViewById(R.id.textViewfn);
            TextView lastnameTV  = view.findViewById(R.id.textViewln);
            TextView idTV        = view.findViewById(R.id.textViewid);

            try{
                firstnameTV.setText(firstname.get(position).trim());
                lastnameTV.setText(lastname.get(position).trim());
                idTV.setText(id.get(position).toString().trim());

                Log.e("id", id.get(position).toString());
            } catch (Exception e) {
                Log.e("shit", e.getMessage());
            }

        }

        return view;
    }
}
