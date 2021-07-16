package com.yagi.android.taskalarm.Dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.yagi.android.taskalarm.SmallTaskActivity;
import com.yagi.android.taskalarm.TaskActivity;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TimePickerDialogFragment2 extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (SmallTaskActivity)getActivity(), hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //時刻が選択されたときの処理
    }

}