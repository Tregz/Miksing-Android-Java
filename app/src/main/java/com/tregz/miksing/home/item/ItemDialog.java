package com.tregz.miksing.home.item;

import android.content.Context;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.tregz.miksing.R;
import com.tregz.miksing.base.date.DateDialog;

import java.util.Calendar;
import java.util.Date;

public class ItemDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public final static String TAG = ItemDialog.class.getSimpleName();

    private Date date;
    private ItemView listener;

    ItemDialog(@NonNull final ItemView listener, @NonNull Date  date) {
        this.date = date;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DateDialog dialog = null;
        int style = R.style.Picker;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (getContext() != null) {
            dialog = new DateDialog(getContext(), style, this, year, month, day);
            dialog.setTitle(R.string.song_release_date);
        }
        return dialog != null ? dialog : super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = cal.getTime();
        listener.release(date);
    }
}
