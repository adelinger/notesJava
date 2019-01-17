package com.delinger.antun.notesjava.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.delinger.antun.notesjava.R;
import com.delinger.antun.notesjava.claimsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class calendarFragment extends DialogFragment  {
    private View view;
    private Button saveButton;
    private CalendarView calendar;

    private String previousDate;
    private String chosenDate;
    private String dateFromCalendar;
    private Boolean dateSelected;

    private Datum listener;

    public interface Datum {
        void datePicked(String datum, String dateFromCalendar);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (Datum) activity;
        }
        catch (final ClassCastException e) {
           Log.e("exception", e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_fragment_layout, null, false);

        view         = inflater.inflate(R.layout.calendar_fragment_layout, null, false);
        calendar     = view.findViewById(R.id.calendarViewAdmin);
        saveButton   = view.findViewById(R.id.calendarSaveButton);

        previousDate = getArguments().getString("previousDate");
        getPreviouslyChosenDate(previousDate);
        dateSelected = false;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dateSelected) {
                    chosenDate       = getToday();
                    dateFromCalendar = getTodayInMillis();
                }

                listener.datePicked(chosenDate, dateFromCalendar);
                getDialog().dismiss();
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                chosenDate = getDatum(i, i1+1, i2);
                Integer j = i;
                Integer j1 = i1+1;
                Integer j2 = i2;
                dateFromCalendar = j.toString()+j1.toString()+j2.toString();
                dateSelected = true;
            }
        });

        return view;
    }

    private String getTodayInMillis() {
        Calendar calender = Calendar.getInstance();
        String timeStamp  = new SimpleDateFormat("dd-MM-YYYY").format(Calendar.getInstance().getTimeInMillis());
        return timeStamp;
    }

    private String getToday() {
        Calendar calender = Calendar.getInstance();
        String timeStamp = new SimpleDateFormat("dd-MM-YYYY").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    private void getPreviouslyChosenDate(String datum) {
        try{
            if(datum != "") {

                datum = datum.replaceAll("-", " ");

                String day   = datum.substring(0, datum.indexOf(" ")).trim();
                String month = datum.substring(datum.indexOf(" ")+1, datum.indexOf(" "+2)).trim();
                String year  = datum.substring(datum.indexOf(" ")+3, datum.length()).trim();

                Calendar calendarInstance = Calendar.getInstance();
                calendarInstance.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                Long militime = calendarInstance.getTimeInMillis();
                calendar.setDate(militime);
            }

        } catch (Exception e) {
            Log.e("shit", e.getMessage());
        }
    }


    private String getDatum(Integer year, Integer month, Integer day) {
            String datum = "";
            String stringDay = day.toString();
            String stringMont= month.toString();

            if (stringDay.length() ==  1) stringDay = "0"+stringDay;
            if (stringMont.length() == 1) stringMont= "0"+stringMont;

            datum = stringDay + "-" + stringMont + "-" + year;
            return datum;
        }
}
