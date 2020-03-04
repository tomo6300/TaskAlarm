package com.yagi.android.taskalarm.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.yagi.android.taskalarm.SmallTaskActivity;
import com.yagi.android.taskalarm.TaskActivity;

import java.util.Calendar;

public class DatePickerDialogFragment2 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(),
                (SmallTaskActivity)getActivity(),  year, month, dayOfMonth);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理

    }

}