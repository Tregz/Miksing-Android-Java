package com.tregz.miksing.home.work;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseDialog;

import java.util.Calendar;
import java.util.Date;

public class WorkDialog extends BaseDialog implements DatePicker.OnDateChangedListener {

    private Date date;

    WorkDialog(@NonNull ViewGroup group, @NonNull final WorkView listener, @NonNull Date at) {
        super(group.getContext());
        date = at;
        builder.setTitle(R.string.song_release_date);
        View view = inflate(group, R.layout.picker_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(at);
        int year = cal.get(Calendar.YEAR);
        int monthOfYear = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        DatePicker picker = view.findViewById(R.id.picker_date);
        picker.init(year, monthOfYear, dayOfMonth, this);
        builder.setView(view);
        builder.setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.release(date);
            }
        });
        show();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = cal.getTime();
    }
}
