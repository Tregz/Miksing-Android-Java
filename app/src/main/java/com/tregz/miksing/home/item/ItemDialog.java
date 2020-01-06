package com.tregz.miksing.home.item;

import android.content.Context;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tregz.miksing.R;
import com.tregz.miksing.base.date.DateDialog;

import java.util.Calendar;
import java.util.Date;

public class ItemDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public final static String TAG = ItemDialog.class.getSimpleName();
    private final static String DATE = "date";

    private Date date;
    private ItemView listener;

    static ItemDialog newInstance(@NonNull Date  date) {
        ItemDialog dialog = new ItemDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE, date);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ItemView) context;
        } catch (ClassCastException e) {
            String name = ItemView.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = (Date) getArguments().getSerializable(DATE);
        }
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
