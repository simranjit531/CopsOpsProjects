package copops.com.copopsApp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import copops.com.copopsApp.R;

@SuppressLint("ValidFragment")
public class MyDatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener dateSetListener;
    public MyDatePickerFragment(DatePickerDialog.OnDateSetListener dateSetListener ){
       this.dateSetListener=dateSetListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), R.style.datepicker, dateSetListener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return dpDialog;
    }
}